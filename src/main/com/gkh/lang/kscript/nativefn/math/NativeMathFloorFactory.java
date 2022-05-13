package com.gkh.lang.kscript.nativefn.math;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.nativefn.NativeFunctionFactory;

public class NativeMathFloorFactory implements NativeFunctionFactory {
    @Override
    public String getName() {
        return "floor";
    }

    @Override
    public Callable build() {
        return new NativeMathFloor();
    }
}
