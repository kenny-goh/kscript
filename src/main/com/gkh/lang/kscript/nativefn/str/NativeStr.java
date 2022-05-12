package com.gkh.lang.kscript.nativefn.str;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.Interpreter;

import java.util.List;

public class NativeStr implements Callable {
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return arguments.get(0).toString();
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public String toString() {
        return "<native fn>";
    }
}
