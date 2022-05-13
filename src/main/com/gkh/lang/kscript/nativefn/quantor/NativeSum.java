package com.gkh.lang.kscript.nativefn.quantor;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.Interpreter;
import com.gkh.lang.kscript.exceptions.RuntimeError;
import com.gkh.lang.kscript.types.KList;
import com.gkh.lang.kscript.types.SumQuantor;

import java.util.List;

public class NativeSum implements Callable {
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        SumQuantor visitor = new SumQuantor();
        Object inst = arguments.get(0);
        if (inst instanceof KList) {
            return visitor.visitKList((KList) inst);
        }
        throw new RuntimeError(null, "Expect a list for sum quantor.");
    }

    @Override
    public int arity() {
        return 1;
    }
}
