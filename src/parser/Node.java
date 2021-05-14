package parser;

import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Token token;
    private final List<Node> childs=new ArrayList();
    public Node(Token token)
    {
        this.token=token;
    }

    public Token getToken() {
        return token;
    }

    public List<Node> getChilds() {
        return childs;
    }

    public void AddChildren(List<Node> children){
        this.childs.addAll(children);
    }

    @Override
    public java.lang.String toString() {
        return token.toString();
    }
}
