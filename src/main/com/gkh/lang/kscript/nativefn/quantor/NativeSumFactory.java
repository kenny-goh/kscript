package com.gkh.lang.kscript.nativefn.quantor;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.nativefn.NativeFunctionFactory;

public class NativeSumFactory implements NativeFunctionFactory {
    @Override
    public String getName() {
        return "sum";
    }

    @Override
    public Callable build() {
        return new NativeSum();
    }
}
