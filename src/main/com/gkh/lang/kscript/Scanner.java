package com.gkh.lang.kscript;

import com.gkh.lang.kscript.enums.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gkh.lang.kscript.enums.TokenType.*;

/**
 * Scanner is responsible for scanning source and returning a list of tokens.
 */
public class Scanner {
    protected final String source;
    protected final List<Token> tokens = new ArrayList<>();
    protected int start = 0;
    protected int current = 0;
    protected int line = 1;
    protected boolean isBlockComment;

    private static final Map<String, TokenType> keywords = new HashMap<>();
    static {
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("lambda", LAMBDA);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
        keywords.put("break", BREAK);
        keywords.put("extends", EXTENDS);
    }

    public Scanner(String source) {
        this.source =source;
    }

    /**
     * @return a list of tokens
     */
    public List<Token> scanTokens() {
        while(!isAtEnd()) {
            // Beginning of next lexeme
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF,"", null, line));
        return tokens;
    }

    /**
     * @return true if pointer is at end
     */
    protected boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     *
     */
    protected void scanToken() {
        char c = advance();
        if (isBlockComment) {
            if (c == '*' && match('/')) {
                isBlockComment = false;
            }
            // Ignore processing rest of the token when in block comment mode
            return;
        }
        switch(c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case '|': addToken(PIPE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!':
                addToken(match('=')  ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    while(peek() != '\n' && !isAtEnd() ) advance();
                } else if (match('*')) {
                    isBlockComment = true;
                    while((peek() != '*' && peekNext() != '/') && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"': string(); break;
            case 'o':
                if (match('r')) {
                    addToken(OR);
                }
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                  identifier();
                } else {
                    KScript.error(line, "Unexpected character.");
                }
                break;
        }
    }


    protected void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    protected boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    protected boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    protected void number() {
        while (isDigit(peek())) advance();
        // Look for fractional bit
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while(isDigit(peek())) advance();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    protected char peekNext() {
        if (current + 1 >= source.length()) return 0;
        return source.charAt(current + 1);
    }

    protected boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    protected void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            KScript.error(line, "Unterminated string.");
            return;
        }
        advance(); // closing "
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    /**
     * @return char at the current position
     */
    protected char peek() {
        if (isAtEnd()) return 0;
        return source.charAt(current);
    }

    /**
     * @param expected text to match
     * @return true if match
     */
    protected boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    protected void addToken(TokenType type) {
        this.addToken(type, null);
    }

    protected void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * @return return the current char (and shift current to the right)
     */
    protected char advance() {
        return source.charAt(current++);
    }
}
