package lexer;

import symbols.Type;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
   private int line =1; //record the line number
   private char peek=' '; //the current character
   private final BufferedReader bufferedReader;
   private  final File file;
   Hashtable<String,Word>words=new Hashtable();

    public int getLine() {
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
       reserve(new Word("for",Tag.FOR));
       reserve(Word.True);
       reserve(Word.False);
       reserve(Type.Int);
       reserve(Type.Char);
       reserve(Type.Bool);
       reserve(Type.Float);
       bufferedReader=new BufferedReader(new FileReader(filename));
       file=new File(filename);
       Tag.maps.put(256,"AND");
       Tag.maps.put(257,"BASIC");
       Tag.maps.put(258,"BREAK");
       Tag.maps.put(259,"DO");
       Tag.maps.put(260,"ELSE");
       Tag.maps.put(261,"EQ");
       Tag.maps.put(262,"FALSE");
       Tag.maps.put(263,"GE");
       Tag.maps.put(264,"ID");
       Tag.maps.put(265,"IF");
       Tag.maps.put(266,"INDEX");
       Tag.maps.put(267,"LE");
       Tag.maps.put(268,"MINUS");
       Tag.maps.put(269,"NE");
       Tag.maps.put(270,"NUM");
       Tag.maps.put(271,"OR");
       Tag.maps.put(272,"REAL");
       Tag.maps.put(273,"TEMP");
       Tag.maps.put(274,"TRUE");
       Tag.maps.put(275,"WHILE");
       Tag.maps.put(276,"FOR");
       Tag.maps.put(277,"INC");
       Tag.maps.put(278,"DEC");
       Tag.maps.put(279,"SEMI");
       Tag.maps.put(280,"GR");
       Tag.maps.put(281,"ASSIGN");
       Tag.maps.put(282,"LS");
       Tag.maps.put(283,"NOT");
       Tag.maps.put(284,"BIT_AND");
       Tag.maps.put(285,"BIT_OR");
       Tag.maps.put(286,"ADD");
       Tag.maps.put(287,"SUBTRACT");
       Tag.maps.put(288,"MULT");
       Tag.maps.put(289,"DIVIDE");
       Tag.maps.put(290,"SLP");
       Tag.maps.put(291,"SRP");
       Tag.maps.put(292,"MLP");
       Tag.maps.put(293,"MRP");
       Tag.maps.put(294,"LP");
       Tag.maps.put(295,"RP");
       Tag.maps.put(296,"COMMA");
       Tag.maps.put(297,"STRING");
       Tag.maps.put(298,"MOD");
       Tag.maps.put(299,"ADD_ASSIGN");
       Tag.maps.put(300,"SUBTRACT_ASSIGN");
       Tag.maps.put(301,"MULT_ASSIGN");
       Tag.maps.put(302,"DIVIDE_ASSIGN");
       Tag.maps.put(303,"MOD_ASSIGN");
   }
   //read a character from a file
   void read_ch() throws IOException
   {
       int IS_end=0; // mark the end of the file
       IS_end=bufferedReader.read();
       if(IS_end==-1)
       {
           System.out.println("The file has finished.");
           System.out.println("The file has "+line+" lines");
           if(test.errors.isEmpty())
               System.out.println("No lexical errors found");
           else
           {
               for(int i=0;i<test.errors.size();i++)
               System.out.println(test.errors.get(i));
           }
           System.exit(0);
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
               if (peek == ' ' || peek == '\t'||peek=='\n')
                   continue;
               else break;
           }
      switch (peek)  //deal with operators and delimiters
      {
          case '&':
              if(read_ch('&')) { peek=' ';return Word.and;} else return Word.bit_and;
          case '|':
              if(read_ch('|')) {peek=' ' ;return Word.or;} else return Word.bit_or;
          case '=':
              if(read_ch('=')) {peek=' ' ;return Word.eq;} else return Word.assign;
          case '!':
              if(read_ch('=')) {peek=' ';return Word.ne;} else return Word.not;
          case '<':
              if(read_ch('=')) {peek=' ';return Word.le;} else return Word.ls;
          case '>':
              if(read_ch('=')) {peek=' ';return Word.ge;} else return Word.gr;
          case '+': {
              read_ch();
              if (peek=='+') {peek=' ';return Word.inc;}
              else if (peek=='=') {peek=' ';return Word.add_assign;}
              else return Word.add;
          }
          case '-':
              {
              read_ch();
              if (peek=='-') {peek=' ';return Word.dec;}
              else if (peek=='=') {peek=' ';return Word.subtract_assign;}
              else return Word.subtract;
          }
          case '%':
              if(read_ch('=')) {peek=' ';return Word.mod_assign;}else {return Word.mod;}
          case '*':
              if(read_ch('=')) {peek=' ';return Word.mult_assign;}else {return Word.mult;}
          case '/':
              if(read_ch('=')) {peek=' ';return Word.divide_assign;}else {return Word.divide;}
          case ';': peek=' ';return Word.semi;//assign peek as ' ' so the next time the peek will be skipped
          case '(': peek=' ';return  Word.slp;
          case ')': peek=' ';return Word.srp;
          case '[': peek=' ';return Word.mlp;
          case ']': peek=' ';return Word.mrp;
          case '{': peek=' ';return Word.lp;
          case '}': peek=' ';return Word.rp;
          case ',': peek=' ';return  Word.comma;
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
       if(peek=='"')  //deal with string constant
       {
           StringBuilder stringBuilder=new StringBuilder();
           do{
               read_ch();
               if(peek=='\\')
               {
                   read_ch();
                   if(peek=='"')
                       stringBuilder.append('"');
                   else{
                       stringBuilder.append('\\');
                       stringBuilder.append(peek);
                   }
                   peek=' ';
               }
               else if(peek!='"'){
                   stringBuilder.append(peek);
               }
           }while(peek!='"');
           peek=' ';
           return new Word(stringBuilder.toString(),Tag.STRING);
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
       Token tok=new Token(peek);
       peek=' ';
       return tok;
   }
}
