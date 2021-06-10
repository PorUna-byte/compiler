package symbols;

import java.util.ArrayDeque;
import java.util.Queue;

public class Fun {
    private final Type retype;
    private final String fun_name;
    private final Queue<Type>parameters=new ArrayDeque<>();
    public Fun(Type retype,String fun_name,Queue<Type>parameters)
    {
        this.retype=retype;
        this.fun_name=fun_name;
        this.parameters.addAll(parameters);
    }

    public Type getRetype() {
        return retype;
    }

    public String getFun_name() {
        return fun_name;
    }

    public Queue<Type> getParameterstype() {
        return parameters;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        while(!parameters.isEmpty())
        {
            Type type=parameters.poll();
            stringBuilder.append(type.getLexeme()+",");
        }
        return retype.getLexeme() +" "+fun_name+"( "+stringBuilder.toString()+" )\n";
    }
}
