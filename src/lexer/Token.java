package lexer;

//A general class for all tokens
public class Token {
    private final int tag;

    public int getTag() {
        return tag;
    }

    public Token(int tag){
        this.tag=tag;
    }
    public String toString(){
        return ""+(char)tag;
    }
}
