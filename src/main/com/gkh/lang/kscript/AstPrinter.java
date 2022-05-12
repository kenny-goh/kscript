package com.gkh.lang.kscript;

import com.gkh.lang.kscript.enums.TokenType;

/**
 * Print the AST Tree node using the LISP notation (token expr...)
 * eg. (+ 1 3)
 */
public class AstPrinter implements Expr.Visitor<String> {

    String print(Expr expr) {
        return expr.accept(this);
    }

    String print(Stmt stmt) {
        return ((Stmt.Expression)stmt).expression.accept(this);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return null;
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return parenthesize("logical", expr.left, expr.right);
    }

    @Override
    public String visitSetExpr(Expr.Set expr) {
        return null;
    }

    @Override
    public String visitSuperExpr(Expr.Super expr) {
        return null;
    }

    @Override
    public String visitThisExpr(Expr.This expr) {
        return null;
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable variable) {
        return null;
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return parenthesize("function", expr.callee);
    }

    @Override
    public String visitGetExpr(Expr.Get expr) {
        return null;
    }

    @Override
    public String visitLambdaExpr(Expr.Lambda expr) {
        return parenthesize("lambda", expr.expr);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr: exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    public static void main(String[] args) {
        Expr expr = new Expr.Binary(
                new Expr.Unary(new Token(TokenType.MINUS, "-", null,1), new Expr.Literal(123)),
                new Token(TokenType.STAR, "*",  null, 1),
                new Expr.Grouping(new Expr.Literal(45.67))
        );
        System.out.println(new AstPrinter().print(expr));
    }
}
