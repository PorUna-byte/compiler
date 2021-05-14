package parser;

import lexer.Tag;

import java.util.List;
import java.util.Objects;
import java.util.Set;


public class Item {
    private final Production production;
    private final int dot_position;
    private final Set<String> look_aheads;
    public Item(Production production, int dot_position, Set<String> look_ahead){
        this.production=production;
        this.dot_position=dot_position;
        this.look_aheads=look_ahead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return dot_position == item.dot_position &&
                look_aheads .equals( item.look_aheads )&&
                production.equals(item.production);
    }

    @Override
    public String toString() {
        StringBuilder show_right=new StringBuilder();
        boolean dot=false;
        for(int i=0;i<=production.getRight().size();i++)
        {
            if(i==this.getDot_position()&&!dot)
            {
                i--;
                show_right.append(".");
                dot=true;
            }
            else if(i<production.getRight().size())
                show_right.append(production.getRight().get(i)+" ");

        }
        StringBuilder look_aheads=new StringBuilder();
        for(String look_ahead: this.look_aheads) {
            look_aheads.append(look_ahead);
            look_aheads.append("/");
        }
        String look_aheads_str=look_aheads.toString();
        look_aheads_str=look_aheads_str.substring(0,look_aheads_str.length()-1);
        return production.getLeft()+"->"+show_right.toString()+"  "+look_aheads_str;
    }

    @Override
    public int hashCode() {
        return Objects.hash(production, dot_position, look_aheads);
    }

    public Production getProduction() {
        return production;
    }

    public int getDot_position() {
        return dot_position;
    }

    public Set<String> getLook_aheads() {
        return look_aheads;
    }
}
