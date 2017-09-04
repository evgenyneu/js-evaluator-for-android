package com.evgenii.jsevaluator;

import java.util.UUID;

public class RequestIdGenerator {

    public int generate(){
        return UUID.randomUUID().hashCode();
    }
}
