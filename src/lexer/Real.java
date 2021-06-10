package lexer;

//Token for Real values
public class Real extends Token{
    private final float value;
    public Real(float value)
    {
        super(Tag.REAL);
        this.value=value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "real: "+value+" ("+this.getLine()+")";
    }
}
