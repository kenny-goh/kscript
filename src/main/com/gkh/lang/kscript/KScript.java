package com.gkh.lang.kscript;

import com.gkh.lang.kscript.enums.TokenType;
import com.gkh.lang.kscript.exceptions.RuntimeError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * K Interpreted Programming Language.
 *
 * This language was written based on the lessons from the Crafting Interpreters. I have added additional language
 * features that wasn't in the book
 *
 * - Lambda functions similar to rust e.g |x| -> x
 * - Basic collection type such as list, map, pair and set
 *
 */
public class KScript {

    private static final Interpreter interpreter = new Interpreter();
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    /**
     * K as an interpretive eval loop session
     * @throws IOException
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for (;;) {
            System.out.println("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
            hadRuntimeError = false;
        }
    }

    /**
     * Run K script from the given file
     * @param path
     * @throws IOException
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    /**
     *
     * @param source
     */
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();
        if (hadError) return;
        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(stmts);
        if (hadError) return;
        interpreter.interpret(stmts);
        //System.out.println(new AstPrinter().print(expression));
    }

    /**
     *
     * @param line
     * @param message
     */
    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    /**
     * Report error
     * @param line
     * @param where
     * @param message
     */
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line +"] Error " + where +": " + message);
        hadError =  true;
    }

    public static void main(String[] args) throws Exception {
        // fixme: validate lox extension (.lox) if file nameis given
        if (args.length >  1) {
            System.out.println("Usage: kscript [script].ks");
            System.exit(64);
        } else if ( args.length == 1) {
            if (!args[0].endsWith("ks")) {
                System.out.println("Usage: kscript [script].ks");
                System.exit(64);
            }
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }
}
