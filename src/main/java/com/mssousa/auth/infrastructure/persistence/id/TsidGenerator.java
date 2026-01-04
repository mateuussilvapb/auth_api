package com.mssousa.auth.infrastructure.persistence.id;

import io.hypersistence.tsid.TSID;

public final class TsidGenerator {
    private static final TSID.Factory FACTORY = TSID.Factory.builder()
            .withNodeBits(nodeBits(TsidNodeResolver.nodeCount()))
            .withNode(TsidNodeResolver.nodeId())
            .build();

    private TsidGenerator() {
    }

    public static Long generateLong() {
        return FACTORY.generate().toLong();
    }

    private static int nodeBits(int nodeCount) {
        return (int) (Math.log(nodeCount) / Math.log(2));
    }
}