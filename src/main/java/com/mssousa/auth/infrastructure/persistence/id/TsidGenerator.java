package com.mssousa.auth.infrastructure.persistence.id;

import org.springframework.stereotype.Component;

import com.mssousa.auth.domain.model.shared.IdGenerator;

import io.hypersistence.tsid.TSID;

@Component
public final class TsidGenerator implements IdGenerator {
    private static final TSID.Factory FACTORY = TSID.Factory.builder()
            .withNodeBits(nodeBits(TsidNodeResolver.nodeCount()))
            .withNode(TsidNodeResolver.nodeId())
            .build();

    @Override
    public Long generate() {
        return FACTORY.generate().toLong();
    }

    private static int nodeBits(int nodeCount) {
        return (int) (Math.log(nodeCount) / Math.log(2));
    }
}