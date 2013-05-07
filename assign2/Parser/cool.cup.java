/*
 *  cool.cup
 *              Parser definition for the COOL language.
 *
 */

import java_cup.runtime.*;

/* Stuff enclosed in {: :} is copied verbatim to the class containing
all parser actions.  All the extra variables/functions you want to use
in the semantic actions should go here.  Don't remove or modify anything
that was there initially.  */

action code {: 

    int curr_lineno() {
	return ((CoolTokenLexer)parser.getScanner()).curr_lineno();
    }

    AbstractSymbol curr_filename() {
	return ((CoolTokenLexer)parser.getScanner()).curr_filename();
    }
:} 

/************************************************************************/
/*                DONT CHANGE ANYTHING IN THIS SECTION                  */

parser code {:
    int omerrs = 0;

    public void syntax_error(Symbol cur_token) {
        int lineno = action_obj.curr_lineno();
	String filename = action_obj.curr_filename().getString();
        System.err.print("\"" + filename + "\", line " + lineno + 
		         ": parse error at or near ");
        Utilities.printToken(cur_token);
	omerrs++;
	if (omerrs>50) {
	   System.err.println("More than 50 errors");
	   System.exit(1);
	}
    }

    public void unrecovered_syntax_error(Symbol cur_token) {
    }
:}

/* Declare the terminals; a few have types for associated lexemes.  The
token ERROR is never used in the parser; thus, it is a parse error when
the lexer returns it.  */

terminal CLASS, ELSE, FI, IF, IN, INHERITS, LET, LET_STMT, LOOP, POOL, THEN, WHILE;
terminal CASE, ESAC, OF, DARROW, NEW, ISVOID;
terminal ASSIGN, NOT, LE, ERROR;
terminal PLUS, DIV, MINUS, MULT, EQ, LT, DOT, NEG, COMMA, SEMI, COLON;
terminal LPAREN, RPAREN, AT, LBRACE, RBRACE;
terminal AbstractSymbol STR_CONST, INT_CONST;
terminal Boolean BOOL_CONST;
terminal AbstractSymbol TYPEID, OBJECTID;

/*  DON'T CHANGE ANYTHING ABOVE THIS LINE, OR YOUR PARSER WONT WORK       */
/**************************************************************************/

   /* Complete the nonterminal list below, giving a type for the semantic
      value of each non terminal. (See the CUP documentation for details. */

nonterminal programc program;
nonterminal Classes class_list;
nonterminal class_c class;
nonterminal Features dummy_feature_list;
nonterminal Feature feature;
nonterminal Formals dummy_formal_list;
nonterminal formalc formal;
nonterminal Expression expression, semi_expression, let_declaration_expression;
nonterminal Expressions semi_expressions, comma_expressions; // two types of expression lists
nonterminal Case branch; // since branch is the only "useful" case; also avoids 'case', a Java keyword
nonterminal Cases case_list;

/* Precedence declarations go here. */

precedence right ASSIGN;
precedence right NOT;
precedence nonassoc LT, EQ;
precedence left MINUS, PLUS;
precedence left DIV, MULT;
precedence right NEG;
precedence right ISVOID;
precedence right AT;
precedence right DOT;


program	
	::= class_list:cl
	    {: RESULT = new programc(curr_lineno(), cl); :}
        ;

class_list
	/* single class */
	::= class:c 
	    {: RESULT = (new Classes(curr_lineno())).appendElement(c); :}
	/* several classes */
	| class_list:cl class:c 
	    {: RESULT = cl.appendElement(c); :}
	;

/* If no parent is specified, the class inherits from the Object class */
class
	::= CLASS TYPEID:n LBRACE dummy_feature_list:f RBRACE SEMI
	    {: RESULT = new class_c(curr_lineno(), n, 
		                   AbstractTable.idtable.addString("Object"), 
				   f, curr_filename()); :}
	| CLASS TYPEID:n INHERITS TYPEID:p LBRACE dummy_feature_list:f RBRACE SEMI
	    {: RESULT = new class_c(curr_lineno(), n, p, f, curr_filename()); :}
	| error SEMI 
	;

