package parser;

import lexer.Tag;

import java.util.*;

public class Production {
    private final String Left;
    private final List<String> Right;
    public Production(String Left,List<String> right){
        this.Left=Left;
        this.Right=right;
    }

    public String getLeft() {
        return Left;
    }

    public List<String> getRight() {
        return Right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Left.equals(that.Left) &&
                Right.equals(that.Right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Left, Right);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        for(String str:this.getRight()) {
            stringBuilder.append(str);
            stringBuilder.append(" ");
        }
        return this.getLeft()+"->"+stringBuilder.toString();
    }
}
