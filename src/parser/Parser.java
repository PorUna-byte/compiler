package parser;

import Inter.Execute_Marker;
import Inter.Execute_Synthetic;
import Inter.Share;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import symbols.Env;

import java.io.IOException;
import java.util.*;

public class Parser {
  private  Lexer lexer;
  private final Stack<Integer> States_stack;
  private final Stack<Node>    Nodes_stack;
  private final Map<Integer_Str,String> Action_Table;
  private final Map<Integer_Str,String>Goto_Table;
  private final List<Production> productions;
  private final Map<Integer,String> map;
  private final HashMap<Node,Integer> child_index=new HashMap<>();
  public Node root=null;
  public Parser() throws IOException {
      States_stack=new Stack<>();
      Nodes_stack=new Stack<>();
      LRTableGen lrTableGen=new LRTableGen();
      lrTableGen.Generate();
      productions=Productions_Builder.getAllProductions();
      map=lrTableGen.getMap();
      int initialState = lrTableGen.get_InitialState();
      Action_Table=lrTableGen.getAction_Table();
      Goto_Table=lrTableGen.getGoto_Table();
      States_stack.push(initialState);
  }
    public void parse(String file) throws IOException
  {
      lexer=new Lexer(file);
      Token token = lexer.scan();
      while (true) {
          int current_state=States_stack.peek();
          String lexeme;
          switch (token.getTag())
          {
              case Tag.NUM:
                  lexeme="num";
                  break;
              case Tag.REAL:
                  lexeme="real";
                  break;
              case Tag.ID:
                  lexeme="id";
                  break;
              default:
                  lexeme=((Word)token).getLexeme();
                  break;
          }
          String action=Action_Table.get(new Integer_Str(current_state,lexeme));
          if(action==null)
          {
              System.out.println("Syntax error at line:"+lexer.getLine());
              System.exit(-1);
              break;
          }
          if(action.startsWith("s"))
          {
              Integer Num=Integer.valueOf(action.substring(1));
              token.setLine(lexer.getLine());
              Nodes_stack.push(new Node(token));
              States_stack.push(Num);
              token = lexer.scan();
          }
          else if(action.startsWith("r"))//reduce A->beta
          {
              Integer production_index=Integer.valueOf(action.substring(1));
              List<Node> children=new ArrayList<>();
              List<String> right=productions.get(production_index).getRight();
              if(!right.contains("epsilon")) { //more specifically :A->epsilon or M->epsilon
                  for (int i = 0; i < right.size(); i++) {
                      States_stack.pop();
                      Node child = Nodes_stack.pop(); //collect children of a parent node
                      children.add(child);
                  }
              }
              else
              {
                  //A->epsilon or M(a marker)->epsilon
                  if(productions.get(production_index).getLeft().startsWith("M")) //A marker,some semantic action must be done
                  {
                     Execute_Marker _executeMarker =new Execute_Marker(Integer.valueOf(
                             productions.get(production_index).getLeft().substring(1)),this.Nodes_stack);
                     _executeMarker.Execute();
                  }
                  children.add(new Node(new Word("epsilon",Tag.EPSILON)));
              }
              int t=States_stack.peek();
              States_stack.push(Integer.valueOf(
                      Goto_Table.get(new Integer_Str(t,productions.get(production_index).getLeft()))));
              //The top is parent node's state,from which we can recover the parent node
              t=States_stack.peek();
              Word parent_word=new Word(map.get(t),t);
              parent_word.setLine(lexer.getLine());
              Node parent=new Node(parent_word);
              List<Node> reversed_children=new ArrayList<>();
              for(int i=children.size()-1;i>=0;i--)
                  reversed_children.add(children.get(i));
              parent.AddChildren(reversed_children);
              Nodes_stack.push(parent);//now the reduction from beta to A is done A->beta
//              we need to do some synthetic attributes evaluation if necessary
              Execute_Synthetic _executeSynthe =new Execute_Synthetic(
                      production_index,this.Nodes_stack);
              _executeSynthe.Execute();
          }
          else if(action.equals("accept")) {//Parsing successful!
              root=Nodes_stack.peek();
              System.out.println("Parsing successful!");
              break;
          }
      }
  }
  public void Print_Tree(){
      if(root==null)
      {
          System.out.println("The parsing is error.");
          return;
      }
      Stack<Node> traces=new Stack<>();
      traces.push(root);
      int depth=0;
      while(!traces.empty()){
          Node cur=traces.peek();
          while(has_child(cur))
          {
              if(get_child_index(cur)==0)
              System.out.println(String.join("", Collections.nCopies(depth,"  ")) +cur);
              if(cur.getChilds().size()>get_child_index(cur)) {
                  traces.push(cur.getChilds().get(get_child_index(cur)));
                  inc_child_index(cur);
                  cur = traces.peek();
                  depth++;
              }
              else{
                  break;
              }
          }
          if(get_child_index(cur)==0)
          System.out.println(String.join("", Collections.nCopies(depth,"  ")) +cur);
          depth--;
          traces.pop();
      }
  }
  private boolean has_child(Node node){
      return !node.getChilds().isEmpty();
  }
  private int get_child_index(Node node)
  {
      return child_index.get(node)==null?0:child_index.get(node);
  }
  private void inc_child_index(Node node)
  {
      child_index.put(node,get_child_index(node)+1);
  }
}
