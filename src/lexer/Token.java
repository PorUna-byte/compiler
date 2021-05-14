package lexer;

//A general class for all tokens
public class Token {
    private final int tag;
    private  int line=0;
    public int getTag() {
        return tag;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Token(int tag){
        this.tag=tag;
    }
    public String toString(){
        return (char)tag+" ("+line+")";
    }
}
