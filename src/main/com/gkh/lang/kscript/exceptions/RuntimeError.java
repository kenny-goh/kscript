package com.gkh.lang.kscript.exceptions;

import com.gkh.lang.kscript.Token;

public class RuntimeError extends RuntimeException {
    public final Token token;
    public RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
