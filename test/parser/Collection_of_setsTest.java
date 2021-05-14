package parser;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;
class Collection_of_setsTest {
   @Test
   public void get_Collectios_of_sets() {
      try {
         Productions_Builder.init();
         Iterator<Set<Item>> collection = Collection_of_sets.get_Collectios_of_sets().iterator();
         int i = 0;
         while (collection.hasNext()) {
            Set<Item> this_set = collection.next();
            Iterator<Item> iterator = this_set.iterator();
            System.out.println("I" + i++);
            while (iterator.hasNext()) {
               System.out.println(iterator.next());
            }
            System.out.println();
         }
      }catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}