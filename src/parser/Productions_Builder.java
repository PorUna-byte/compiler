package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Productions_Builder {
    private final static List<Production> allProductions=new ArrayList<>();
    private final static Set<String>   non_terminals=new HashSet<>();
    private final static Set<String> terminals=new HashSet<>();

    public static List<Production> getAllProductions() {
        return allProductions;
    }

    public static Set<String> getNon_terminals() {
        return non_terminals;
    }

    public static Set<String> getTerminals() {
        return terminals;
    }

    private static void init_productions(List<String> Grammars)
    {
        Iterator<String> iterator=Grammars.iterator();
        while(iterator.hasNext())
        {
            String current=iterator.next();
            String left=current.split("->")[0];
            List<String> right= Arrays.asList(current.split("->")[1].split(" "));
            Production production=new Production(left,right);
            allProductions.add(production);
        }
    }
    private static void init_nonterminals()
    {
        for(int i=0;i<allProductions.size();i++)
        {
            non_terminals.add(allProductions.get(i).getLeft());
        }
    }
    private static void init_terminals()
    {
        for(int i=0;i<allProductions.size();i++)
        {
            List<String> right=allProductions.get(i).getRight();
            for(int j=0;j<right.size();j++)
            {
                if(!non_terminals.contains(right.get(j)))
                    terminals.add(right.get(j));
            }
        }
        terminals.add("$");
    }
    public static void init() throws IOException {
        List<String> Grammars=new ArrayList<>();
        BufferedReader bufferedReader=new BufferedReader(new FileReader("src/grammar.txt"));
        String newline= bufferedReader.readLine();
        while (newline!=null) {  //read productions from grammar.txt
            Grammars.add(newline);
            newline=bufferedReader.readLine();
        }
        First.init();
        init_productions(Grammars);  //the sequence of initialization is fixed.
        init_nonterminals();
        init_terminals();
    }
}
