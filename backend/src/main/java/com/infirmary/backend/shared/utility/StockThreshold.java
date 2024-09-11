package com.infirmary.backend.shared.utility;

import lombok.Getter;

@Getter
public enum StockThreshold {
    DEFAULT_THRESHOLD(100L);
    private final long l;

    StockThreshold(long l) {
        this.l = l;
    }
}
