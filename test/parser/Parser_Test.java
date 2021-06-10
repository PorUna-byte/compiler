package parser;

import org.junit.jupiter.api.Test;

public class Parser_Test {
    @Test
    public void ParserTest(){
        try {
           Parser parser= new Parser();
           parser.parse("src/test.txt");
           parser.Print_Tree();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
