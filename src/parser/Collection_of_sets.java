package parser;
import java.util.*;

public class Collection_of_sets {
    public static Set<Set<Item>> get_Collectios_of_sets() {
        List<Production> productions = Productions_Builder.getAllProductions();
        Set<String> non_terminals = Productions_Builder.getNon_terminals();
        Set<String> terminals = Productions_Builder.getTerminals();
        Set<String> all_symbols=new HashSet<>();
        all_symbols.addAll(non_terminals);
        all_symbols.addAll(terminals);
        Set<Set<Item>> Collection_of_sets = new HashSet<>();
        Set<Item> inital = new HashSet<>();
        inital.add(new Item(productions.get(0), 0, Set.of("$")));
        Collection_of_sets.add(new Closure(inital).getItems());//initialize C to {CLOSURE({[S'->.S,$]})}
        Set<Set<Item>> newly_added_sets = new HashSet<>();
        while (true) {
            for (Set<Item> I:Collection_of_sets) {
                //for each non-terminals add GOTO(I,X) into C is it is non-empty
                //No need to consider redundancy since C is a set.
                {
                    for (String symbol : all_symbols) {
                        Set<Item> GOTOIX = new GOTO_I_X(I, symbol).result();
                        if (!GOTOIX.isEmpty()&&!Collection_of_sets.contains(GOTOIX))
                            newly_added_sets.add(GOTOIX);
                    }
                }
            }
            if (newly_added_sets.isEmpty())
                break;
            else {
                Collection_of_sets.addAll(newly_added_sets);
                newly_added_sets.clear();
            }
        }
        return Collection_of_sets;
    }
}
