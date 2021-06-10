package Inter;
import org.junit.jupiter.api.Test;
import parser.Parser;
import symbols.Env;
import symbols.Fun;

class _ExecuteTest {
   static volatile Parser parser;
   @Test
   public void action_Test(){
      try {
         parser = new Parser();
         parser.parse("src/test.txt");
         Gen_code generator=new Gen_code(parser.root);
         generator.Transferlabel();
         generator.gen_code(parser.root);
         for (Env env:Share.saved_env) {
            if (!env.getSymbol_table().isEmpty()) {
               System.out.println("symbol table:");
               for (Object lexeme : env.getSymbol_table().keySet()) {
                  System.out.println("lexeme:" + lexeme + "\t" + env.getSymbol_table().get(lexeme));
               }
            }
         }
         System.out.println("functions:");
         for(Fun fun:Share.funList)
         {
            System.out.println(fun);
         }
       System.out.println("---------------------------------------------------");
       System.out.println("Intermediate code:");
       System.out.println(parser.root.code);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}