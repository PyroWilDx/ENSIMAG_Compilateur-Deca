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

//Mots réservés:
ASM : 'asm';

INSTANCEOF : 'instanceof';

READINT : 'readInt';
READFLOAT : 'readFloat';

TRUE : 'true';
FALSE : 'false';

THIS : 'this';

NEW : 'new';
NULL : 'null';

IF : 'if';
ELSE : 'else';

WHILE : 'while';

RETURN : 'return';

PRINT : 'print';
PRINTLN : 'println';
PRINTX : 'printx';
PRINTLNX : 'printlnx';


//Symboles spéciaux
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

PLUS : '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
PERCENT : '%';

EXCLAM : '!';
DOT : '.';


//Identificateurs
LETTER : 'a'..'z'|'A'..'Z';
DIGIT : ('0'..'9');
IDENT : (LETTER|'$'|'_')(LETTER|DIGIT|'$'|'_')*;


//Littéraux entiers
POSITIVE_DIGIT : ('1'..'9');
INT : '0'| POSITIVE_DIGIT DIGIT* ;


//Littéraux flottants
NUM : DIGIT+;
SIGN : ('+' | '-')?;
EXP : ('E' | 'e') SIGN NUM;
DEC : NUM '.' NUM;
FLOATDEC : (DEC | DEC EXP) ('F' | 'f')?;
DIGITHEX : DIGIT | 'A'..'F' | 'a'..'f';
NUMHEX : DIGITHEX+;
FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f')?;
FLOAT : FLOATDEC + FLOATHEX;


//Chaines de caractères
STRING_CAR : ~ ('"' | '\\' | '\n');
STRING : '"'(STRING_CAR | '\\' | '\\\\')*'"';
MULTI_LINE_STRING : '"'(STRING_CAR | '\\' | '\\\\' | '\n')*'"';


//Commentaires
COMMENT : ('//'.*? '\n'|'/*' .*? '*/') {skip();};


//Séparateurs
RTL : '\n' {skip();};
TAB : '\t' {skip();};
SPACE : ' ' {skip();};

//Inclusion de fichier
FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"';
DUMMY_TOKEN: .;