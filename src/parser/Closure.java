package parser;

import lexer.Tag;

import java.util.*;

public class Closure {  //a set of items which contains some kernel items and other items that derived from the kernel items
    private final Set<Item> items=new HashSet<>();
    public Closure(Set<Item> items)
    {
        List<Production> all_productions=Productions_Builder.getAllProductions();
        Set<String> non_terminals=Productions_Builder.getNon_terminals();
        this.items.addAll(items);
        while(true) {
            Iterator<Item> current_items = this.items.iterator();
            Set<Item> added_items=new HashSet<>();
            while (current_items.hasNext()) {
                 Item this_item=current_items.next();
                 String after_dot_str="null";
                 if(this_item.getDot_position()<this_item.getProduction().getRight().size()) {
                      after_dot_str = this_item.getProduction().getRight().get(this_item.getDot_position());
                 }
                 if(non_terminals.contains(after_dot_str))  //this_item is [A->alpha.Bbeta,a]
                 {
                     for(Production production:all_productions)
                     {
                         if((production.getLeft()).equals(after_dot_str))//B->gamma
                         {
                             List<String> beta_a = new ArrayList<>();
                             if (this_item.getDot_position() + 1 <= this_item.getProduction().getRight().size() - 1) {//add beta
                                 beta_a.addAll(this_item.getProduction().getRight().subList(this_item.getDot_position() + 1,
                                         this_item.getProduction().getRight().size() ));
                             }
                             Set<String> newlook_aheads=new HashSet<>();
                             for(String look_ahead:this_item.getLook_aheads())
                             {
                             beta_a.add(look_ahead); //one possible beta_a
                             Set<String> first_set_of_beta_a = new First(beta_a).result();
                             newlook_aheads.addAll(first_set_of_beta_a);
                             if(beta_a.size()>=2)  //recover beta from beta_a
                             beta_a=beta_a.subList(0,beta_a.size()-1);
                             else
                                 beta_a.clear();
                             }
                             Item newItem=null;
                             if (production.getRight().contains("epsilon"))
                             newItem=new Item(production,1,newlook_aheads);
                             else
                             newItem=new Item(production,0,newlook_aheads);
                             if(!this.items.contains(newItem))
                             added_items.add(newItem);
                         }
                     }
                 }
            }
            if (!added_items.isEmpty())
            {
                this.items.addAll(added_items);
                added_items.clear();
            }
            else
              break;
        }
    }

    public Set<Item> getItems() {
        return items;
    }
}
