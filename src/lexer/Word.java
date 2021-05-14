package lexer;

//token for all words such as identifiers , keywords and operators with two or more characters etc...
public class Word extends Token{
    public String getLexeme() {
        return lexeme;
    }
    private  final String lexeme;
    public Word(String lexeme,int tag)
    {
        super(tag);
        this.lexeme=lexeme;
    }
    public String toString(){
        if(this.getTag()== Tag.ID)
        return "id :"+lexeme+" ("+this.getLine()+")";
        else
        return  lexeme+" ("+this.getLine()+")";
    }
    //Singleton words,for simplicity
    public static final Word
    and =new Word("&&",Tag.AND),
    or =new Word("||",Tag.OR),
    eq =new Word("==",Tag.EQ),
    ne =new Word("!=",Tag.NE),
    le =new Word("<=",Tag.LE),
    ge =new Word(">=",Tag.GE),
    True =new Word("true",Tag.TRUE),
    False=new Word("false",Tag.FALSE);
}
