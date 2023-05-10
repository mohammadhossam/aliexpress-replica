grammar hamada;

expr : expr '+' expr     #AdditionExpr
     | NUMBER           #NumberExpr
     ;

NUMBER : [0-9]+ ;

WS : [ \t\r\n]+ -> skip;
