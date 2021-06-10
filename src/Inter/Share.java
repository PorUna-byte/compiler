package Inter;

import symbols.Env;
import symbols.Fun;
import symbols.Type;

import java.util.*;

public class Share {
     public static Stack<Env> envStack=new Stack<>();
     public static List<Fun>  funList=new ArrayList<>();
     public static Env current_env=null;//action may alter current environment, Two Executors share the same env
     public static Type temp;  //Two action share the same temporary type t
     public static Queue<Type> parameters_deftype =new ArrayDeque<>();
     public static Queue<String> parameters_calladdr=new ArrayDeque<>();
     public static Queue<Type> parameters_calltype=new ArrayDeque<>();
     public static List<Env> saved_env=new ArrayList<>();//old env that will not be used again.
     public static int t=0;
     public static int label=0;
     public static Stack<String> Enclosing_next=new Stack<>();
     public static String newTemp(){return "t"+(++Share.t);}
     public static String newLabel(){return "L"+(++Share.label)+":";}
}
