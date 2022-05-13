package com.gkh.lang.kscript.nativefn;


import com.gkh.lang.kscript.nativefn.assertion.NativeAssertFactory;
import com.gkh.lang.kscript.nativefn.clock.NativeClockFactory;
import com.gkh.lang.kscript.nativefn.str.NativeStrFactory;

import java.util.List;

public class DefaultNativePlugin implements NativeFunctionPlugin {
    @Override
    public List<NativeFunctionFactory> getNativeFunctionFactories() {
        return List.of(new NativeClockFactory(),
                new NativeStrFactory(),
                new NativeAssertFactory());
    }
}
