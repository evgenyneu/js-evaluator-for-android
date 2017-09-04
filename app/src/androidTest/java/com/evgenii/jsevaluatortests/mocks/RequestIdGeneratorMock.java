package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.RequestIdGenerator;

public class RequestIdGeneratorMock extends RequestIdGenerator {

    private int nextGenerated = 1;

    public void setNextGenerated(int nextGenerated) {
        this.nextGenerated = nextGenerated;
    }

    @Override
    public int generate() {
        return nextGenerated;
    }
}
