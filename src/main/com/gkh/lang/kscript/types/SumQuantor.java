package com.gkh.lang.kscript.types;

import java.util.List;
import java.util.stream.Collectors;

public class SumQuantor implements Quantor {
    @Override
    public Object visitKList(KList list) {
        List<Double> numbers = list.list.stream().map(it->(Double)it).collect(Collectors.toList());
        return numbers.stream().reduce(0.0, Double::sum);
    }
}
