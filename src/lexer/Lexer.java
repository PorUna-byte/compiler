package lexer;

import symbols.Type;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
   public static int line =1; //record the line number
   private char peek=' '; //the current character
   private final BufferedReader bufferedReader;
   private  final File file;
   Hashtable<String,Word>words=new Hashtable();

   public  int getLine() {
        return line;
    }

    void reserve(Word w)
   {
       words.put(w.getLexeme(),w);
   }
   //reserve the keywords first before we begin to scan
   public Lexer(String filename)throws IOException{
       reserve(new Word("if",Tag.IF));
       reserve(new Word("else",Tag.ELSE));
       reserve(new Word("while",Tag.WHILE));
       reserve(new Word("do",Tag.DO));
       reserve(new Word("break",Tag.BREAK));
       reserve(new Word("return",Tag.RETURN));
       reserve(new Word("struct",Tag.STRUCT));
       reserve(new Word("void",Tag.VOID));
       reserve(new Word("for",Tag.FOR));
       reserve(new Word("switch",Tag.SWITCH));
       reserve(Word.True);
       reserve(Word.False);
       reserve(Type.Int);
       reserve(Type.Char);
       reserve(Type.Bool);
       reserve(Type.Float);
       reserve(Type.VOID);
       bufferedReader=new BufferedReader(new FileReader(filename));
       file=new File(filename);
   }
   //read a character from a file
   void read_ch() throws IOException
   {
       int IS_end=0; // mark the end of the file
       IS_end=bufferedReader.read();
       if(IS_end==-1)
       {
          peek='$';
       }
       else {
           peek = (char) IS_end;
           if(peek=='\n') line++;
       }
   }
   //read a character from a file and judge if it is character c
   boolean read_ch(char c)throws IOException
   {
       read_ch();
       if(peek!=c)
           return false;
       peek=' ';
       return true;
   }
   void eliminate_annotation()throws IOException{
            bufferedReader.mark((int)file.length()+1);
            read_ch();
            if(peek=='/')
            {
                while(!read_ch('\n')) ;
                read_ch();
                return;
            }
            else if(peek=='*')
            {
               while(true) {
                   if(!read_ch('*'))
                       continue;
                   if (read_ch('/')) {
                       read_ch();
                       return;
                   }
               }
            }
            else
            {
                peek='/';
                bufferedReader.reset();
            }
   }
   public Token scan() throws IOException
   {
           //skip all blank space and annotations
           for (; ; read_ch()) {
               if (peek == '/')
                   eliminate_annotation();
               if (peek == ' ' || peek == '\t'||peek=='\n'||peek=='\r')
                   continue;
               else break;
           }
      switch (peek)  //deal with operators and delimiters
      {
          case '&':
              if(read_ch('&')) { peek=' ';return Word.and;} else return new Word('&'+"",'&');
          case '|':
              if(read_ch('|')) {peek=' ' ;return Word.or;} else return new Word('|'+"",'|');
          case '=':
              if(read_ch('=')) {peek=' ' ;return Word.eq;} else return new Word('='+"",'=');
          case '!':
              if(read_ch('=')) {peek=' ';return Word.ne;} else return new Word('!'+"",'!');
          case '<':
              if(read_ch('=')) {peek=' ';return Word.le;} else return new Word('<'+"",'<');
          case '>':
              if(read_ch('=')) {peek=' ';return Word.ge;} else return new Word('>'+"",'>');
      }
       if(Character.isDigit(peek))
       {
           if(peek=='0')
           {
               read_ch();
               if(peek=='X'||peek=='x') //deal with hexadecimal such as 0x10f or 0Xea
               {
                   read_ch();
                   int v=0;
                   do{
                       v=16*v+Character.digit(peek,16);
                       read_ch();
                   }while(Character.isDigit(peek));
                   return new Num(v);
               }
               else if(Character.isDigit(peek)){         //deal with octal such as 0372 or 0112
                   int v=0;
                   do{
                       v=8*v+Character.digit(peek,8);
                       read_ch();
                   }while(Character.isDigit(peek));
                   return new Num(v);
               }
               else   //deal with single 0
               {
                   return new Num(0);
               }
           }
           int v=0;
           do{    //deal with decimal number
               v=10*v+Character.digit(peek,10);
               read_ch();
           }while(Character.isDigit(peek));
           if(peek!='.') return new Num(v);  //deal with real
           float x=v;
           float d=10;
           while(true) {
               read_ch();
               if (!Character.isDigit(peek)) break;
               x += Character.digit(peek, 10) / d;
               d *= 10;
           }
           if(peek=='E'||peek=='e')  //deal with real in scientific notation
           {
               read_ch();
                if(peek=='-')
               {
                   read_ch();
                   float tail = 0;
                   do {
                       tail = 10 * tail + Character.digit(peek, 10);
                       read_ch();
                   } while (Character.isDigit(peek));
                   return new Real((float)(x*Math.pow(10,-tail)));
               }
               else{
                   if(peek=='+')
                   read_ch();
                   float tail = 0;
                   do {
                       tail = 10 * tail + Character.digit(peek, 10);
                       read_ch();
                   } while (Character.isDigit(peek));
                   return new Real((float)(x*Math.pow(10,tail)));
               }
           }
           return new Real(x);
       }
       if(Character.isLetter(peek)||peek=='_')  //deal with identifiers or keywords
       {
           StringBuffer b=new StringBuffer();
           do{
               b.append(peek);
               read_ch();
           }while(Character.isLetterOrDigit(peek)||peek=='_');
           String s=b.toString();
           Word w=words.get(s);
           if(w!=null) return w;
           w=new Word(s,Tag.ID);
           words.put(s,w);
           return w;
       }
       Token tok=new Word(peek+"",peek);
       peek=' ';
       return tok;
   }
}
