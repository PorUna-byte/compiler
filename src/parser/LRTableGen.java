package parser;

import java.io.IOException;
import java.util.*;

public class LRTableGen {
      private final Map<Interger_Str,String>Action_Table=new HashMap<>();
      private final Map<Interger_Str,String>Goto_Table=new HashMap<>();
      private final List<Set<Item>>States=new ArrayList<>();
      private final HashMap<Integer,String>map=new HashMap<>();
      public LRTableGen() throws IOException {
          Productions_Builder.init();
          States.addAll(Collection_of_sets.get_Collectios_of_sets());
      }

    public HashMap<Integer, String> getMap() {
        return map;
    }

    public Map<Interger_Str, String> getAction_Table() {
        return Action_Table;
    }

    public Map<Interger_Str, String> getGoto_Table() {
        return Goto_Table;
    }
    public int get_InitialState(){
          Item inital_item=new Item(Productions_Builder.getAllProductions().get(0),0,Set.of("$"));
          for(int i=0;i<States.size();i++)
          {
              if(States.get(i).contains(inital_item))
                  return i;
          }
          return -1;
    }
    public void Generate(){
          List<Production> productions=Productions_Builder.getAllProductions();
          for(Set<Item> state:States) //I_i
          {
              for(Item item:state)
              {
                  if(item.getDot_position()<item.getProduction().getRight().size())//A->alpha . a beta  , b
                  {
                      String symbol=item.getProduction().getRight().get(item.getDot_position());
                      Interger_Str interger_str=new Interger_Str(States.indexOf(state),symbol);//(i,a) or (i,A)
                      if(interger_str==new Interger_Str(121,"if"))
                          ;
                      Set<Item> I_j=new GOTO_I_X(state,symbol).result();//Goto(I_i,a)=I_j
                      map.put(States.indexOf(I_j),symbol);
                      if(Productions_Builder.getTerminals().contains
                              (symbol))
                      {
                          Action_Table.put(interger_str,"s"+States.indexOf(I_j));
                      }
                      else
                      {
                          Goto_Table.put(interger_str,States.indexOf(I_j)+"");
                      }
                  }
                  else {
                      Set<String> lookaheads=item.getLook_aheads();
                      if(productions.indexOf(item.getProduction())==0)
                      {
                          Interger_Str interger_str=new Interger_Str(States.indexOf(state),"$");//(i,$)
                          Action_Table.put(interger_str,"accept");
                      }
                      else
                      {
                          for(String look_ahead:lookaheads)
                          {
                              Interger_Str interger_str=new Interger_Str(States.indexOf(state),look_ahead);//(i,look_ahead)
                              Action_Table.put(interger_str,"r"+productions.indexOf(item.getProduction()));
                          }
                      }
                  }
              }
          }
      }

    @Override
    public String toString() {
       StringBuilder show_table=new StringBuilder();
       show_table.append("Action table:\n");
       for(int i=0;i<States.size();i++)
       {
           show_table.append(i+":  ");
           for(Interger_Str interger_str:Action_Table.keySet())
           {
               if(interger_str.integer==i)
               {
                   show_table.append("("+interger_str.string+","+Action_Table.get(interger_str)+")  ");
               }
           }
           show_table.append("\n");
       }
        show_table.append("Goto table:\n");
        for(int i=0;i<States.size();i++)
        {
            show_table.append(i+":  ");
            for(Interger_Str interger_str:Goto_Table.keySet())
            {
                if(interger_str.integer==i)
                {
                    show_table.append("("+interger_str.string+","+Goto_Table.get(interger_str)+")  ");
                }
            }
            show_table.append("\n");
        }
        return show_table.toString();
    }
}
