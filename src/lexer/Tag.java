package lexer;

import java.util.HashMap;

/*
Tags for words
 */
public class Tag {
    public final static int
    AND=256 , BASIC=257 , BREAK=258 , DO=259 , ELSE=260 ,
     EQ=261 , FALSE=262 , GE=263 , ID=264 ,IF=265 ,
    INDEX=266 , LE=267 , MINUS=268 ,NE= 269 ,NUM=270 ,
    OR=271 , REAL=272, TEMP=273, TRUE=274 ,WHILE=275,
    FOR=276,INC=277,DEC=278,SEMI=279,GR=280,
     ASSIGN=281, LS=282,NOT=283,BIT_AND=284,BIT_OR=285,
     ADD=286,SUBTRACT=287,MULT=288,DIVIDE=289,SLP=290,
    SRP=291,MLP=292,MRP=293,LP=294,RP=295,
    COMMA=296,STRING=297,MOD=298,ADD_ASSIGN=299,SUBTRACT_ASSIGN=300,
            MULT_ASSIGN=301,DIVIDE_ASSIGN=302,MOD_ASSIGN=303;
    ;
    public final static HashMap<Integer,String>
    maps=new HashMap<>();
}
