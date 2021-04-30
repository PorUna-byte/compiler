package lexer;

import lexer.*;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static final List<String> errors=new ArrayList<>();
    public static void main(String[] args)
    {
        try {
            Lexer lex = new Lexer("src/test.txt");
            Token tok;
            while(true)
            {
                tok=lex.scan();
                if(tok.getTag()>255)
                System.out.println(tok);
                else
                {
                    errors.add("Lexical error at Line ["+lex.getLine()+"]: illegal characters.");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
