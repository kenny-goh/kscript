package com.gkh.lang.kscript;

import com.gkh.lang.kscript.exceptions.RuntimeError;

import java.util.HashMap;
import java.util.Map;

public class KlassInstance {
    private Klass klass;
    private final Map<String, Object> fields = new HashMap<>();

    public KlassInstance(Klass klass) {
        this.klass = klass;
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }

    public Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }
        Function method = klass.findMethod(name.lexeme);
        if (method != null) return method.bind(this);
        throw new RuntimeError(name,"Undefined property '" + name.lexeme + ".");
    }


    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
}
