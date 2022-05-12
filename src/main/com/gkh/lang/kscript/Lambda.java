package com.gkh.lang.kscript;

import com.gkh.lang.kscript.enums.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Lambda allows lambda function such as |x|-> x * x to be passed as argument to another function
 */
public class Lambda implements Callable {

    private final static Token RETURN =  new Token(TokenType.RETURN, "return","",0);
    private final Expr.Lambda lambda;
    private final Environment closure;

    public Lambda(Expr.Lambda lambda, Environment closure) {
        this.lambda = lambda;
        this.closure = closure;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(this.closure);
        for (int i = 0; i < lambda.params.size(); i++) {
            environment.define(lambda.params.get(i).lexeme, arguments.get(i));
        }
        try {
            List<Stmt> block = new ArrayList<>();
            if (lambda.expr != null) {
                block.add(new Stmt.Return(RETURN, lambda.expr));
            } else if (lambda.block != null) {
                block.addAll(lambda.block);
            } else {
                throw new RuntimeException("Unreachable code");
            }
            interpreter.executeBlock(block, environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }
        return null;
    }

    @Override
    public int arity() {
        return lambda.params.size();
    }

    @Override
    public String toString() {
        return "<lambda>";
    }
}
