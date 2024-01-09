lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.

SEMI : ';';

COMMA : ',';

OBRACE : '{';
CBRACE : '}';

OPARENT : '(';
CPARENT : ')';

EQUALS : '=';

OR : '||';
AND : '&&';
EQEQ : '==';
NEQ : '!=';
LEQ : '<=';
GEQ : '>=';
LT : '<';
GT : '>';

INSTANCEOF : 'instanceof';

PLUS : '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
PERCENT : '%';

EXCLAM : '!';

DOT : '.';

READINT : 'readInt';
READFLOAT : 'readFloat';
NEW : 'new';

TRUE : 'true';
FALSE : 'false';
THIS : 'this';
NULL : 'null';

IF : 'if';
ELSE : 'else';

WHILE : 'while';

RETURN : 'return';

PRINT : 'print';
PRINTLN : 'println';
PRINTX : 'printx';
PRINTLNX : 'printlnx';

CLASS : 'class';
EXTENDS : 'extends';
PROTECTED : 'protected';
ASM : 'asm';

COMMENT : ('//'.*? '\n'|'/*' .*? '*/') {skip();};

STRING : '"'(STRING_CAR | '\\' | '\\\\')*'"';
INT : '0' | (POSITIVE_DIGIT DIGIT*);
FLOAT : FLOATDEC | FLOATHEX;
INCLUDE : '#include' (' ')* ' "'(LETTER | DIGIT | '.' | '-' | '_')+ '"'{doInclude(getText());};
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT |'_')*;


MULTI_LINE_STRING : '"'(STRING_CAR | '\\' | '\\\\' | '\n')*'"';

POSITIVE_DIGIT : '1'..'9';
DIGIT : '0'..'9';
LETTER : ('a'..'z'|'A'..'Z');


NUM : DIGIT+;
EXP : ('E' | 'e') ('+' | '-')? NUM;
DEC : NUM '.' NUM;
FLOATDEC : (DEC | DEC EXP) ('F' | 'f')?;
DIGITHEX : DIGIT | ('A'..'F') | ('a'..'f');
NUMHEX : DIGITHEX+;
FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') ('+' | '-')? NUM ('F' | 'f')?;


RTL : '\n' {skip();};
TAB : '\t' {skip();};
SPACE : ' ' {skip();};
STRING_CAR : ~ ('"' | '\\' | '\n');
