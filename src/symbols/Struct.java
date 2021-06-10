package symbols;

import Inter.Share;
import lexer.Tag;

import java.util.Collections;
public class Struct extends Type{
    private final Env env;
    private final int depth;
    public Struct(Env env,int depth){
        super("struct_def", Tag.STRUCT_DEF,env.getOffset());
        this.depth=depth;
        this.env=env;
    }

    public Env getEnv() {
        return env;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        String padding=String.join("", Collections.nCopies(depth,"\t"));
            stringBuilder.append("type:struct\t\twidth:"+this.getWidth()+"\n"+
                    padding+"Elements in struct:\n");
            for (Object lexeme:env.getSymbol_table().keySet()) {
                stringBuilder.append(padding+"lexeme:" + lexeme + "\t" + env.getSymbol_table().get(lexeme)+"\n");
            }
            stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
        return stringBuilder.toString();
    }
}
