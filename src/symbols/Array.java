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

    @Override
    public String toString() {
        return "["+size+"]"+of.toString();
    }
}
