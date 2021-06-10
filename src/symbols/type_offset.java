package symbols;

public class type_offset {
    private Type type;
    private int offset;
    public type_offset(Type type,int offset)
    {
        this.type=type;
        this.offset=offset;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return type.toString()+" offset:"+offset;
    }
}
