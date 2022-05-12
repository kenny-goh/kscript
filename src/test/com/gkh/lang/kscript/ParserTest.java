package com.gkh.lang.kscript;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testEquality() {
        Scanner scanner = new Scanner("10 == 10;");
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        String strExpr = new AstPrinter().print(stmts.get(0));
        Assertions.assertEquals("(== 10.0 10.0)",strExpr);
    }

    @Test
    public void testPlus() {
        Scanner scanner = new Scanner("10 + 10;");
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        String strExpr = new AstPrinter().print(stmts.get(0));
        Assertions.assertEquals("(+ 10.0 10.0)",strExpr);
    }

    @Test
    public void testComparison() {
        Scanner scanner = new Scanner("1 > 2;");
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        String strExpr = new AstPrinter().print(stmts.get(0));
        Assertions.assertEquals("(> 1.0 2.0)",strExpr);
    }

    @Test
    public void testPrimaryNumber() {
        Scanner scanner = new Scanner("10;");
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        String strExpr = new AstPrinter().print(stmts.get(0));
        Assertions.assertEquals("10.0",strExpr);
    }

    @Test
    public void testPrimaryString() {
        Scanner scanner = new Scanner("\"hello\";");
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        String strExpr = new AstPrinter().print(stmts.get(0));
        Assertions.assertEquals("hello",strExpr);
    }

    @Test
    public void testPrimaryBooleanTruthy() {
        Scanner scanner = new Scanner("true;");
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        String strExpr = new AstPrinter().print(stmts.get(0));
        Assertions.assertEquals("true",strExpr);
    }

    @Test
    public void testUnary() {
        Scanner scanner = new Scanner("-10;");
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        String strExpr = new AstPrinter().print(stmts.get(0));
        Assertions.assertEquals("(- 10.0)",strExpr);
    }
}
