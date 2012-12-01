/*
 *  The scanner definition for COOL.

 */

import java_cup.runtime.Symbol;

%%

%state COMMENT LINECOMMENT STRING BREAKOUT

<DIGITS>             =      [0-9]+
<WHITESPACENOTNL>    =      (" "|\f|\r|\t|\x0b)

<CLASS>         =       (c|C)(l|L)(a|A)(s|S)(s|S)
<ELSE>          =       (e|E)(l|L)(s|S)(e|E)
<FALSE>         =       (f)(a|A)(l|L)(s|S)(e|E)
<FI>            =       (f|F)(i|I)
<IF>            =       (i|I)(f|F)
<IN>            =       (i|I)(n|N)
<INHERITS>      =       (i|I)(n|N)(h|H)(e|E)(r|R)(i|I)(t|T)(s|S)
<ISVOID>        =       (i|I)(s|S)(v|V)(o|O)(i|I|)(d|D)
<LET>           =       (l|L)(e|E)(t|T)
<LOOP>          =       (l|L)(o|O)(o|O)(p|P)
<POOL>          =       (p|P)(o|O)(o|O)(l|L)
<THEN>          =       (t|T)(h|H)(e|E)(n|N)
<WHILE>         =       (w|W)(h|H)(i|I)(l|L)(e|E)
<CASE>          =       (c|C)(a|A)(s|S)(e|E)
<ESAC>          =       (e|E)(s|S)(a|A)(c|C)
<NEW>           =       (n|N)(e|E)(w|W)
<OF>            =       (o|O)(f|F)
<NOT>           =       (n|N)(o|O)(t|T)
<TRUE>          =       (t)(r|R)(u|U)(e|E)

<TYPEID>        =       [A-Z][a-zA-Z0-9_]*
<OBJECTID>      =       [a-z][a-zA-Z0-9_]*

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    static int commentDepth = 0;
    String errorMessage = new String();

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
    return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
    filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
    return filename;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */


%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
    /* nothing special to do in the initial state */
    break;
    case COMMENT:
        yybegin(YYINITIAL);
        errorMessage = new String("EOF in comment");
        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));
    case STRING:
        yybegin(YYINITIAL);
        errorMessage = new String("EOF in string");
        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%%

<YYINITIAL>     "=>"            {return new Symbol(TokenConstants.DARROW);}
<YYINITIAL>     "*"             {return new Symbol(TokenConstants.MULT);}
<YYINITIAL>     "("             {return new Symbol(TokenConstants.LPAREN);}
<YYINITIAL>     ";"             {return new Symbol(TokenConstants.SEMI);}
<YYINITIAL>     "-"             {return new Symbol(TokenConstants.MINUS);}
<YYINITIAL>     ")"             {return new Symbol(TokenConstants.RPAREN);}
<YYINITIAL>     ","             {return new Symbol(TokenConstants.COMMA);}
<YYINITIAL>     "/"             {return new Symbol(TokenConstants.DIV);}
<YYINITIAL>     "("             {return new Symbol(TokenConstants.LPAREN);}
<YYINITIAL>     "+"             {return new Symbol(TokenConstants.PLUS);}
<YYINITIAL>     "<-"            {return new Symbol(TokenConstants.ASSIGN);}
<YYINITIAL>     "."             {return new Symbol(TokenConstants.DOT);}
<YYINITIAL>     ":"             {return new Symbol(TokenConstants.COLON);}
<YYINITIAL>     "<"             {return new Symbol(TokenConstants.LT);}
<YYINITIAL>     "<="            {return new Symbol(TokenConstants.LE);}
<YYINITIAL>     "{"             {return new Symbol(TokenConstants.LBRACE);}
<YYINITIAL>     "}"             {return new Symbol(TokenConstants.RBRACE);}
<YYINITIAL>     "="             {return new Symbol(TokenConstants.EQ);}
<YYINITIAL>     "~"             {return new Symbol(TokenConstants.NEG);}
<YYINITIAL>     "@"             {return new Symbol(TokenConstants.AT);}
<YYINITIAL>     "*)"            {return new Symbol(TokenConstants.ERROR, new StringSymbol(new String("Unmatched *)"), 12, 0));}


