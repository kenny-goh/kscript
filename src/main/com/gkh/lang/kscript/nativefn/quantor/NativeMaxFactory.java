package com.gkh.lang.kscript.nativefn.quantor;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.nativefn.NativeFunctionFactory;

public class NativeMaxFactory implements NativeFunctionFactory {
    @Override
    public String getName() {
        return "max";
    }

    @Override
    public Callable build() {
        return new NativeMax();
    }
}
