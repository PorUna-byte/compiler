package symbols;

import lexer.Token;

import java.util.Hashtable;
//This class is an Environment which contains symbol table
public class Env {
    private final Hashtable<String,type_offset> symbol_table=new Hashtable<String,type_offset>();
    private final Env pre;//a link to previous environment
    private int offset=0;//initial offset is 0
    public Env(Env pre)
    {
        this.pre=pre;
    }
    public Hashtable getSymbol_table() {
        return symbol_table;
    }

    public Env getPre() {
        return pre;
    }

    public int getOffset() {
        return offset;
    }

    public void Put(String lexeme, Type type)
    {
          symbol_table.put(lexeme,new type_offset(type,this.offset));
          offset+=type.getWidth();
    }
    public type_offset cur_get(String lexeme)
    {
        return this.getSymbol_table().get(lexeme)==null?null:(type_offset) this.getSymbol_table().get(lexeme);
    }
    public type_offset get(String lexeme)
    {
        Env current_env=this;
        Object type=current_env.getSymbol_table().get(lexeme);
        while(type==null&&current_env.getPre()!=null)//while this lexeme is not found in this current environment
            //and the current environment has previous environment we search previous environment for this lexeme
        {
            current_env=current_env.getPre();
            type=current_env.getSymbol_table().get(lexeme);
        }
        if(type!=null)
            return (type_offset) type;
        else     //identifier is not defined before used
            return null;
    }
}
