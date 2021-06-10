package Inter;

import lexer.*;
import parser.Node;
import symbols.*;

import java.util.List;
import java.util.Stack;
//Note that this class is very similar to Execute_Marker
public class Execute_Synthetic {
    private int Production_index; //determine which action to take
    private final Stack<Node>    Nodes_stack;
    public Execute_Synthetic(int Production_index, Stack<Node>Nodes_stack)
    {
        this.Nodes_stack=Nodes_stack;
        this.Production_index=Production_index;
    }
    private Type check(String lexeme)
    {
        if(Share.current_env.get(lexeme)==null) {
            System.out.println("semantic error at line " + Lexer.line + ":" + lexeme + " is not defined.");
            System.exit(1);
        }
        return Share.current_env.get(lexeme).getType();
    }
    private Type match(Type type1,Type type2)
    {
        Type type=Type.max(type1,type2);  //activate type coercion mechanism
//        if(type1!=type2)
//            type=null;
        if(type==null)
        {
            System.out.println("semantic error at line " + Lexer.line + ":" + type1.getLexeme()+" does not match "+type2.getLexeme());
            System.exit(1);
        }
        return type;
    }
    public void Execute(){
        switch (Production_index)
        {
            case 3://{fun=new Fun(basic1.type,id.lexeme,Share.parameters_deftype);
                //funList.add(fun);Save(env);env=env.pre;stmts.next=new label();
                //check(basic2.type match basic1.type);
                // program.code=gen(id.lexeme':')||stmts.code||stmts.next;}
                Node parent3=Nodes_stack.peek();
                Fun fun=new Fun(parent3.getChilds().get(0).type,
                        ((Word)parent3.getChilds().get(1).getToken()).getLexeme(),Share.parameters_deftype);
                Share.funList.add(fun);Share.saved_env.add(Share.current_env);
                Share.current_env=Share.current_env.getPre();
                parent3.getChilds().get(8).next=Share.newLabel();
                if(parent3.getChilds().get(10).type!=parent3.getChilds().get(0).type)
                {
                    System.out.println("semantic error at line " + Lexer.line + ":function "+((Word)parent3.getChilds().get(1).getToken()).getLexeme()+"'s return type" +
                            " does not match defined type");
                    System.exit(1);
                }
                break;
            case 6://{Save(env);env=env.pre;stmts.next=new label();block.code=stmts.code||stmts.next;}
                Node parent6=Nodes_stack.peek();
                Share.saved_env.add(Share.current_env);
                Share.current_env=Share.current_env.getPre();
                parent6.getChilds().get(3).next=Share.newLabel();
                break;
            case 9:// {type.type=column.type;}
                Nodes_stack.peek().type=Nodes_stack.peek().getChilds().get(2).type;
                break;
            case 10://{type.type=new Struct(env,depth);env=envStack.pop();}
                Nodes_stack.peek().type=new Struct(Share.current_env,Share.envStack.size());
                Share.current_env=Share.envStack.pop();
                break;
            case 11://{basic.type=int;}
                Nodes_stack.peek().type= Type.Int;
                break;
            case 12://{basic.type=float;}
                Nodes_stack.peek().type= Type.Float;
                break;
            case 13://{basic.type=void;}
                Nodes_stack.peek().type= Type.VOID;
                break;
            case 14://{column.type=temp;}
                Nodes_stack.peek().type=Share.temp;
                break;
            case 15://{column.type=new Array(num.value,column1.type);}
                List<Node> childs=Nodes_stack.peek().getChilds();
                Nodes_stack.peek().type=new Array(((Num)(childs.get(1).getToken())).getValue(),childs.get(3).type);
                break;
            case 16://{stmt1.next=new label();stmts2.next=stmts1.next;stmts1.code=stmt1.code||stmt1.next||stmts2.code;}
                Node parent16=Nodes_stack.peek();
                parent16.getChilds().get(0).next=Share.newLabel();
                parent16.getChilds().get(1).next=parent16.next;
                break;
            case 18://{ check:id.type=env.get(id.lexeme);match(id.type,expr.type);stmt.code=expr.code||gen(id.lexeme'='expr.addr);}
                Node parent18=Nodes_stack.peek();
                String lexeme18=((Word)parent18.getChilds().get(0).getToken()).getLexeme();
                parent18.getChilds().get(0).type=check(lexeme18);
                match(parent18.getChilds().get(0).type,parent18.getChilds().get(2).type);
                break;
            case 19://{match(left_array.subtype,expr.type);stmt.code=left_array.code||expr.code||
               // gen(left_array.base'['left_array.addr']''='expr.addr);}
                Node parent19=Nodes_stack.peek();
                Node child0=parent19.getChilds().get(0);
                match(child0.subtype,parent19.getChilds().get(2).type);
                break;
            case 20://{check:id.type=(struct)left_str.type.env.get(id.lexeme);match(id.type,expr.type);
                //stmt.code=left_str.code||expr.code||gen(left_str.addr'.'id.lexeme'='expr.addr);}
                Node stmt = Nodes_stack.peek();
                Node left_str = stmt.getChilds().get(0);
                Node id = stmt.getChilds().get(1);
                Node expr = stmt.getChilds().get(3);
                String lexeme20 = ((Word) id.getToken()).getLexeme();
                if ((id.type=((Struct) left_str.type).getEnv().get(lexeme20).getType()) == null) {
                    System.out.println("semantic error at line " + Lexer.line + ":" +
                            lexeme20 + "is not defined in this struct");
                    System.exit(1);
                }
                match(id.type,expr.type);
                break;
            case 21:
            case 22://expr.addr=new Temp();expr.type=match(expr1.type,term.type);
                Node expr21=Nodes_stack.peek();
                expr21.addr=Share.newTemp();
                expr21.type=match(expr21.getChilds().get(0).type,expr21.getChilds().get(2).type);
                break;
            case 23://{check:id.type=env.get(id.lexeme) factor.addr=id.lexeme;factor.type=id.type;}
                Nodes_stack.peek().getChilds().get(0).type=check(((Word)Nodes_stack.peek().getChilds().get(0).getToken()).getLexeme());
                Nodes_stack.peek().addr=((Word)Nodes_stack.peek().getChilds().get(0).getToken()).getLexeme();
                Nodes_stack.peek().type=Nodes_stack.peek().getChilds().get(0).type;
                break;
            case 24://{expr.addr=new Temp();expr.code=left_array.code||
                    //gen(expr.addr'='left_array.base'['left_array.addr']');expr.type=left_array.subtype;}
                Node expr24=Nodes_stack.peek();
                Node left_array=expr24.getChilds().get(0);
                expr24.addr=Share.newTemp();
                expr24.type=left_array.subtype;
                break;
            case 25://{check:id.type=(struct)left_str.type.env.get(id.lexeme);expr.type=id.type;
                //expr.addr=new Temp();expr.code=left_str.code||gen(expr.addr'='left_str.addr'.'id.lexeme);}
                Node expr25=Nodes_stack.peek();
                Node left_str25=expr25.getChilds().get(0);
                String lexeme25=((Word)expr25.getChilds().get(1).getToken()).getLexeme();
                if((expr25.getChilds().get(1).type=((Struct)left_str25.type).getEnv().get(lexeme25).getType())==null)
                {
                    System.out.println("semantic error at line " + Lexer.line + ":" + lexeme25 + "is not defined.");
                    System.exit(1);
                }
                expr25.type=expr25.getChilds().get(1).type;
                expr25.addr=Share.newTemp();
                break;
            case 26://{factor.addr=num.value;factor.type=Type.Int;}
                Nodes_stack.peek().type=Type.Int;
                Nodes_stack.peek().addr=((Num)Nodes_stack.peek().getChilds().get(0).getToken()).getValue()+"";
                break;
            case 27://{factor.addr=real.value;factor.type=Type.Float;}
                Nodes_stack.peek().type=Type.Float;
                Nodes_stack.peek().addr=((Real)Nodes_stack.peek().getChilds().get(0).getToken()).getValue()+"";
                break;
            case 28:// {check:id.type=env.get(id.lexeme) is struct;left_str.type=id.type;left_str.addr=id.lexeme}
                Node left_str28=Nodes_stack.peek();
                String lexeme28=((Word)left_str28.getChilds().get(0).getToken()).getLexeme();
                left_str28.type=left_str28.getChilds().get(0).type=check(lexeme28);
                if(!left_str28.type.getLexeme().equals("struct_def"))
                {
                    System.out.println("semantic error at line " + Lexer.line + ":" +"dot(.) can not be applied to non-struct variable");
                    System.exit(1);
                }
                left_str28.addr=lexeme28;
                break;
            case 29://{new_env=(struct)left_str1.type.env;check:id.type=new_env.get(id.lexeme);
                //left_str.type=new_env.get(id.lexeme);left_str.addr=new Temp();
                //left_str.code=left_str1.code||gen(left_str.addr'='left_str1.addr'.'id.lexeme);}
                Node left_str29=Nodes_stack.peek();
                Env new_env=((Struct)left_str29.getChilds().get(0).type).getEnv();
                String lexeme29=((Word)left_str29.getChilds().get(1).getToken()).getLexeme();
                if((left_str29.getChilds().get(1).type=new_env.get(lexeme29).getType())==null)
                {
                    System.out.println("semantic error at line " + Lexer.line + ":" + lexeme29 + "is not defined.");
                    System.exit(1);
                }
                left_str29.type=new_env.get(lexeme29).getType();
                if(!left_str29.type.getLexeme().equals("struct_def"))
                {
                    System.out.println("semantic error at line " + Lexer.line + ":" +"dot(.) can not be applied to non-struct variable");
                    System.exit(1);
                }
                left_str29.addr=Share.newTemp();
                break;
            case 30://{id.type=check(id.lexeme) is array;check(expr.type is int);left_array.type=env.get(id.lexeme);
                //left_array.base=id.lexeme;left_array.subtype=(Array)left_array.type.of;
                //left_array.addr=new Temp();left_array.code=expr.code||
                // gen(left_array.addr'='expr.addr'*'left_array.subtype.width)}
                Node left_array30=Nodes_stack.peek();
                String lexeme30=((Word)left_array30.getChilds().get(0).getToken()).getLexeme();
                Node expr30=left_array30.getChilds().get(2);
                left_array30.getChilds().get(0).type=check(lexeme30);
                if(expr30.type!=Type.Int)
                {
                    System.out.println("semantic error at line " + Lexer.line + ":" +"index in array should be integer.");
                    System.exit(1);
                }
                left_array30.type=Share.current_env.get(lexeme30).getType();
                if (!left_array30.type.getLexeme().equals("[]")) {
                    System.out.println("semantic error at line " + Lexer.line + ":" +"[] should be applied to array.");
                    System.exit(1);
                }
                left_array30.base=lexeme30;
                left_array30.subtype=((Array)left_array30.type).getOf();
                left_array30.addr=Share.newTemp();
                break;
            case 31://{left_array.type=left_array1.type;check(expr.type is int);left_array.subtype=(Array)left_array1.subtype.of
    // left_array.base=left_array1.base;t=new Temp();left_array.addr=new Temp();left_array.code=left_array1.code||expr.code||
                  //gen(t'='expr.addr'*'left_array.subtype.width)||gen(left_array.addr'='left_array1.addr'+'t);}
               Node left_array31=Nodes_stack.peek();
               Node expr31=left_array31.getChilds().get(2);
                if(expr31.type!=Type.Int)
                {
                    System.out.println("semantic error at line " + Lexer.line + ":" +"index in array should be integer.");
                    System.exit(1);
                }
               left_array31.type=left_array31.getChilds().get(0).type;
                if(!((left_array31.getChilds().get(0).subtype) instanceof Array))
                {
                    System.out.println("semantic error at line " + Lexer.line + ":" +"[] should be applied to array.");
                    System.exit(1);
                }
               left_array31.subtype=((Array)left_array31.getChilds().get(0).subtype).getOf();
               left_array31.base=left_array31.getChilds().get(0).base;
               left_array31.addr=Share.newTemp();
               break;
            case 32://{bool.true=fall;bool.false=stmt1.next=stmt.next;stmt.code=bool.code||stmt1.code;}
                Node stmt_32=Nodes_stack.peek();
                Node bool_32=stmt_32.getChilds().get(2);
                Node stmt1_32=stmt_32.getChilds().get(4);
                bool_32.True="fall";
                bool_32.False=stmt1_32.next=stmt_32.next;
                break;
            case 33://{bool.true=fall;bool.false=new label();
                //stmt1.next=stmt2.next=stmt.next;stmt.code=bool.code||stmt1.code||
                // gen('goto' stmt.next)||bool.false||stmt2.code;}
                Node stmt_33=Nodes_stack.peek();
                Node bool_33=stmt_33.getChilds().get(2);
                Node stmt1_33=stmt_33.getChilds().get(4);
                Node stmt2_33=stmt_33.getChilds().get(6);
                bool_33.True="fall";
                bool_33.False=Share.newLabel();
                stmt1_33.next= stmt2_33.next=stmt_33.next;
                break;
            case 34://{begin=new label();bool.true=fall;bool.false=stmt.next;stmt1.next=begin;
                //stmt.code=begin||bool.code||stmt1.code||gen('goto' begin);}
                String begin=Share.newLabel();
                Node stmt_34=Nodes_stack.peek();
                Node bool_34=stmt_34.getChilds().get(2);
                Node stmt1_34=stmt_34.getChilds().get(4);
                bool_34.True="fall";
                bool_34.False= stmt_34.next;
                stmt1_34.next=begin;
                break;
            case 35://{check:funList().contains(id.lexeme);n=0;check:function's required parameters' type match provided parameters.
               // while(!queue.empty) {n++;p=queue.dequeue();stmt.code=stmt.code||gen('param' p);} stmt.code=stmt.code||
                 //   gen('call' id.lexeme ',' n);}
                 Node stmt_35=Nodes_stack.peek();
                 String lexeme_35=((Word)stmt_35.getChilds().get(0).getToken()).getLexeme();
                 boolean found=false;
                 Fun found_fun=null;
                 for(Fun cur_fun:Share.funList)
                 {
                     if (cur_fun.getFun_name().equals(lexeme_35)) {
                         found = true;
                         found_fun=cur_fun;
                         break;
                     }
                 }
                 if(!found)
                 {
                     System.out.println("semantic error at line " + Lexer.line + ":function " +lexeme_35+" not found");
                     System.exit(1);
                 }
                 if(found_fun.getParameterstype().size()!=Share.parameters_calltype.size())
                 {
                     System.out.println("semantic error at line " + Lexer.line + ":function " +lexeme_35+" parameters number does not match");
                     System.exit(1);
                 }
                 for(Type type:found_fun.getParameterstype())
                 {
                     Type call_type=Share.parameters_calltype.poll();
                     if(call_type!=type)
                     {
                         System.out.println("semantic error at line " + Lexer.line + ":function " +lexeme_35+" parameters' type does not match");
                         System.exit(1);
                     }
                 }
                break;
            case 37://Share.parameters_calladdr.enqueue(expr.addr);Share.parameters_calltype.enqueue(expr.type)
                 Node expr_list37=Nodes_stack.peek();
                 Node expr_37=expr_list37.getChilds().get(1);
                 Share.parameters_calladdr.add(expr_37.addr);
                 Share.parameters_calltype.add(expr_37.type);
                break;
            case 38:// {Share.parameters_calladdr=new queue();Share.parameters_calltype=new queue();}
                 Share.parameters_calltype.clear();
                 Share.parameters_calladdr.clear();
                break;
            case 39://{bool1.true=bool.true!=fall?bool.true:new label();bool1.false=fall;bool2.true=bool.true;
                //bool2.false=bool.false;bool.code=bool.true!=fall?bool1.code||bool2.code:bool1.code||bool2.code||bool1.true;}
                Node bool_39=Nodes_stack.peek();
                Node bool1_39=bool_39.getChilds().get(0);
                Node bool2_39=bool_39.getChilds().get(2);
                bool1_39.True=!bool_39.True.equals("fall")?bool_39.True:Share.newLabel();
                bool1_39.False="fall";
                bool2_39.True= bool_39.True;
                bool2_39.False=bool_39.False;
                break;
            case 40://{bool1.false=bool.false!=fall?bool.false:new label();bool1.true=fall;bool2.true=bool.true;
                //bool2.false=bool.false;bool.code=bool.false!=fall?bool1.code||bool2.code:bool1.code||bool2.code||bool1.false;}
                Node bool_40=Nodes_stack.peek();
                Node bool1_40=bool_40.getChilds().get(0);
                Node bool2_40=bool_40.getChilds().get(2);
                bool1_40.False=!bool_40.False.equals("fall")?bool_40.False:Share.newLabel();
                bool1_40.True="fall";
                bool2_40.True=bool_40.True;
                bool2_40.False=bool_40.False;
                break;
            case 41://{bool1.true=bool.false;bool1.false=bool.true;bool.code=bool1.code;}
                Node bool_41=Nodes_stack.peek();
                Node bool1_41=bool_41.getChilds().get(1);
                bool1_41.True=bool_41.False;
                bool1_41.False=bool_41.True;
                break;
            case 45://{rel.addr="<"}
                Nodes_stack.peek().addr="<";
                break;
            case 46://{rel.addr="<="}
                Nodes_stack.peek().addr="<=";
                break;
            case 47://{rel.addr=">"}
                Nodes_stack.peek().addr=">";
                break;
            case 48://{rel.addr=">="}
                Nodes_stack.peek().addr=">=";
                break;
            case 49://{rel.addr="=="}
                Nodes_stack.peek().addr="==";
                break;
            case 50://{rel.addr="!="}
                Nodes_stack.peek().addr="!=";
                break;
            case 56://{type.type=star.type;}
                Node type_56=Nodes_stack.peek();
                Node star_56=type_56.getChilds().get(2);
                type_56.type=star_56.type;
                break;
            case 57://{star.type=new Star(star1.type);}
                Node star_57=Nodes_stack.peek();
                Node star1_57=star_57.getChilds().get(1);
                star_57.type=new Star(star1_57.type);
                break;
            case 58://{star.type=temp;}
                Nodes_stack.peek().type=Share.temp;
                break;
            case 59://  {begin=new label();bool.false=fall;bool.true=begin;
                //stmt.code=begin||stmt1.code||bool.code ;}
                String begin_59=Share.newLabel();
                Node bool_59=Nodes_stack.peek().getChilds().get(4);
                bool_59.False="fall";
                bool_59.True=begin_59;
                break;
            case 64://{expr.type=term.type;expr.addr=term.addr;expr.code=term.code;}
            case 67://{term.addr=factor.addr;term.type=factor.type;term.code=factor.code;}
                Node expr_64=Nodes_stack.peek();
                Node term_64=expr_64.getChilds().get(0);
                expr_64.type=term_64.type;
                expr_64.addr= term_64.addr;
                break;
            case 65://term.addr=new Temp();term.type=match(term1.type,factor.type);
                Node term_65=Nodes_stack.peek();
                Node term1_65=term_65.getChilds().get(0);
                Node factor_65=term_65.getChilds().get(2);
                term_65.addr=Share.newTemp();
                term_65.type=match(term1_65.type,factor_65.type);
                break;
            case 68://{factor.type=expr.type;factor.addr=expr.addr;factor.code=expr.code;}
                Node factor_68=Nodes_stack.peek();
                Node expr_68=factor_68.getChilds().get(1);
                factor_68.type=expr_68.type;
                factor_68.addr= expr_68.addr;
                break;
    }
    }
}
