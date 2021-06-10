package Inter;

import lexer.Lexer;
import lexer.Tag;
import lexer.Word;
import parser.Node;
import parser.Production;
import parser.Productions_Builder;


import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class Gen_code {
    private final Node parser_tree_root;
    public Gen_code(Node parser_tree_root)
    {
        this.parser_tree_root=parser_tree_root;
    }
    private String gen(String code)
    {
        if(code.equals("")||code.startsWith("L")||code.endsWith("\n"))
            return code;
        return code+"\n";
    }
    private int match_a_production(Node parent)
    {
        List<Node> children=parent.getChilds();
        List<Production> productions= Productions_Builder.getAllProductions();
        if(!(parent.getToken() instanceof Word))//if current node is a leaf then it has no label to transfer
            return 0;
        for(int j=0;j<children.size();j++) {
            //check this node and its children match one specific production,from SDT pattern we know that production which needs
            //to transfer labels has all its children in type Word.
            if (!(children.get(j).getToken() instanceof Word)) //no need to transfer label or collect code.
                return 0;
        }
        int production_index=0;
        String lexeme="";
        for(int i=0;i<productions.size();i++)
        {
            Production production=productions.get(i);
            if(production.getLeft().equals(((Word)parent.getToken()).getLexeme())&&production.getRight().size()==children.size())
            {
                int j=0;
                for(;j<children.size();j++)
                {
                    if(children.get(j).getToken().getTag()== Tag.ID)
                        lexeme="id";
                    else
                        lexeme=((Word)children.get(j).getToken()).getLexeme();
                    if(!production.getRight().get(j).equals(lexeme))
                        break;
                }
                if(j==children.size()) {//this production match the node and its children
                    production_index = i;
                    break;
                }
            }
        }
        return production_index;
    }
    private void transfer_label_action(Node parent){ //this method does a label transfer from a node to its children
        int production_index=match_a_production(parent);
        List<Node> children=parent.getChilds();
        switch (production_index)//note that ,if no production matched, then production_index is 0 ,and we do nothing at 0 by chance
        {
            case 16://stmt2.next=stmt.next in stmt->stmt1 stmt2
                children.get(1).next=parent.next;
                break;
            case 32://bool.false=stmt1.next=stmt.next in stmt->if ( bool )  stmt1
                parent.getChilds().get(2).False=parent.getChilds().get(4).next=parent.next;
                break;
            case 33://stmt1.next=stmt2.next=stmt.next in  stmt->if ( bool ) stmt1 else stmt2
                parent.getChilds().get(4).next=parent.getChilds().get(6).next=parent.next;
                break;
            case 34://Enclosing_next=bool.false=stmt.next in stmt->while ( bool ) stmt1
                parent.getChilds().get(2).False=parent.next;
                Share.Enclosing_next.push(parent.next);
                break;
            case 39://bool1.true=bool.true!=fall?bool.true:new label();bool2.true=bool.true;
               // bool2.false=bool.false in bool->bool1 || bool2
               parent.getChilds().get(0).True=!parent.True.equals("fall")? parent.True:Share.newLabel();
               parent.getChilds().get(2).True=parent.True;
               parent.getChilds().get(2).False=parent.False;
               break;
            case 40://bool1.false=bool.false!=fall?bool.false:new label();bool2.true=bool.true;
                //bool2.false=bool.false in  bool->bool1 && bool2
                parent.getChilds().get(0).False=!parent.False.equals("fall")? parent.False:Share.newLabel();
                parent.getChilds().get(2).True=parent.True;
                parent.getChilds().get(2).False=parent.False;
                break;
            case 41://bool1.true=bool.false;bool1.false=bool.true in bool->! bool1
                parent.getChilds().get(1).True=parent.False;
                parent.getChilds().get(1).False=parent.True;
                break;
            case 59:// Enclosing_next.push(stmt.next);
                Share.Enclosing_next.push(parent.next);
                break;
            case 62://{begin=new begin();bool.true=fall;bool.false=stmt.next;stmt1.next=begin;}
                String begin_62=Share.newLabel();
                children.get(3).True="fall";
                children.get(3).False=parent.next;
                children.get(7).next=begin_62;
                Share.Enclosing_next.push(parent.next);
                break;
        }
    }
    public void Transferlabel(){ //this function does a top-down transfer of labels
        Queue<Node> nodeQueue=new ArrayDeque<>();
        Node cur_node=parser_tree_root;
        nodeQueue.add(cur_node);
        while(!nodeQueue.isEmpty())
        {
             cur_node=nodeQueue.poll();
             transfer_label_action(cur_node);
             nodeQueue.addAll(cur_node.getChilds());
        }
   }
   private void gen_code_action(Node parent){//this method does a code generation for a node (from its children's code)
       int production_index=match_a_production(parent);
       List<Node> children=parent.getChilds();
       switch (production_index)
       {
           case 1://programs->program programs1 {programs.code=program.code||programs1.code}
               parent.code=gen(children.get(0).code)+gen(children.get(1).code);
               break;
           case 3://program.code=gen(id.lexeme':')||stmts.code;
               parent.code=gen(((Word)children.get(1).getToken()).getLexeme()+":")+
               gen(children.get(8).code);
               break;
           case 6://block.code=stmts.code;
               parent.code=gen(children.get(3).code);
               break;
           case 16://{stmts1.code=stmt1.code||stmts2.code;}
               parent.code=gen(children.get(0).code)+gen(children.get(1).code);
               break;
           case 18://stmt.code=expr.code||
               //if(expr.type!=id.type)
                //   gen(id.lexeme'=('+id.type')'+expr.addr);
               //else gen(id.lexeme'='+expr.addr);
               parent.code=gen(children.get(2).code)+(
               children.get(2).type!=children.get(0).type?
                gen(((Word)children.get(0).getToken()).getLexeme()+"=("+children.get(0).type.getLexeme()+
                        ")"+children.get(2).addr):
                gen(((Word)children.get(0).getToken()).getLexeme()+"="+children.get(2).addr));
               break;
           case 19://stmt.code=left_array.code||expr.code||
               //gen(left_array.base'['left_array.addr']''='expr.addr);
               parent.code=gen(children.get(0).code)+gen(children.get(2).code)+gen(children.get(0).base+"["+
                       children.get(0).addr+"]="+children.get(2).addr);
               break;
           case 20://stmt.code=left_str.code||expr.code||gen(left_str.addr'.'id.lexeme'='expr.addr);
               parent.code=gen(children.get(0).code)+gen(children.get(3).code)+gen(children.get(0).addr+"."+
                       ((Word)children.get(1).getToken()).getLexeme()+"="+children.get(3).addr);
               break;
           case 21://expr.code=expr1.code||term.code||
               //if (expr1.type!=expr.type)
               //gen(expr.addr+'=('+expr.type+')'+expr1.addr'+'term.addr);
               //else if(term.type!=expr.type)
               //gen(expr.addr'='expr1.addr'+('+expr.type+')'+term.addr);
               //else gen(expr.addr'='expr1.addr'+'term.addr);
               String str;
               parent.code=gen(children.get(0).code)+gen(children.get(2).code);
               if(children.get(0).type!=parent.type)
               str=gen(parent.addr+"=("+parent.type.getLexeme()+")"+children.get(0).addr+"+"+children.get(2).addr);
               else if(children.get(2).type!=parent.type)
               str=gen(parent.addr+"="+children.get(0).addr+"+("+parent.type.getLexeme()+")"+children.get(2).addr);
               else
               str=gen(parent.addr+"="+children.get(0).addr+"+"+children.get(2).addr);
               parent.code=parent.code+str;
               break;
           case 22://expr.code=expr1.code||term.code||
               //if (expr1.type!=expr.type)
               //gen(expr.addr+'=('+expr.type+')'+expr1.addr'-'term.addr);
               //else if(term.type!=expr.type)
               //gen(expr.addr'='expr1.addr'-('+expr.type+')'+term.addr);
               //else gen(expr.addr'='expr1.addr'-'term.addr);
               String str1;
               parent.code=gen(children.get(0).code)+gen(children.get(2).code);
               if(children.get(0).type!=parent.type)
                   str1=gen(parent.addr+"=("+parent.type.getLexeme()+")"+children.get(0).addr+"-"+children.get(2).addr);
               else if(children.get(2).type!=parent.type)
                   str1=gen(parent.addr+"="+children.get(0).addr+"-("+parent.type.getLexeme()+")"+children.get(2).addr);
               else
                   str1=gen(parent.addr+"="+children.get(0).addr+"-"+children.get(2).addr);
               parent.code=parent.code+str1;
               break;
           case 24://expr.code=left_array.code||
               //gen(expr.addr'='left_array.base'['left_array.addr']');expr.type=left_array.subtype;
               parent.code=gen(children.get(0).code)+gen(parent.addr+"="+children.get(0).base+"["+children.get(0).addr+
                       "]");
               break;
           case 25://expr.code=left_str.code||gen(expr.addr'='left_str.addr'.'id.lexeme)
           case 29://left_str.code=left_str1.code||gen(left_str.addr'='left_str1.addr'.'id.lexeme);
               parent.code=gen(children.get(0).code)+gen(parent.addr+"="+children.get(0).addr+"."+
                      ((Word)children.get(1).getToken()).getLexeme());
              break;
           case 30://left_array.code=expr.code||gen(left_array.addr'='expr.addr'*'left_array.subtype.width)
              parent.code=gen(children.get(2).code)+gen(parent.addr+"="+children.get(2).addr+"*"+
                      parent.subtype.getWidth());
              break;
           case 31://left_array.code=left_array1.code||expr.code||
               //gen(t'='expr.addr'*'left_array.subtype.width)||gen(left_array.addr'='left_array1.addr'+'t);
               String t=Share.newTemp();
              parent.code=children.get(0).code+gen(children.get(2).code)+gen(t+"="+children.get(2).addr+"*"+
                      parent.subtype.getWidth())+gen(parent.addr+"="+children.get(0).addr+"+"+t);
              break;
           case 32://stmt.code=bool.code||stmt1.code||stmt.next;
              parent.code=gen(children.get(2).code)+gen(children.get(4).code)+gen(parent.next);
              break;
           case 33://stmt.code=bool.code||stmt1.code||gen('goto' stmt.next)||bool.false||stmt2.code||stmt.next;
              parent.code=gen(children.get(2).code)+gen(children.get(4).code)+gen("goto "+
                      parent.next.substring(0,parent.next.length()-1))+
              gen(children.get(2).False)+gen(children.get(6).code)+gen(parent.next);
              break;
           case 34://stmt.code=begin||bool.code||stmt1.code||gen('goto' begin)||stmt.next;
              parent.code=gen(children.get(4).next)+gen(children.get(2).code)+gen(children.get(4).code)+gen("goto "+
                      children.get(4).next.substring(0,children.get(4).next.length()-1))+gen(parent.next);
              break;
           case 35://while(!queue.empty) {n=queue.size();p=queue.dequeue();stmt.code=stmt.code||gen('param' p);}
               // stmt.code=stmt.code||gen('call' id.lexeme ',' n);}
               int n=Share.parameters_calladdr.size();
               while(!Share.parameters_calladdr.isEmpty())
               {
                   String p=Share.parameters_calladdr.poll();
                   parent.code=gen(parent.code)+gen("param "+p);
               }
               parent.code=parent.code+gen("call "+((Word)children.get(0).getToken()).getLexeme()+" , "+n);
               break;
           case 36://{stmt.code=block.code;}
              parent.code=gen(children.get(0).code);
              break;
           case 39://bool.code=bool.true!=fall?bool1.code||bool2.code:bool1.code||bool2.code||bool1.true;
              parent.code=!parent.True.equals("fall")?gen(children.get(0).code)+gen(children.get(2).code):
              gen(children.get(0).code)+gen(children.get(2).code)+gen(children.get(0).True);
              break;
           case 40://bool.code=bool.false!=fall?bool1.code||bool2.code:bool1.code||bool2.code||bool1.false;
              parent.code=!parent.False.equals("fall")?gen(children.get(0).code)+gen(children.get(2).code):
              gen(children.get(0).code)+gen(children.get(2).code)+gen(children.get(0).False);
              break;
           case 41://bool.code=bool1.code;
              parent.code=gen(children.get(1).code);
              break;
           case 42://test=expr1.addr rel.addr expr2.addr
               //if bool.true!=fall and bool.false!=fall :
               //bool.code=gen('if' test 'goto' bool.true)||gen('goto' bool.false);
                   //else if bool.true!=fall bool.code=gen('if' test 'goto' bool.true);
                  //else if bool.false!=fall bool.code=gen('ifFalse' test 'goto' bool.false);
               String test=children.get(0).addr+" "+children.get(1).addr+" "+children.get(2).addr;
               String common=gen(children.get(0).code)+gen(children.get(2).code);
               if(!parent.True.equals("fall")&&!parent.False.equals("fall"))
               parent.code=gen("if "+test+" goto "+parent.True.substring(0,parent.True.length()-1))
                       +gen(" goto "+parent.False.substring(0,parent.False.length()-1));
               else if(!parent.True.equals("fall"))
               parent.code=gen("if " +test+" goto "+parent.True.substring(0,parent.True.length()-1));
               else if(!parent.False.equals("fall"))
               parent.code=gen("ifFalse "+test+" goto "+parent.False.substring(0,parent.False.length()-1));
               parent.code=common+parent.code;
               break;
           case 43://{bool.code=gen('goto' bool.true);}
               parent.code=gen("goto "+parent.True.substring(0,parent.True.length()-1));
               break;
           case 44://{bool.code=gen('goto' bool.false);}
               parent.code=gen("goto "+parent.False.substring(0,parent.False.length()-1));
               break;
           case 59://{stmt.code=begin||stmt1.code||bool.code||stmt.next ;}
               parent.code=gen(children.get(4).True)+gen(children.get(1).code)+gen(children.get(4).code)+gen(parent.next);
               break;
           case 62://stmt.code=stmt_semi1.code||stmt1.next||bool.code||stmt1.code||stmt_semi2.code
               // ||gen('goto' stmt1.next)||stmt.next;
               parent.code=gen(children.get(2).code)+gen(children.get(7).next)+gen(children.get(3).code)+
               gen(children.get(7).code)+gen(children.get(5).code)+gen("goto "+children.get(7).next.substring(
                      0,children.get(7).next.length()-1
               ))+gen(parent.next);
               break;
           case 60://stmt_semi->stmt{stmt_semi.code=stmt.code;}
               parent.code=gen(children.get(0).code);
               break;
           case 63://{check:Enclosing.next!="" ;stmt.code=gen("goto" Enclosing.next);}
               if(Share.Enclosing_next.isEmpty())
               {
                   System.out.println("semantic error at line " + Lexer.line + ":" +"break statement should be in a loop" +
                           "or unreachable break");
                   System.exit(1);
               }
               String str_63=Share.Enclosing_next.pop();
               parent.code=gen("goto "+str_63.substring(0,str_63.length()-1));
               break;
           case 64:
           case 67:
               parent.code=children.get(0).code;
               break;
           case 65://term.code=term1.code||factor.code||
              // if (term1.type!=term.type)
              //gen(term.addr'=('+term.type+')'+term1.addr'*'factor.addr);
              //else if(factor.type!=term.type)
              // gen(term.addr'='term1.addr'*('+term.type+')'+factor.addr);
              //else gen(expr.addr'='expr1.addr'*'term.addr)
               String str_65;
               parent.code=gen(children.get(0).code)+gen(children.get(2).code);
               if(children.get(0).type!=parent.type)
                   str_65=gen(parent.addr+"=("+parent.type.getLexeme()+")"+children.get(0).addr+"*"+children.get(2).addr);
               else if(children.get(2).type!=parent.type)
                   str_65=gen(parent.addr+"="+children.get(0).addr+"*("+parent.type.getLexeme()+")"+children.get(2).addr);
               else
                   str_65=gen(parent.addr+"="+children.get(0).addr+"*"+children.get(2).addr);
               parent.code=parent.code+str_65;
               break;
           case 66:
               String str_66;
               parent.code=gen(children.get(0).code)+gen(children.get(2).code);
               if(children.get(0).type!=parent.type)
                   str_66=gen(parent.addr+"=("+parent.type.getLexeme()+")"+children.get(0).addr+"/"+children.get(2).addr);
               else if(children.get(2).type!=parent.type)
                   str_66=gen(parent.addr+"="+children.get(0).addr+"/("+parent.type.getLexeme()+")"+children.get(2).addr);
               else
                   str_66=gen(parent.addr+"="+children.get(0).addr+"/"+children.get(2).addr);
               parent.code=parent.code+str_66;
               break;
           case 68:
               parent.code=children.get(1).code;
               break;

       }
   }
   public void gen_code(Node cur){//traverse the parser tree in post order.
        for(Node child:cur.getChilds())
        {
            gen_code(child);
        }
        gen_code_action(cur);
   }
}
