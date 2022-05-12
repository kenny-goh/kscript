package com.gkh.lang.kscript.nativefn;

import java.util.Collections;
import java.util.List;

public interface NativeFunctionPlugin {
    default List<NativeFunctionFactory> getNativeFunctionFactories() {
        return Collections.emptyList();
    }
}
