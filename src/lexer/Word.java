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
        if(this.getTag()==264||this.getTag()==257||this.getTag()==297)
        return lexeme+"\t\t<"+Tag.maps.get(this.getTag())+" , "+lexeme+">";
        else
        return lexeme+"\t\t<"+Tag.maps.get(this.getTag())+" , _"+">";
    }
    //Singleton words,for simplicity
    public static final Word
    and =new Word("&&",Tag.AND),
    or =new Word("||",Tag.OR),
    eq =new Word("==",Tag.EQ),
    ne =new Word("!=",Tag.NE),
    le =new Word("<=",Tag.LE),
    ge =new Word(">=",Tag.GE),
    inc=new Word("++",Tag.INC),
    dec=new Word("--",Tag.DEC),
    assign=new Word("=",Tag.ASSIGN),
    semi =new Word(";",Tag.SEMI),
    gr =new Word(">",Tag.GR),
    ls =new Word("<",Tag.LS),
    not =new Word("!",Tag.NOT),
    bit_and=new Word("&",Tag.BIT_AND),
    bit_or =new Word("|",Tag.BIT_OR),
    add =new Word("+",Tag.ADD),
    subtract=new Word("-",Tag.SUBTRACT),
    mult =new Word("*",Tag.MULT),
    divide=new Word("/",Tag.DIVIDE),
    slp =new Word("(",Tag.SLP),
    srp =new Word(")",Tag.SRP),
    mlp =new Word("[",Tag.MLP),
    mrp =new Word("]",Tag.MRP),
    lp =new Word("{",Tag.LP),
    rp =new Word("}",Tag.RP),
    comma= new Word(",",Tag.COMMA),
    minus=new Word("minus",Tag.MINUS),
    True =new Word("true",Tag.TRUE),
    False=new Word("false",Tag.FALSE),
    temp =new Word("t",Tag.TEMP),
    mod=new Word("%",Tag.MOD),
    add_assign=new Word("+=",Tag.ADD_ASSIGN),
    subtract_assign=new Word("-=",Tag.SUBTRACT_ASSIGN),
    mult_assign=new Word("*=",Tag.MULT_ASSIGN),
    divide_assign=new Word("/=",Tag.DIVIDE_ASSIGN),
    mod_assign=new Word("%=",Tag.MOD_ASSIGN);
}
