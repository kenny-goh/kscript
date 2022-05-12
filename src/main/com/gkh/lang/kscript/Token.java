package com.gkh.lang.kscript;

import com.gkh.lang.kscript.enums.TokenType;

/**
 *
 */
public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    /**
     *
     * @param tokenType
     * @param lexeme
     * @param literal
     * @param line
     */
    public Token(TokenType tokenType, String lexeme, Object literal, int line) {
        this.type = tokenType;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