<YYINITIAL>     {<CLASS>}       {return new Symbol(TokenConstants.CLASS);}
<YYINITIAL>     {<TRUE>}        {return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));}
<YYINITIAL>     {<FALSE>}       {return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false));}
<YYINITIAL>     {<ELSE>}        {return new Symbol(TokenConstants.ELSE);}
<YYINITIAL>     {<FI>}          {return new Symbol(TokenConstants.FI);}
<YYINITIAL>     {<IF>}          {return new Symbol(TokenConstants.IF);}
<YYINITIAL>     {<IN>}          {return new Symbol(TokenConstants.IN);}
<YYINITIAL>     {<INHERITS>}    {return new Symbol(TokenConstants.INHERITS);}
<YYINITIAL>     {<ISVOID>}      {return new Symbol(TokenConstants.ISVOID);}
<YYINITIAL>     {<LET>}         {return new Symbol(TokenConstants.LET);}
<YYINITIAL>     {<LOOP>}        {return new Symbol(TokenConstants.LOOP);}
<YYINITIAL>     {<POOL>}        {return new Symbol(TokenConstants.POOL);}
<YYINITIAL>     {<THEN>}        {return new Symbol(TokenConstants.THEN);}
<YYINITIAL>     {<WHILE>}       {return new Symbol(TokenConstants.WHILE);}
<YYINITIAL>     {<CASE>}        {return new Symbol(TokenConstants.CASE);}
<YYINITIAL>     {<ESAC>}        {return new Symbol(TokenConstants.ESAC);}
<YYINITIAL>     {<OF>}          {return new Symbol(TokenConstants.OF);}
<YYINITIAL>     {<NOT>}         {return new Symbol(TokenConstants.NOT);}
<YYINITIAL>     {<NEW>}         {return new Symbol(TokenConstants.NEW);}



<YYINITIAL>     \"              {yybegin(STRING); string_buf = new StringBuffer();}
<STRING>        \"              {yybegin(YYINITIAL);

                                    String currentString = string_buf.toString();
                                    if (string_buf.length() >= MAX_STR_CONST) {
                                        errorMessage = new String("String constant too long");
                                        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));}
                                    if (currentString.indexOf('\0') != -1) {
                                        errorMessage = new String("String contains null character");
                                        return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));}
                                    else {
                                        return new Symbol(TokenConstants.STR_CONST, new StringSymbol(currentString, currentString.length(), 0));}}
<STRING>        \n              {yybegin(YYINITIAL);
                                    curr_lineno++;
                                    errorMessage = new String("Unterminated string constant");
                                    return new Symbol(TokenConstants.ERROR, new StringSymbol(errorMessage, errorMessage.length(), 0));}


<STRING>        (\\)n             {string_buf.append('\n');}
<STRING>        (\\)(\n)          {string_buf.append('\n');}
<STRING>        (\\)b             {string_buf.append('\b');}
<STRING>        (\\)t             {string_buf.append('\t');}
<STRING>        (\\)f             {string_buf.append('\f');}


<STRING>        (\\).             {string_buf.append(yytext().substring(1));}
<STRING>        [^\n]                 {string_buf.append(yytext());}


<YYINITIAL>    "(*"            {yybegin(COMMENT);}
<COMMENT>      "(*"            {commentDepth++;}
<COMMENT>      "*)"            {if(commentDepth > 0) {commentDepth--;} else {yybegin(YYINITIAL);}}
<COMMENT>      \n              {curr_lineno++;}
<COMMENT>      [^\n]               {;}


<YYINITIAL>    "--"            {yybegin(LINECOMMENT);}
<LINECOMMENT>   .              {;}
<LINECOMMENT>   \n             {yybegin(YYINITIAL);curr_lineno++;}

<YYINITIAL>     {<CLASS>}      {return new Symbol(TokenConstants.CLASS);}

<YYINITIAL>     {<DIGITS>}     {return new Symbol(TokenConstants.INT_CONST, new StringSymbol(yytext(), yytext().length(), 0));}
<YYINITIAL>     {<TYPEID>}     {return new Symbol(TokenConstants.TYPEID, new StringSymbol(yytext(), yytext().length(), 0));}
<YYINITIAL>     {<OBJECTID>}   {return new Symbol(TokenConstants.OBJECTID, new StringSymbol(yytext(), yytext().length(), 0));}







<YYINITIAL>      \n            {curr_lineno++;}
<YYINITIAL>     {<WHITESPACENOTNL>}  {;}







                .               {return new Symbol(TokenConstants.ERROR, new StringSymbol(yytext(), yytext().length(), 0));}
