package fr.ensimag.deca.syntax;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class LexTest {
    /*
    SPACES|NEWLINE
    {SPACES|NEWLINE *5
    println SPACES ( SPACES*3 "HELLO WORLD" SPACES*3 ) SPACES|NEWLINES *5;
    SPACES|NEWLINES*5
    }
    * */
    @Test
    public void testLex() throws IOException {
        String sb = "";
        Path decas = Paths.get("src/test/deca/syntax/decaFiles");
        Stream<Path> paths = Files.list(decas);
        
        paths.toArray();
        paths.forEach(deca -> {
//            sb.append(Files.readAllLines(deca));
        });

        System.out.println(sb);
        System.out.println("====================================");
//        DecaLexer lex = new DecaLexer(CharStreams.fromString(program));


    }
}
