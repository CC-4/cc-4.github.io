# Lab 2 \(Lexer\)



En este lab implementarán la primera fase de un compilador, un analizador léxico, para una version sin objetos de COOL. Lo llamaremos CNOOL _**Classroom Not Object Oriented Language**_.

Un analizador léxico conforma la primer parte de un compilador. La función principal de un analizador léxico es tomar una cadena de caracteres y separarla en tokens. Cada uno de estos tokens representa un símbolo del lenguaje de programación.

Con el objetivo de ayudarlos en su proyecto, a lo largo de los labs implementaremos fases del proyecto en lenguajes más simples, como ahorita con CNOOL dejando de lado los objetos y los comentarios y diciendo que únicamente existen tres tipos básicos: String, Int y Bool.

Los tokens que debe implementar son los siguientes:
```bash
PLUS
MINUS
MULT
DIV
NEG
LT
LE
EQ
NOT
TYPEID
OBJECTID
ASSIGN
LPAREN
RPAREN
LBRACE
RBRACE
SEMI
COMMA
COLON
INT_CONST
BOOL_CONST
```

Si implementa correctamente los tokens anteriores, podrá escribir cualquier expresión que aparezca en esta gramática a la que llamaremos CNOOL:

```bash
program ::= [feature]+
feature ::= ID([formal[,formal]*]) : TYPE {expr};
formal  ::= ID : TYPE
expr    ::= ID <- expr
        |   ID([expr[,expr]*])    
        |   ID : TYPE [<- expr]
        |   {[expr;]+}
        |   expr + expr
        |   expr - expr
        |   expr * expr
        |   expr / expr
        |   ~ expr
        |   expr < expr
        |   expr <= expr
        |   expr = expr
        |   not expr
        |   (expr)
        |   ID
        |   integer
        |   string
        |   true
        |   false
```

Los archivos necesarios para este laboratorio ya los tiene en su máquina, son los mismos que usaremos para el proyecto 1. Para obtenerlos ejecute esta instrucción **adentro de la carpeta donde trabajará:**

```bash
make -f /usr/class/cc4/assignments/PA1/Makefile
```

De todos los archivos que se copiarán, únicamente deben de modificar **cool.lex**, y agregar las expresiones regulares necesarias para que se generen los tokens indicados. Puede leer pero no modificar el archivo **TokenConstants.java**, que contiene todos los tokens necesarios para el proyecto. Recuerde que para facilitar la implementación de este laboratorio, retiraremos los comentarios y los strings.

## Referencias

1. [JLex Manual](http://www.cs.princeton.edu/~appel/modern/java/JLex/) - Manual de JLex.

