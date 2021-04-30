package lexer;

//Token for Real values
public class Real extends Token{
    private final float value;
    public Real(float value)
    {
        super(Tag.REAL);
        this.value=value;
    }

    @Override
    public String toString() {
        return value+"\t\t<"+Tag.maps.get(this.getTag())+" , "+value+">";
    }
}
