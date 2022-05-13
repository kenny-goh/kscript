package com.gkh.lang.kscript.nativefn;


import com.gkh.lang.kscript.nativefn.assertion.NativeAssertFactory;
import com.gkh.lang.kscript.nativefn.clock.NativeClockFactory;
import com.gkh.lang.kscript.nativefn.math.NativeMathFloorFactory;
import com.gkh.lang.kscript.nativefn.quantor.NativeMaxFactory;
import com.gkh.lang.kscript.nativefn.quantor.NativeMinFactory;
import com.gkh.lang.kscript.nativefn.quantor.NativeSumFactory;
import com.gkh.lang.kscript.nativefn.str.NativeStrFactory;

import java.util.List;

public class DefaultNativePlugin implements NativeFunctionPlugin {
    @Override
    public List<NativeFunctionFactory> getNativeFunctionFactories() {
        return List.of(new NativeClockFactory(),
                new NativeStrFactory(),
                new NativeAssertFactory(),
                new NativeMaxFactory(),
                new NativeMinFactory(),
                new NativeSumFactory(),
                new NativeMathFloorFactory());
    }
}
