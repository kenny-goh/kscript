package com.gkh.lang.kscript.nativefn.math;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.Interpreter;

import java.util.List;

public class NativeMathFloor implements Callable {

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Double number = (Double) arguments.get(0);
        return Math.floor(number);
    }

    @Override
    public int arity() {
        return 1;
    }
}
