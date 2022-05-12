package com.gkh.lang.kscript;

import com.gkh.lang.kscript.enums.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class ScannerTest {

    @Test
    public void testGreater() {
        Scanner scanner = new Scanner(">");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(TokenType.GREATER, tokens.get(0).type);
    }

    @Test
    public void testLessEqual() {
        Scanner scanner = new Scanner("<=");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(TokenType.LESS_EQUAL, tokens.get(0).type);
    }

    @Test
    public void testLess() {
        Scanner scanner = new Scanner("<");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(TokenType.LESS, tokens.get(0).type);
    }

    @Test
    public void testEqual() {
        Scanner scanner = new Scanner("=");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(TokenType.EQUAL, tokens.get(0).type);
    }

    @Test
    public void testGreaterEqual() {
        Scanner scanner = new Scanner(">=");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(TokenType.GREATER_EQUAL, tokens.get(0).type);
    }

    @Test
    public void testStringTokenLength() {
        Scanner scanner = new Scanner("\"hello world\"");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(tokens.size(), 2);
    }

    @Test
    public void testStringTokenType() {
        Scanner scanner = new Scanner("\"hello world\"");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(token.type, TokenType.STRING);
    }

    @Test
    public void testNumberLength() {
        Scanner scanner = new Scanner("100.50");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(tokens.size(), 2);
    }

    @Test
    public void testNumberTokenType() {
        Scanner scanner = new Scanner("100.50");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(token.type, TokenType.NUMBER);
    }

    @Test
    public void testVarLength() {
        Scanner scanner = new Scanner("var");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(tokens.size(), 2);
    }

    @Test
    public void testVarTokenType() {
        Scanner scanner = new Scanner("var");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(token.type, TokenType.VAR);
    }

    @Test
    public void testIdentifierLength() {
        Scanner scanner = new Scanner("foo");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(tokens.size(), 2);
    }

    @Test
    public void testIdentifierType() {
        Scanner scanner = new Scanner("foo");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(token.type, TokenType.IDENTIFIER);
    }

    @Test
    public void testSingleCharacterToken() {
        Scanner scanner = new Scanner("=");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(token.type, TokenType.EQUAL);
    }

    @Test
    public void testDoubleCharacterToken() {
        Scanner scanner = new Scanner("==");
        List<Token> tokens = scanner.scanTokens();
        Token token = tokens.get(0);
        Assertions.assertEquals(token.type, TokenType.EQUAL_EQUAL);
    }

    @Test
    public void testMultipleTokensExample1Length() {
        Scanner scanner = new Scanner("var foo=\"bar\"");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(tokens.size(), 5);
    }

    @Test
    public void testMultipleTokensExample1() {
        Scanner scanner = new Scanner("var foo=\"bar\"");
        List<Token> tokens = scanner.scanTokens();
        Token varToken = tokens.get(0);
        Assertions.assertEquals(varToken.type, TokenType.VAR);
        Token identifierToken = tokens.get(1);
        Assertions.assertEquals(identifierToken.type, TokenType.IDENTIFIER);
        Token eqToken = tokens.get(2);
        Assertions.assertEquals(eqToken.type, TokenType.EQUAL);
        Token strToken = tokens.get(3);
        Assertions.assertEquals(strToken.type, TokenType.STRING);
    }

    @Test
    public void testScan() {
        Scanner scanner = new Scanner("var foo=\"bar\"");
        Assertions.assertEquals(0, scanner.current);
        scanner.scanToken(); // var
        Assertions.assertEquals(3, scanner.current);
        scanner.scanToken(); // space
        Assertions.assertEquals(4, scanner.current);
        scanner.scanToken(); // foo
        Assertions.assertEquals(7, scanner.current);
        scanner.scanToken(); // =
        Assertions.assertEquals(8, scanner.current);
        scanner.scanToken(); // "bar"
        Assertions.assertEquals(13, scanner.current);
    }

    @Test
    public void testIsAtEnd() {
        Scanner scanner = new Scanner("var");
        Assertions.assertFalse(scanner.isAtEnd());
        scanner.scanToken(); // var
        Assertions.assertTrue(scanner.isAtEnd());
    }

    @Test
    public void testPeek() {
        Scanner scanner = new Scanner("var");
        char peek = scanner.peek();
        Assertions.assertEquals('v', peek);
    }

    @Test
    public void testCommentShouldIgnoreRest() {
        Scanner scanner = new Scanner("// this is a test");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(1, tokens.size());
    }

    @Test
    public void testBlockComment() {
        Scanner scanner = new Scanner("/* this is a test \n this a test */ var foo=\"bar\"");
        List<Token> tokens = scanner.scanTokens();
        Assertions.assertEquals(tokens.size(), 5);
    }
}
