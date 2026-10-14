---
description: Recursive Descent Parser
---

# Lab 3 \(RDP\)

![](/img/rdp.gif)

En este laboratorio aprenderán a implementar un Recursive Descent Parser para una gramática simple.

Los repositorios ya fueron creados, obtenga sus archivos con:

```
git clone https://github.com/cc-4/lab03-2026-mi-usuario
```

## 1. Introducción

En las últimas clases ustedes vieron el tema de **Recursive Descent Parsing \(RDP para simplificar\)**, aprendieron que es un parser top-down bastante ineficiente pero a cambio muy fácil de implementar. También vieron de forma general un algoritmo para implementarlo y en este laboratorio lo pondremos en práctica.

Como el nombre sugiere, un **Recursive Descent Parser** usa funciones recursivas para implementar un parser predictivo. La idea central es que cada no terminal es representado por una función (no numerada) que a su vez llama otras funciones (numeradas) que representan a cada posible producción

**Recuerden que**:

1. Un analizador léxico \(lexer\) convierte texto \(raw-text\) en un stream de tokens.
2. Un analizador sintáctico \(parser\) convierte el stream de tokens en un AST.

De forma gráfica esos 2 pasos los podemos ver asi:

![](/img/lexerparser.png)

En este laboratorio realizaremos **una calculadora con un par de operaciones** e iremos un paso más adelante al implementar un **intérprete**, es decir no crearemos un árbol sintáctico sino que evaluaremos en el momento las expresiones.

## 2. Gramática

La gramática con la que trabajaremos es la siguiente:

```bash
# note que S -> E ; tiene un semicolon al final
# es la unica que lo tiene

S ::= E ;
E ::= E + E
  |   E - E
  |   E * E
  |   E / E
  |   E % E
  |   E ^ E
  |   - E
  |   (E)
  |   number

# number en nuestro caso significara un double
```

Esta gramática tiene un gran problema para nuestro RDP. Si se recuerdan por lo visto en clase, sufre de un problema llamado **left-recursion**. Una de las desventajas de este tipo de parsers es que las gramáticas con las que puede trabajar son aquellas que no son recursivas hacia la izquierda y claramente esta lo es. Lo que necesitamos hacer primero es limpiar nuestra gramática, asegurándonos que la nueva gramática sea equivalente a la original.

!!! note "Tarea 1"
    Limpie su gramática. Asegúrese que la nueva gramática no tenga ambigüedad, tome en cuenta la precedencia de operaciones, no tenga recursión por la izquierda, sea equivalente a la gramática original. Escriba su gramática resultante en **grammar.txt**

## 3. Lexer

Luego de limpiar la gramática, ustedes van a implementar el lexer para la gramática de nuestra calculadora utilizando **JLex**. El motivo principal de esto es para que sigan ganando práctica con esta herramienta, que surgan dudas y que los ayude a empezar/avanzar con el proyecto.

Dentro del repositorio van a encontrar un archivo llamado **lexer.lex**, en ese archivo ustedes tienen que definir el lexer para la gramática. Dentro de ese archivo hay en forma de comentarios algunas instrucciones para guiarlos.

### 3.1 Clase Token

En el directorio de trabajo hay una clase llamada **Token** que nos va a servir para representar los tokens de la gramática y es el tipo de objeto que tenemos que devolver dentro de las acciones del lexer. Esta clase tiene 2 constructores:

1. **Token\(int id, String val\)**
2. **Token\(int id\)**

Dentro de esta clase también están definidos los IDs que representan cada token y tienen que hacer uso de ellos cuando encuentren un token. En ese archivo también están definidos otros métodos que pueden ser útiles para la siguiente parte del laboratorio.

**Ejemplo:**

```java
// Asi se veria en la parte de acciones del archivo .lex
<YYINITIAL>{SEMI}   { return new Token(Token.SEMI);             }
<YYINITIAL>{NUMBER} { return new Token(Token.NUMBER, yytext()); }
```

