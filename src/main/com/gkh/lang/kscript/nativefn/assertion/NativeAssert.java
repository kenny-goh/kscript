package com.gkh.lang.kscript.nativefn.assertion;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.Expr;
import com.gkh.lang.kscript.Interpreter;

import java.util.List;

public class NativeAssert implements Callable {

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        assert(interpreter.evaluate((Expr) arguments.get(0)) == Boolean.TRUE);
        return null;
    }

    @Override
    public int arity() {
        return 1;
    }
}
