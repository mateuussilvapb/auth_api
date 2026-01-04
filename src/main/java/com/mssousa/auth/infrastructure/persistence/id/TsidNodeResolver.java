package com.mssousa.auth.infrastructure.persistence.id;

import java.util.Optional;

public final class TsidNodeResolver {

    public static final String NODE_ID_PROPERTY = "tsid.node.id";
    public static final String NODE_COUNT_PROPERTY = "tsid.node.count";

    private static final int DEFAULT_NODE_ID = 0;
    private static final int DEFAULT_NODE_COUNT = 1;

    private TsidNodeResolver() {}

    public static int nodeId() {
        return read(NODE_ID_PROPERTY).orElse(DEFAULT_NODE_ID);
    }

    public static int nodeCount() {
        return read(NODE_COUNT_PROPERTY).orElse(DEFAULT_NODE_COUNT);
    }

    private static Optional<Integer> read(String key) {
        try {
            return Optional.ofNullable(System.getProperty(key))
                    .map(Integer::parseInt);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}