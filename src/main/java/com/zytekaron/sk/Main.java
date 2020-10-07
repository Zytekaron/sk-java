/*
   Copyright 2020 Michael Thornes

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.zytekaron.sk;

import com.zytekaron.sk.interpret.Interpreter;
import com.zytekaron.sk.lex.Lexer;
import com.zytekaron.sk.parse.Parser;
import com.zytekaron.sk.parse.nodes.Node;
import com.zytekaron.sk.struct.Context;
import com.zytekaron.sk.struct.Token;
import com.zytekaron.sk.struct.VariableTable;
import com.zytekaron.sk.types.SkDouble;
import com.zytekaron.sk.types.SkNull;
import com.zytekaron.sk.types.SkValue;
import com.zytekaron.sk.types.error.SkError;
import com.zytekaron.sk.struct.result.LexResult;
import com.zytekaron.sk.struct.result.ParseResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final Context globalContext = new Context("<program>");
    private static final VariableTable globalVariables = createGlobalVariables();
    
    public static void main(String[] args) {
        String text1 = "2 * (2 ** -5. / 3) - (1./3) // lol 55";
        String text2 = "123.456 + 789 + \"this is \\\" \\' \\n \\t a string\" + 445";
        String text3 = "(var abc = 123) + 7 + abc";
        
        globalContext.setVariableTable(globalVariables);
        
        String content = Objects.requireNonNull(getResourceFileAsString("test.sk"));
        String[] lines = content.split("\n");
        
        for (String line : lines) run(line.trim());
//        repl();
    }
    
    private static void repl() {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print("sk> ");
            input = scanner.nextLine().trim();
            run(input);
        } while (!input.equalsIgnoreCase(".exit"));
    }
    
    private static void run(String text) {
        Lexer lexer = new Lexer(text);
        LexResult<List<Token>> lexerParseResult = lexer.tokenize();
    
        System.out.println("=== Lexer ===");
        if (!lexerParseResult.success()) {
            lexerParseResult.getError().raise();
            return;
        }
        List<Token> tokens = lexerParseResult.getResult();
        System.out.println("Tokens: " + tokens);
    
//        if (true) return;
    
        System.out.println("=== Parser ===");
        Parser parser = new Parser(tokens);
        ParseResult headParseResult = parser.parse();
        if (!headParseResult.success()) {
            headParseResult.getError().raise();
            return;
        }
        Node node = headParseResult.getResult();
        System.out.println("Node: " + node);
        if (node == null) return;
        
        System.out.println("=== Interpreter ===");
        Interpreter interpreter = new Interpreter();
        SkValue value = interpreter.visit(node, globalContext);
        if (value instanceof SkError) {
            ((SkError) value).raise();
            return;
        }
        System.out.println("Result: " + value.getType() + "(" + value + ")");
        
        System.out.println('\n');
    }
    
    private static VariableTable createGlobalVariables() {
        VariableTable table = new VariableTable();
        table.put("null", new SkNull());
        table.put("PI", new SkDouble(Math.PI));
        table.put("E", new SkDouble(Math.E));
        return table;
    }
    
    private static String getResourceFileAsString(String fileName) {
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            try (InputStream in = classLoader.getResourceAsStream(fileName)) {
                if (in == null) return null;
                try (InputStreamReader isr = new InputStreamReader(in);
                     BufferedReader reader = new BufferedReader(isr)) {
                     return reader.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
}