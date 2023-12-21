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

// DOUBLEQUOTATION : '"';

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

INT : 'int';
FLOAT : 'float';
STRING : 'String';
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

IDENT : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;

CLASS : 'class';
EXTENDS : 'extends';
PROTECTED : 'protected';

MULTI_LINE_STRING : '\\';

COMMENT : ('//'.*? '\n'|'/*' .*? '*/') {skip();};

STRING_LITERAL : '"'(  ~ ('"' | '\n'))*'"';
INT_LITERAL : ('+' | '-')?DIGIT+;
FLOAT_LITERAL : ('+' | '-')?((DIGIT+'.'DIGIT*) | (DIGIT*'.'DIGIT+));

DIGIT : (('0'..'9')|'1');

ASM : 'asm';

RTL : '\n' {skip();};
TAB : '\t' {skip();};
SPACE : ' ' {skip();};

DUMMY_TOKEN: .;