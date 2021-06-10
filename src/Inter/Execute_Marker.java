package Inter;

import lexer.Lexer;
import lexer.Word;
import parser.Node;
import symbols.Env;

import java.util.Stack;

public class Execute_Marker {
    private int Mark_Num; //determine which action to take
    private final Stack<Node>    Nodes_stack;
    private void check(String lexeme)
    {
        if(Share.current_env.cur_get(lexeme)!=null)
        {
            System.out.println("semantic error at line " + Lexer.line + ":" + lexeme + " is duplicated.");
            System.exit(1);
        }
    }
    public Execute_Marker(int mark_Num, Stack<Node>Nodes_stack)
    {
        this.Nodes_stack=Nodes_stack;
        this.Mark_Num=mark_Num;
    }
    public void Execute(){
        switch (this.Mark_Num)
        {
            case 0://{env=new Env(env);}
                Share.current_env=new Env(Share.current_env);
                break;
            case 1://{Share.parameters_deftype.enqueue(type.type);check(id.lexeme);env.put(id.lexeme,type.type)}
                Share.parameters_deftype.add( Nodes_stack.get(Nodes_stack.size()-3).type);
                check(((Word)Nodes_stack.get(Nodes_stack.size()-2).getToken()).getLexeme());
                Share.current_env.Put(((Word)Nodes_stack.get(Nodes_stack.size()-2).getToken()).getLexeme(),
                        Nodes_stack.get(Nodes_stack.size()-3).type);
                break;
            case 2://{check(id.lexeme);env.put(id.lexeme,type.type);}
                check(((Word)Nodes_stack.get(Nodes_stack.size()-2).getToken()).getLexeme());
                Share.current_env.Put(((Word)Nodes_stack.get(Nodes_stack.size()-2).getToken()).getLexeme(),
                        Nodes_stack.get(Nodes_stack.size()-3).type);
                break;
            case 3://{temp=basic.type;}
                Share.temp=Nodes_stack.peek().type;
                break;
            case 4://{envStack.push(env);env=new Env(null);}
                Share.envStack.push(Share.current_env);
                Share.current_env=new Env(null);
                break;
        }
    }

}
