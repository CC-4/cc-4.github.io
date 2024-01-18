# Lab 4 \(JCUP\)

![](/img/cup_logo.gif)

En este laboratorio aprenderán a utilizar **cup**, el analizador sintáctico para la fase 2 del proyecto.

## 1. Introducción

Descarguen todos los archivos del siguiente repositorio:

```text
https://classroom.github.com/a/RYr4XVtS
```

Ahora que han terminado la fase 1 de su proyecto, saben utilizar JLex para el análisis léxico de una cadena de caracteres. Como han visto en clase, el análisis sintáctico se basa en gramáticas, y tiene algoritmos muy bien definidos para poder ser implementado. Para prepararlos para la fase 2, utilizaremos una herramienta llamada **cup** que funciona para analizar la sintáxis de un texto dado.

Al igual que JLex, cup cuenta con una sección en donde se colocan las funciones que posteriormente se copiarán en la clase generada. En este laboratorio no utilizaremos esta funcionalidad, pero les será útil para su proyecto. Luego existe una lista de símbolos terminales, como los paréntesis, los dígitos, etc., seguido de una lista de símbolos no terminales. Tanto los símbolos terminales como los no terminales pueden tener un tipo definido:

```java
terminal PLUS, MINUS, MULT;
terminal Integer INTEGER;
terminal Float FLOAT;
```

Luego pueden especificar una precedencia. Por ejemplo, una multiplicación se debe hacer antes que una suma. Para este tipo de precedencia deben emplear **precedence left**, como en el siguiente ejemplo:

```java
precedence left PLUS, MINUS;
precedence left TIMES;
```

Es importante recordar que a cup no le importa la aritmética, le importan los árboles. Esto quiere decir que un operador con una mayor precedencia quedará más cerca de la raíz de su árbol.

Seguido de eso, van todas las reglas de producción. Siguiendo la siguiente sintáxis, en donde `RESULT` es la derivación usando bottom-up parsing.

```java
parent_expr ::= LPAREN expr:e RPAREN {: RESULT = e; :};
```

## 2. Lexer

La gramática con la que trabajaremos es la siguiente:

```bash
S         ::= S expr_part
          |   expr_part ;
expr_part ::= expr ;
expr      ::= exprI | exprF
exprI     ::= I + I
          |   I - I
          |   I * I
          |   I / I
          |   I % I
          |   (I)
exprF     ::= I + F
          |   F + I
          |   F + F
          |   I - F
          |   F - I
          |   F - F
          |   I * F
          |   F * I
          |   F * F
          |   I / F
          |   F / I
          |   F / F
          |   I ^ F
          |   F ^ I
          |   F ^ F
          |   sin I
          |   sin F
          |   cos I
          |   cos F
          |   tan I
          |   tan F
          |   (F)

# donde I es un entero y F es un float.
```

Para la primer parte deben completar el archivo **calculator.lex** generando los tokens necesarios para una calculadora con operaciones básicas y que maneje tanto floats como enteros. Noten que las funciones trigonométricas no son case sensitive, por lo que "sin" y "sIN" son aceptadas de igual manera. Esta es la lista de tokens que debe tener:

```text
SEMI
PLUS
MINUS
TIMES
DIVI
LPAREN
RPAREN
POW
REM
SIN
COS
TAN
INTEGER
FLOAT
```

Si analizan la gramática, pueden ver que una expresión puede ser una expresión entera, o una expresión de punto flotante.

## 3. Parser

Para la segunda parte, ustedes deben completar el archivo **calculator.cup**, agregando las reglas de derivación. Pueden agregar tantos símbolos no terminales como quieran, pero no pueden modificar ninguno de los símbolos terminales. Una vez agregadas todas las reglas de derivación, pueden compilar su archivo de la siguiente manera:

```bash
sh make.sh
```

y para correrlo:

```bash
sh calculator.sh
```

Así se deberá de ver al probarlo:

```bash
2 + 2;
= 4;
10 * sin(90) - 1;
= 9;
```

Puede trabajar las trigonométricas en grados o radianes, solo deje indicado en un comentario cuál indicó.

Una vez terminado, tienen que realizar un commit de los archivos .cup y .lex y subir al GES el link de su repositorio.

## Referencias

1. [cup Manual](http://www2.cs.tum.edu/projects/cup/manual.html) - Manual de JCup.
2. [cup Javadoc](http://web.stanford.edu/class/cs143/javadoc/java_cup/index.html) - Javadoc de JCup.



