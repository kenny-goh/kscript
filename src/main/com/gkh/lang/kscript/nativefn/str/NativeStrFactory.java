package com.gkh.lang.kscript.nativefn.str;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.nativefn.NativeFunctionFactory;

public class NativeStrFactory implements NativeFunctionFactory {
    @Override
    public String getName() {
        return "str";
    }

    @Override
    public Callable build() {
        return new NativeStr();
    }
}
