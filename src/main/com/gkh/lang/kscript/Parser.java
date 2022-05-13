package com.gkh.lang.kscript;

import com.gkh.lang.kscript.enums.TokenType;
import com.gkh.lang.kscript.exceptions.ParseError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Grammar:
 * expression   -> equality;
 * equality     -> comparison ( ( "!=" | "==" ) comparison )*
 * comparison   -> term ( ( ">" | ">=" | "<" | "<=" ) term )*
 * term         -> factor ( ( "-" | "+" ) factor )*
 * factor       -> unary ( ( "/" | "*" ) unary )*
 * unary        -> ( "!" | "-" ) unary | primary
 * primary      -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")";
 */
public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private int nestedLoopCount = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt declaration() {
        try {
            if (match(TokenType.FUN)) return function("function");
            if (match(TokenType.VAR)) return varDeclaration();
            if (match(TokenType.CLASS)) return classDeclaration();
            return statement();
        } catch (ParseError error ) {
            synchronize();
            return null;
        }
    }

    private Stmt classDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect a class name.");
        Expr.Variable superclass = null;
        if (match(TokenType.EXTENDS)) {
            consume(TokenType.IDENTIFIER, "Expect a superclass name");
            superclass = new Expr.Variable(previous());
        }
        consume(TokenType.LEFT_BRACE, "Expect a '{' before class body.");

        List<Stmt.Function> methods = new ArrayList<>();
        while(!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            methods.add(function("method"));
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after a class body" );

        return new Stmt.Class(name, superclass, methods);
    }

    private Expr finishLambda() {
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.PIPE)) {
            do {
                if (parameters.size() > KConstants.LAMBDA_ARGS_COUNT) {
                    error(peek(), "Can't have more than " +  KConstants.LAMBDA_ARGS_COUNT + " parameters");
                }
                parameters.add(consume(TokenType.IDENTIFIER, "Expect parameter name."));
            } while(match(TokenType.COMMA));
        }
        consume(TokenType.PIPE, "Expect closing '|' for lambda.");
        if (check(TokenType.LEFT_BRACE))  {
            consume(TokenType.LEFT_BRACE, "'{' expected for lambda with block closure.");
            List<Stmt> block = block();
            return new Expr.Lambda(parameters, null, block);
        }
        Expr expr = expression();
        return new Expr.Lambda(parameters, expr, null);
    }

    private Stmt.Function function(String kind) {
        Token name = consume(TokenType.IDENTIFIER, "Expect " + kind + " name.");
        consume(TokenType.LEFT_PAREN, "Expect '(' after " + kind + " name.");
        List<Token> parameters = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (parameters.size() > KConstants.MAX_FUNCTION_ARGS) {
                    error(peek(), "Can't have more than 255 parameters");
                }
                parameters.add(consume(TokenType.IDENTIFIER, "Expect parameter name."));
            } while(match(TokenType.COMMA));
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after " + kind + " name.");
        consume(TokenType.LEFT_BRACE, "Expect '{' before " + kind + " body. ");
        List<Stmt> body = block();
        return new Stmt.Function(name, parameters, body);
    }

    private Stmt varDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect a variable name.");

        Expr initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after a variable declaration.");
        return new Stmt.Var(name,initializer);
    }

    private Stmt statement() {
        if (match(TokenType.FOR)) return forStatement();
        if (match(TokenType.IF)) return ifStatement();
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.RETURN)) return returnStatement();
        if (match(TokenType.WHILE)) return whileStatement();
        if (match(TokenType.LEFT_BRACE)) return new Stmt.Block(block());
        if (match(TokenType.BREAK)) return breakStatement();
        return expressionStatement();
    }

    private Stmt returnStatement() {
        Token keyword = previous();
        Expr value = null;
        if (!check(TokenType.SEMICOLON)) {
            value = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after return value.");
        return new Stmt.Return(keyword, value);
    }

    private Stmt breakStatement() {
        Token operator = previous();
        consume(TokenType.SEMICOLON, "Expect ';' after a break statement.");
        if (nestedLoopCount == 0) {
            error(operator,  "Break statement should be used inside loop statements ");
        }
        return new Stmt.Break(operator);
    }

    private Stmt forStatement() {
        this.nestedLoopCount++;
        consume(TokenType.LEFT_PAREN, "Expect '(' after a for.");
        Stmt initializer;
        if (match(TokenType.SEMICOLON)) {
            initializer = null;
        } else if (match(TokenType.VAR)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }
        Expr condition = null;
        if (!check(TokenType.SEMICOLON)) {
            condition = expression();
        }
        consume(TokenType.SEMICOLON, "Expect ';' after a loop condition");

        Expr increment = null;
        if (!check(TokenType.RIGHT_PAREN)) {
            increment = expression();
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after for clauses");
        Stmt body = statement();
        if (increment != null) {
            body = new Stmt.Block(Arrays.asList(body, new Stmt.Expression(increment)));
        }

        if (condition == null) condition = new Expr.Literal(true);
        body = new Stmt.While(condition, body);

        if (initializer != null) {
            body = new Stmt.Block(Arrays.asList(initializer, body));
        }
        this.nestedLoopCount--;
        return body;
    }

    private Stmt whileStatement() {
        this.nestedLoopCount++;
        consume(TokenType.LEFT_PAREN, "Expect '(' after while.");
        Expr condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after while.");
        Stmt body = statement();
        this.nestedLoopCount--;
        return new Stmt.While(condition, body);
    }

    private Stmt ifStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after if.");
        Expr condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after if condition.");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }
        return new Stmt.If(condition, thenBranch, elseBranch);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(TokenType.RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new Stmt.Expression(expr);
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

     Token previous() {
        return tokens.get(current - 1);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return call();
    }

    private Expr call() {
        Expr expr = primary();
        for(;;) {
            if (match((TokenType.LEFT_PAREN))) {
                expr = finishCall(expr);
            } else if (match(TokenType.DOT)) {
                Token name = consume(TokenType.IDENTIFIER, "Expect a property after '.'.");
                expr = new Expr.Get(expr, name);
            } else if (match(TokenType.LEFT_SQUARE_BRACKET)) {
                expr = finishIndexing(expr);
            }
            else {
                break;
            }
        }
        return expr;
    }


//    private Expr call() {
//        Expr expr = primary();
//        for(;;) {
//            if (match((TokenType.LEFT_PAREN))) {
//                expr = finishCall(expr);
//            } else if (match(TokenType.DOT)) {
//              Token name = consume(TokenType.IDENTIFIER, "Expect a property after '.'.");
//              expr = new Expr.Get(expr, name);
//            } else {
//                break;
//            }
//        }
//        return expr;
//    }

    private Expr finishCall(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                if (arguments.size() >= 255) {
                    error(peek(), "Cant have more than 255 arguments.");
                }
                arguments.add(expression());
            } while (match(TokenType.COMMA));
        }
        Token paren = consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments");
        return new Expr.Call(callee, paren, arguments);
    }

    private Expr finishIndexing(Expr expr) {
        Integer number = 0;
        Expr fromIndex = expression();
//        if (match(expression())) {
//           Token numToken = previous();
//           number = ((Double) numToken.literal).intValue();
//        } else {
//            error(previous(), "Expect a number after '[' for accessing array.");
//        }
        Token last = consume(TokenType.RIGHT_SQUARE_BRACKET, "Expect ']' after accessing array.");
        return new Expr.Index(last, expr, fromIndex, null);
    }

    private Expr primary() {
        if (match(TokenType.FALSE)) return new Expr.Literal(false);
        else if (match(TokenType.TRUE)) return new Expr.Literal(true);
        else if (match(TokenType.NIL)) return new Expr.Literal(null);
        else if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Expr.Literal(previous().literal);
        }
        else if (match(TokenType.SUPER)) {
            Token keyword = previous();
            consume(TokenType.DOT, "Expect a '.' after super.");
            Token method = consume(TokenType.IDENTIFIER, "Expect a superclass method name.");
            return new Expr.Super(keyword, method);
        }
        else if (match(TokenType.THIS)) {
            return new Expr.This(previous());
        }
        else if (match(TokenType.IDENTIFIER)) {
            return new Expr.Variable(previous());
        }
        else if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }
        else if (match(TokenType.LEFT_SQUARE_BRACKET)) {
            return array();
        }
        else if (match(TokenType.PIPE)) return finishLambda();
        throw error(peek(), "Expect expression.");
    }

    private Expr array() {
        List elements = new ArrayList();
        if (!check(TokenType.RIGHT_SQUARE_BRACKET)) {
            do {
                elements.add(expression());
            } while (match(TokenType.COMMA));
        }
        consume(TokenType.RIGHT_SQUARE_BRACKET, "Expect ']' after array");
        return new Expr.Array(elements);
    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = or();
        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Expr value = assignment();
            if (expr instanceof  Expr.Variable) {
                Token name = ((Expr.Variable)expr).name;
                return new Expr.Assign(name, value);
            } else if (expr instanceof Expr.Get) {
                Expr.Get get = (Expr.Get)expr;
                return new Expr.Set(get.object, get.name, value);
            }
            error(equals, "Invalid assignment target.");
        }
        return expr;
    }

    private Expr or() {
        Expr expr = and();
        while(match(TokenType.OR)) {
            Token operator = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, operator, right);
        }
        return expr;
    }

    private Expr and() {
        Expr expr = equality();
        while (match(TokenType.AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, operator, right);
        }

        return expr;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        KScript.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while(!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;
            switch (peek().type) {
                case CLASS:
                case FOR:
                case FUN:
                case IF:
                case PRINT:
                case RETURN:
                case VAR:
                case WHILE: return;
            }
            advance();
        }
    }


}
