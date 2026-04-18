package com.svetanis.agents.currency;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import com.google.common.base.Optional;

import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.fx.FxSymbols;

public class RateProvider implements Provider<Optional<Double>> {

  private static final String SYMBOL = "%s%s=X";

  public RateProvider(String base, String target) {
    this.base = checkNotNull(base, "base");
    this.target = checkNotNull(target, "target");
  }

  private final String base;
  private final String target;

  @Override
  public Optional<Double> get() {
    String symbol = String.format(SYMBOL, base, target);
    Optional<Double> fxRate = fxRate(symbol);
    if (fxRate.isPresent()) {
      return fxRate;
    }
    // fallback
    Map<String, Double> rates = rates();
    if (rates.containsKey(symbol)) {
      return of(rates.get(symbol));
    } else {
      return absent();
    }
  }

  private static Optional<Double> fxRate(String symbol) {
    try {
      FxQuote quote = YahooFinance.getFx(symbol);
      if (quote != null && quote.getPrice() != null) {
        BigDecimal rate = quote.getPrice();
        return of(rate.doubleValue());
      } else {
        return absent();
      }
    } catch (IOException e) {
      return absent();
    }
  }

  private static Map<String, Double> rates() {
    Map<String, Double> map = new HashMap<>();
    map.put(FxSymbols.USDEUR, 0.93);
    map.put(FxSymbols.USDJPY, 157.50);
    map.put(FxSymbols.USDGBP, 0.76);
    return map;
  }
}
