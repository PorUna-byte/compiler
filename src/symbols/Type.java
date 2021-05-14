package symbols;

import lexer.Tag;
import lexer.Word;

public class Type extends Word {
    private final int width;

    public int getWidth() {
        return width;
    }

    public Type(String lexeme , int tag , int width)
    {
        super(lexeme,tag);
        this.width=width;
    }

    public static boolean numeric(Type P)
    {
        if((P==Type.Char) || (P==Type.Float) || (P==Type.Int))
            return true;
        else
            return false;
    }
    //The function is useful for type conversions.
    public static Type max(Type P1, Type P2)
    {
        if(!numeric(P1)||!numeric(P2))
            return null;
        else if(P1==Type.Float||P2==Type.Float)
            return Type.Float;
        else if(P1==Type.Int||P2==Type.Int)
            return Type.Int;
        else
            return Type.Char;
    }
    //singleton basic types,for simplicity
    public static final Type
    Int = new Type("int", Tag.BASIC,4),
    Float = new Type("float",Tag.BASIC,8),
    Char = new Type("char",Tag.BASIC,1),
    Bool = new Type("bool" ,Tag.BASIC,1);
    @Override
    public String toString() {
        return this.getLexeme();
    }
}
