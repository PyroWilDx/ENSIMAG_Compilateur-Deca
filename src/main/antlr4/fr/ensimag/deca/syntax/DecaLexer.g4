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
DUMMY_TOKEN: .;

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

IDENT : '   ';

CLASS : 'class';
EXTENDS : 'extends';
PROTECTED : 'protected';

MULTI_LINE_STRING : '\\';

ASM : 'asm';
