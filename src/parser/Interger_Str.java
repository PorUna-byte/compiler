package parser;

import java.util.Objects;

public class Interger_Str {
    public Integer integer;
    public String  string;
    public Interger_Str(Integer integer,String string){
        this.integer=integer;
        this.string=string;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interger_Str that = (Interger_Str) o;
        return integer.equals(that.integer) &&
                string.equals(that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(integer, string);
    }
}
