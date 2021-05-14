package parser;

import org.junit.jupiter.api.Test;

public class LRTableGen_Test {
    @Test
    public void LRTableGenTest(){
        try {
            LRTableGen lrTableGen = new LRTableGen();
            lrTableGen.Generate();
            System.out.println(lrTableGen.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
