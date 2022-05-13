package com.gkh.lang.kscript.nativefn.assertion;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.nativefn.NativeFunctionFactory;

public class NativeAssertFactory implements NativeFunctionFactory {
    @Override
    public String getName() {
        return "assert";
    }

    @Override
    public Callable build() {
        return new NativeAssert();
    }
}
