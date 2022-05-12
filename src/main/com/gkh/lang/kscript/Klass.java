package com.gkh.lang.kscript;

import java.util.List;
import java.util.Map;

public class Klass implements Callable {
    final String name;
    final Map<String, Function> methods;
    final Klass superclass;

    public Klass(String name, Klass superclass, Map<String, Function> methods) {
        this.name = name;
        this.methods = methods;
        this.superclass = superclass;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        KlassInstance instance = new KlassInstance(this);
        Function initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    @Override
    public int arity() {
        Function initializer = findMethod("init");
        if (initializer == null) return  0;
        return initializer.arity();
    }

    public Function findMethod(String name) {
        if (this.methods.containsKey(name)) {
            return this.methods.get(name);
        }
        if (superclass != null) {
            return superclass.findMethod(name);
        }
        return null;
    }
}
