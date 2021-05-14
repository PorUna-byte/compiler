package parser;

import lexer.Tag;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class First {
    private final List<String>Sentential_form=new ArrayList<>();
    public static final HashMap<String,List<String>> firsts=new HashMap<>();
    public First(List<String> sentential_form){
        this.Sentential_form.addAll(sentential_form);
    }
    public static void init() throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new FileReader("src/first.txt"));
        String newline=bufferedReader.readLine();
        while(newline!=null)
        {
            String left=newline.split("::")[0];
            String[]right=newline.split("::")[1].split(" ");
            firsts.put(left,Arrays.asList(right));
            newline=bufferedReader.readLine();
        }
    }
    public Set<String> result(){  //calculate the first set of a sentential form
    Iterator<String> iterator=Sentential_form.iterator()  ;
    Set<String>result=new HashSet<>();
    while(iterator.hasNext())
    {
        String str=iterator.next();
        if(Productions_Builder.getTerminals().contains(str)) {  //terminal
            result.add(str);
            return result;
        }
        else  //non-terminal
        {
            if(firsts.get(str).contains("epsilon")) {//if X_i -> epsilon ,we need to check X_(i+1)
                result.addAll(firsts.get(str));
                result.remove("epsilon");
            }
            else{
                result.addAll(firsts.get(str));
                return result;
            }
        }
    }
    result.add("epsilon");  //all X_i contains epsilon
    return result;
    }
}
