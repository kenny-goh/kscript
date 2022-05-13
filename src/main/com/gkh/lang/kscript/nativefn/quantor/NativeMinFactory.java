package com.gkh.lang.kscript.nativefn.quantor;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.nativefn.NativeFunctionFactory;

public class NativeMinFactory implements NativeFunctionFactory {
    @Override
    public String getName() {
        return "min";
    }

    @Override
    public Callable build() {
        return new NativeMin();
    }
}
