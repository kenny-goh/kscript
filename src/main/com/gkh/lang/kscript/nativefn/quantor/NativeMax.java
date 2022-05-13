package com.gkh.lang.kscript.nativefn.quantor;

import com.gkh.lang.kscript.Callable;
import com.gkh.lang.kscript.Interpreter;
import com.gkh.lang.kscript.exceptions.RuntimeError;
import com.gkh.lang.kscript.types.KList;
import com.gkh.lang.kscript.types.MaxQuantor;

import java.util.List;

public class NativeMax implements Callable {

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        MaxQuantor visitor = new MaxQuantor();
        Object inst = arguments.get(0);
        if (inst instanceof KList) {
            return visitor.visitKList((KList) inst);
        }
        throw new RuntimeError(null, "Expect a list for max quantor.");
    }

    @Override
    public int arity() {
        return 1;
    }
}
