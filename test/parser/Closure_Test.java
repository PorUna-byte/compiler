package parser;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Closure_Test {
    @Test
    public void ClosureTest(){
        try {
            Productions_Builder.init();
            List<Production> productions = Productions_Builder.getAllProductions();
            Set<String> non_terminals = Productions_Builder.getNon_terminals();
            Set<String> terminals = Productions_Builder.getTerminals();
            for (Production production : productions) {
                System.out.println(production);
            }
            System.out.println("non-terminals----------------");
            for (String non_terminal : non_terminals)
                System.out.println(non_terminal);
            System.out.println("terminals----------------");
            for (String terminal : terminals)
                System.out.println(terminal);

            Set<Item> inital = new HashSet<>();
            inital.add(new Item(productions.get(1), 1, Set.of("$")));
            for (Item item : (new Closure(inital)).getItems())
                System.out.println(item);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
