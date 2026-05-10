package com.svetanis.agents.plugins;

import static com.google.api.client.util.Preconditions.checkArgument;

import com.google.adk.agents.CallbackContext;
import com.google.adk.models.LlmRequest;
import com.google.adk.models.LlmResponse;
import com.google.adk.plugins.BasePlugin;
import com.google.common.base.Joiner;
import io.reactivex.rxjava3.core.Maybe;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.commons.lang3.Strings;

public class RetryPlugin extends BasePlugin {

  private static final Logger logger = Logger.getLogger(RetryPlugin.class.getName());

  private final int maxRetries;
  private final Duration baseDelay;
  private final ConcurrentMap<String, Integer> attempts = new ConcurrentHashMap<>();

  public RetryPlugin(int maxRetries, Duration baseDelay) {
    super("retry-plugin");
    checkArgument(maxRetries > 0, "maxRetries must be greater than zero");
    this.maxRetries = maxRetries;
    this.baseDelay = baseDelay;
  }

  @Override
  public Maybe<LlmResponse> afterModelCallback(CallbackContext callbackCtx, LlmResponse response) {
    attempts.remove(retryKey(callbackCtx));
    return Maybe.empty();
  }

  @Override
  public Maybe<LlmResponse> onModelErrorCallback(
      CallbackContext callbackCtx, LlmRequest.Builder requestBuilder, Throwable error) {
    if (!isRateLimited(error)) {
      attempts.remove(retryKey(callbackCtx));
      return Maybe.error(error);
    }
    String retryKey = retryKey(callbackCtx);
    int currAttempts = attempts.getOrDefault(retryKey, 0);
    if (currAttempts >= maxRetries) {
      logger.warning("Max retries reached for " + retryKey + ". Failing request.");
      attempts.remove(retryKey);
      return Maybe.error(error);
    }
    attempts.put(retryKey, currAttempts + 1);
    long delay = Math.max(0L, baseDelay.toMillis() * (1L << currAttempts));
    String msg = "Rate limit hit for %s. Retrying attempt #%d after %d ms delay.";
    logger.warning(String.format(msg, retryKey, currAttempts + 1, delay));
    return Maybe.timer(delay, TimeUnit.MILLISECONDS).flatMap(t -> Maybe.empty());
  }

  private String retryKey(CallbackContext callbackCtx) {
    String sid = callbackCtx.sessionId();
    String agentName = callbackCtx.agentName();
    // We remove invocationId because a retry attempt usually generates a new ID.
    // Using Session + AgentName allows us to track attempts for this specific task.
    return Joiner.on(".").join(sid, agentName);
  }

  private boolean isRateLimited(Throwable throwable) {
    Throwable curr = throwable;
    while (curr != null) {
      String className = curr.getClass().getSimpleName().toLowerCase(Locale.ROOT);
      String message = curr.getMessage() != null ? curr.getMessage().toLowerCase(Locale.ROOT) : "";
      if (className.contains("rateLimit") || className.contains("toomanyrequests")) {
        return true;
      }
      if (containsAny(message)) {
        return true;
      }
      curr = curr.getCause();
    }
    return false;
  }

  private boolean containsAny(String s) {
    return Strings.CI.containsAny(s, "429", "rate limit", "too many requests");
  }
}
