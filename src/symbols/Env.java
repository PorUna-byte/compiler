package symbols;

import lexer.Token;

import java.util.Hashtable;

public class Env {
    private final Hashtable hashtable=new Hashtable();
    private final Env pre;
    public Env(Env pre)
    {
        this.pre=pre;
    }
}