/* Feature list may be empty, but no empty features in list. */
dummy_feature_list
	::= /* empty */
	    {: RESULT = new Features(curr_lineno()); :}
	|   /* non-empty */ 
		dummy_feature_list:df feature:f SEMI
	  	{: RESULT = df.appendElement(f); :}
	;

feature
	::= /* method with no formals */ 
		OBJECTID:oid LPAREN RPAREN COLON TYPEID:tid LBRACE expression:e RBRACE
		{: RESULT = new method(curr_lineno(), oid, new Formals(curr_lineno()), tid, e); :}
	|	/* method with one or more formals */ 
		OBJECTID:oid LPAREN dummy_formal_list:dfl formal:f RPAREN COLON TYPEID:tid LBRACE expression:e RBRACE 
		{: RESULT = new method(curr_lineno(), oid, dfl.appendElement(f), tid, e); :}
	|   /* attr without assignment */ 
		OBJECTID:id COLON TYPEID:type 
		{: RESULT = new attr(curr_lineno(), id, type, new no_expr(curr_lineno())); :}
	|	/* attr with assignment */
		OBJECTID:id COLON TYPEID:type ASSIGN expression:expr
		{: RESULT = new attr(curr_lineno(), id, type, expr); :}
	|	/* error production */
		error
	;

dummy_formal_list
	::= /* empty */ 
		{: RESULT = new Formals(curr_lineno()); :} 
	|	/* non-empty */ 
		dummy_formal_list:df formal:f COMMA
		{: RESULT = df.appendElement(f); :}
	;

formal 
	::= OBJECTID:oid COLON TYPEID:tid
		{: RESULT = new formalc(curr_lineno(), oid, tid); :}
	;
branch
	::=  /* single branch */
		OBJECTID:oid COLON TYPEID:tid DARROW expression:e SEMI
		{: RESULT = new branch(curr_lineno(), oid, tid, e); :}
	;

case_list
	::= /* one case */
		branch:b 
		{: RESULT = (new Cases(curr_lineno())).appendElement(b); :}
	|  /* multiple cases */
		case_list:cl branch:b
		{: RESULT = cl.appendElement(b); :}
	;
semi_expression
	::= /* standard production */
		expression:e SEMI
		{: RESULT = e; :}
	|	/* error production */
		error SEMI
	;

let_declaration_expression
	::= /* base case */
		IN expression:e
		{: RESULT = e; :}
	|	/* declaration without assignment */
		COMMA OBJECTID:oid COLON TYPEID:tid let_declaration_expression:e
		{: RESULT = new let(curr_lineno(), oid, tid, new no_expr(curr_lineno()), e); :}
	|	/* declaration with assignment */
		COMMA OBJECTID:oid COLON TYPEID:tid ASSIGN expression:a let_declaration_expression:e
		{: RESULT = new let(curr_lineno(), oid, tid, a, e); :}
	|	/* error */
		COMMA error let_declaration_expression:e
		{: RESULT = e; :}
	;

semi_expressions
	::= /* one expression */
		semi_expression:e
		{: RESULT = new Expressions(curr_lineno()).appendElement(e); :}
	|	/* two or more expressions */
		semi_expressions:el semi_expression:e 
		{: RESULT = el.appendElement(e); :}
	|	/* error */
		error SEMI
	;

comma_expressions
	::= /* empty */
		{: RESULT = new Expressions(curr_lineno()); :}
	|  /* non-empty */
		comma_expressions:el expression:e COMMA
		{: RESULT = el.appendElement(e); :}
	;


