package com.svetanis.agentpatterns.base;

import com.google.common.base.Optional;

public record AgentConfigRecord(
    String name,
    String model,
    String description,
    String instruction,
    Optional<String> outputKey) {}
;
