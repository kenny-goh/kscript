package com.gkh.lang.kscript.types;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MaxQuantor implements Quantor {
    @Override
    public Object visitKList(KList list) {
        List<Double> numbers = list.list.stream().map(it->(Double)it).collect(Collectors.toList());
        return Collections.max(numbers);
    }
}
