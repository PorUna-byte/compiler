package symbols;


import lexer.Tag;

public class Star extends Type{
    private final Type of;
    public Star(Type of){
        super("star", Tag.STAR,8);//for 64-bit machine the width of pointer occupies 8 bytes.
        this.of=of;
    }
    public Type getOf() {
        return of;
    }
    @Override
    public String toString() {
//        Type subtype=of;
//        while(subtype.getLexeme().equals("star")) //subtype is also an pointer
//        {
//            subtype=((Star)subtype).getOf();
//        }
//        //now subtype is not an pointer
//        String row_type= subtype.getLexeme();
//        StringBuilder stringBuilder=new StringBuilder();
//        subtype=this;
//        stringBuilder.append(row_type);
//        while(subtype.getLexeme().equals("star"))
//        {
//            stringBuilder.append("*");
//            subtype=((Star) subtype).of;
//        }
//        return stringBuilder.toString()+"\t\t"+"width:"+this.getWidth();
        return "new Pointer("+of.toString()+","+8+")";
    }
}
