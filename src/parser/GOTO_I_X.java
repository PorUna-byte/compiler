package parser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GOTO_I_X {
    private final Set<Item> I=new HashSet<>();
    private final String X;
    public GOTO_I_X(Set<Item> I,String X){
        this.I.addAll(I);
        this.X=X;
    }
    public Set<Item> result(){
        Set<Item> result=new HashSet<>();
        Iterator<Item> iterator=this.I.iterator();
        while(iterator.hasNext())
        {
            Item this_item=iterator.next();
            if(this_item.getDot_position()<this_item.getProduction().getRight().size()&&
            this_item.getProduction().getRight().get(this_item.getDot_position()).equals(this.X))
                //this_item is [A->alpha.Xbeta,a]
            {
                result.add(new Item(this_item.getProduction(),
                        this_item.getDot_position()+1,this_item.getLook_aheads()));
                //Added item is [A->alphaX.beta,a]
            }
        }
        if(!result.isEmpty())
        return new Closure(result).getItems();
        else return result;
    }
}
