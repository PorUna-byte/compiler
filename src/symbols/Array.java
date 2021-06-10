package symbols;

import lexer.Tag;

public class Array extends Type{
    private final Type of;
    private int size=1;
    public Array(int sz,Type p)
    {
        super("[]", Tag.INDEX,sz*p.getWidth());
        this.size=sz;
        this.of=p;
    }

    public Type getOf() {
        return of;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
//        Type subtype=of;
//        while(subtype.getLexeme().equals("[]")) //subtype is also an array
//        {
//            subtype=((Array)subtype).getOf();
//        }
//        //now subtype is not an array
//        String row_type= subtype.getLexeme();
//        StringBuilder stringBuilder=new StringBuilder();
//        subtype=this;
//        stringBuilder.append(row_type);
//        while(subtype.getLexeme()=="[]")
//        {
//            stringBuilder.append("["+((Array)subtype).size+"]");
//            subtype=((Array) subtype).of;
//        }
//        return stringBuilder.toString()+"\t\t"+"width:"+this.getWidth();
        return "new Array("+size+","+of.toString()+","+"width:"+getWidth()+")";
       }
}
