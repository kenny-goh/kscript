package com.gkh.lang.kscript.nativefn.clock;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.Interpreter;

import java.util.List;

public class NativeClock implements Callable {
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return (double) System.currentTimeMillis() / 1000.0;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public String toString() {
        return "<native fn>";
    }
}
