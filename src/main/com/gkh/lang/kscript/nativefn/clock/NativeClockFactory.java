package com.gkh.lang.kscript.nativefn.clock;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.nativefn.NativeFunctionFactory;

public class NativeClockFactory implements NativeFunctionFactory {
    @Override
    public String getName() {
        return "clock";
    }
    @Override
    public Callable build() {
        return new NativeClock();
    }
}