Cuando tengan listo más de algo, pueden probar lo que hicieron utilizando el siguiente comando:

```bash
make lexer
./lexer "2 + 2;"
NUMBER : 2
+
NUMBER : 2
;
```

**Aviso:** En ocasiones el símbolo * puede dar problema al probar esta parte del lab. No se preocupe mucho por esto y siga trabajando las demás partes.

!!! note "Tarea 2"
    Escriba sus expresiones regulares en **lexer.lex**, construya y pruebe su lexer.

## 4. Parser

Para el segundo ejercicio de este laboratorio ustedes implementarán un **RDP**. Esta gramática es bastante simple y prácticamente se trata solo de expresiones aritméticas. Parsear expresiones de este tipo con recursive descent tiene 2 problemas:

1. Obtener un árbol sintáctico que siga la precedencia y la asociatividad de los operadores.
2. Hacerlo eficientemente cuando hay muchos niveles de precedencia.

En clase ustedes vieron la clásica solución para el primer problema, que a pesar de que es bastante buena y elegante, no resuelve el segundo problema. En este laboratorio les vamos a enseñar una técnica llamada **Shunting Yard Algorithm** que es más eficiente y resuelve los dos problemas.

### 4.1 Clase Parser

En el directorio de trabajo van a encontrar un archivo llamado **Parser.java**, en este archivo es donde ustedes tienen que implementar el parser. Prácticamente lo que tienen que hacer es crear una plantilla con funciones recursivas de la gramatica que modificamos. Aquí hay unas funciones que les pueden ser útiles como **term()**.

**Ejemplo:**

Si nuestra gramática empieza de esta manera `S ::= E;` podriamos implementarlo de la siguiente manera.

```java
boolean S() {
    return E() && term(Token.SEMI);
}

boolean E() {
    ...
}
```

!!! note "Tarea 3"
    Implemente un RDP basado en la gramática que limpió, escriba las funciones necesarias en **Parser.java**

### 4.2 Shunting Yard Algorithm

La idea del algoritmo **Shunting Yard** es mantener los operadores en un stack hasta que todos los operandos han sido parseados. Los operandos se mantienen en un segundo stack. El algoritmo shunting yard puede utilizarse directamente para evaluar las expresiones mientras son parseadas \(como un interprete, que es lo que vamos hacer\).

La idea central del algoritmo es mantener los operadores en el stack ordenados por precedencia **\(la precedencia más baja en el fondo del stack y la más alta en el top del stack\)**, por lo menos en la ausencia de paréntesis. Antes de meter un operador en el stack de operadores, todos los operadores que tienen mayor precedencia son sacados del stack. Sacar un operador del stack de operadores consiste en remover el operador y sus operandos del stack de operandos, evaluar, y meter el resultado en el stack de operandos. Al final de una expresión los operadores que quedan son sacados y evaluados con sus respectivos operandos.

La siguiente tabla ilustra el proceso para un input : **x \* y + z.** El stack se va llenando a la izquierda.

![](/img/shuntingyard.png)

* **push\(a\)**    : hace push de a en el stack de operandos
* **pushOp\(op\)** : hace push de un operador en el stack de operadores
* **pre\(op\)**    : devuelve precedencia de un operador

!!! note "Tarea extra"
    Implemente el algoritmo Shunting Yard para que su RDP ahora sea capaz de realizar las operaciones matemáticas ingresadas, escriba el código necesario en **Parser.java**

### 4.3 Precedencia

Para nuestra gramática la precedencia es la siguiente de mayor a menor:

1. **( )**
2. **- unario**
3. **^**
4. **\* / %**
5. **+ -**

Para probar su RDP tienen que hacer lo siguiente:

```bash
make parser
./parser
>>> 2 + 2;
4.0
>>>
```

## Entrega

Al finalizar el periodo de clase haga commit y push de sus avances y suba el link de su repositorio al GES, tendrá cero si no entrega avances. Cada vez que complete una sección, así como al terminar su laboratorio, haga commit y push nuevamente. 