expression
	::= /* assignment */
		OBJECTID:oid ASSIGN expression:e
		{: RESULT = new assign(curr_lineno(), oid, e); :}
	|  /* dispatch without typeID, without parameters */ 
		expression:e DOT OBJECTID:oid LPAREN RPAREN
		{: RESULT = new dispatch(curr_lineno(), e, oid, new Expressions(curr_lineno())); :}
	|  /* dispatch without typeID, with parameters */
		expression:e1 DOT OBJECTID:oid LPAREN comma_expressions:el expression:e2 RPAREN
		{: RESULT = new dispatch(curr_lineno(), e1, oid, el.appendElement(e2)); :}
	|  /* dispatch with typeID, without parameters */
		expression:e AT TYPEID:tid DOT OBJECTID:oid LPAREN RPAREN
		{: RESULT = new static_dispatch(curr_lineno(), e, tid, oid, new Expressions(curr_lineno())); :}
	|  /* dispatch with typeID, with parameters */
		expression:e1 AT TYPEID:tid DOT OBJECTID:oid LPAREN comma_expressions:el expression:e2 RPAREN
		{: RESULT = new static_dispatch(curr_lineno(), e1, tid, oid, el.appendElement(e2)); :}
	|  /* self dispatch with no parameters */ 
		OBJECTID:oid LPAREN RPAREN
		{: RESULT = new dispatch(curr_lineno(), (new object(curr_lineno(), new IdSymbol(new String("self"), 4, 0))), oid, new Expressions(curr_lineno())); :}
	|  /* self dispatch with parameters */
		OBJECTID:oid LPAREN comma_expressions:el expression:e RPAREN
		{: RESULT = new dispatch(curr_lineno(), (new object(curr_lineno(), new IdSymbol(new String("self"), 4, 0))), oid, el.appendElement(e)); :}
	|  /* if statement */
		IF expression:e1 THEN expression:e2 ELSE expression:e3 FI
		{: RESULT = new cond(curr_lineno(), e1, e2, e3); :}
	|  /* while loop */
		WHILE expression:e1 LOOP expression:e2 POOL
		{: RESULT = new loop(curr_lineno(), e1, e2); :}
	|  /* expr_list */
		LBRACE semi_expressions:el RBRACE
		{: RESULT = new block(curr_lineno(), el); :} 
	|	/* let statement with assignment */
		LET OBJECTID:oid COLON TYPEID:tid ASSIGN expression:a let_declaration_expression:e
		{: RESULT = new let(curr_lineno(), oid, tid, a, e); :}
	|	/* let statement with no assignment */
		LET OBJECTID:oid COLON TYPEID:tid let_declaration_expression:e
		{: RESULT = new let(curr_lineno(), oid, tid, new no_expr(curr_lineno()), e); :}
	|	/* let statement with an error in the first assignment */
		LET error let_declaration_expression:e
		{: RESULT = e; :}
	|  /* case statement */
		CASE expression:e OF case_list:cl ESAC
		{: RESULT = new typcase(curr_lineno(), e, cl); :}
	|  /* new */
		NEW TYPEID:tid
		{: RESULT = new new_(curr_lineno(), tid); :}
	|  /* isvoid */
		ISVOID expression:e
		{: RESULT = new isvoid(curr_lineno(), e); :}
	|  /* subtraction */
		expression:e1 MINUS expression:e2
		{: RESULT = new sub(curr_lineno(), e1, e2); :}
	|  /* addition */
		expression:e1 PLUS expression:e2
		{: RESULT = new plus(curr_lineno(), e1, e2); :}
	|  /* multiplication */
		expression:e1 MULT expression:e2
		{: RESULT = new mul(curr_lineno(), e1, e2); :} 
	|  /* division */
		expression:e1 DIV expression:e2
		{: RESULT = new divide(curr_lineno(), e1, e2); :}
	|  /* negation */
		NEG expression:e
		{: RESULT = new neg(curr_lineno(), e); :} 
	|  /* less than */	
		expression:e1 LT expression: e2
		{: RESULT = new lt(curr_lineno(), e1, e2); :}
	|  /* less than equal to */ 
		expression:e1 LT EQ expression:e2
		{: RESULT = new leq(curr_lineno(), e1, e2); :}
	|  /* equal */
		expression:e1 EQ expression:e2
		{: RESULT = new eq(curr_lineno(), e1, e2); :}
	|  /* not */ 
		NOT expression:e
		{: RESULT = new comp(curr_lineno(), e); :}
	|  /* parentheses */ 
		LPAREN expression:e RPAREN
		{: RESULT = e; :}
	|  /* object */ 
		OBJECTID:oid
		{: RESULT = new object(curr_lineno(), oid); :}
	|  /* string constant */ 
		STR_CONST:s
		{: RESULT = new string_const(curr_lineno(), s); :}
	|  /* integer constant */ 
		INT_CONST:i
		{: RESULT = new int_const(curr_lineno(), i); :}
	|  /* boolean constant */ 
		BOOL_CONST:b
		{: RESULT = new bool_const(curr_lineno(), b); :}
	;


