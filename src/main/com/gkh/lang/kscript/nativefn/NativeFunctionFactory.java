package com.gkh.lang.kscript.nativefn;

import com.gkh.lang.kscript.Callable;

public interface NativeFunctionFactory {
    String getName();
    Callable build();
}
