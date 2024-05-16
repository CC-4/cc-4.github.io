# Lab 6 \(Análisis Semántico I\)

Es hora de empezar a poner en práctica el análisis semántico. El motivo de este laboratorio es que puedan ganar experiencia en como se hace el análisis semántico de un lenguaje simple y que posteriormente en su proyecto plasmen estas ideas. En el laboratorio anterior se enteraron de como funcionaba la tabla de símbolos, en este la vamos a utilizar juntamente con otras herramientas para anotar el árbol \(_AST_\) de un lenguaje llamado **Viper** con los tipos correspondientes.

Antes de empezar, vamos a obtener los archivos necesarios desde Github Classroom:

```bash
https://classroom.github.com/a/1z-JR4a4
```

!!!warning "Lean bien"
	Este lab es más complejo que los anteriores, por favor lean todas las instrucciones del lab, antes de empezar.

## Análisis Semántico del lenguaje Viper

En este laboratorio haremos el análisis semántico de algunos nodos del lenguaje Viper, así que los introduciremos a el un poco.

### Lenguaje Viper

**Viper** es un lenguaje bastante básico, es una mezcla de Python y C, y nos ayudará a entender mejor algunos conceptos de Semantic y Codegen por lo que deberán acostumbrarse un poco al lenguaje.

[Gramática de Viper](../cc4/viper_language_grammar.pdf)

La mejor forma de entender este lenguaje es revisando la gramática. Una vez hayan visto/entendido la gramática, esto les parecera conocido:

```python
  def factorial(n: int): int {
      int result = 0;
      if (n < 2) {
          result = 1;
      } else {
          result = n * factorial(n - 1);
      }
      return result;
  }
```

### Reglas de inferencia del lenguaje Viper

**Viper** tiene pocas reglas de inferencia a comparación de **COOL** y son menos complejas, sin embargo ayudan a entender bastante la dinámica de lo que tendrían que hacer en el proyecto, solo que sin objetos. Para ver estas reglas, pueden descargar el siguiente archivo:

[Reglas de inferencia de Viper](../cc4/viper_language_type_rules.pdf)

En ese documento están las reglas en el mismo formato que el manual de **COOL**, solo que en español y que son para el lenguaje **Viper** obviamente.

### ¿Qué tienen que hacer?

En los archivos de lab dentro de **src/viper/tree** están los nodos que representan el AST de un programa de Viper. En algunos de estos nodos ustedes tendrán que realizar el análisis semántico para completar el laboratorio. Los nodos que tienen que analizar esta semana son los siguientes:

1. **Function**
2. **BoolConst**
3. **IntConst**
4. **StrConst**
5. **Add**
6. **Sub**
7. **Mul**
8. **Div**
9. **Mod**
10. **Return**

En el lenguaje Viper existen **statements** y **expressions**. Las expresiones son las que siempre devuelven un valor y se les asigna un tipo, los statements no. Para asignar un tipo a una expresión vean la función **semant()** del nodo **NoReturn** para ver como es que se le asigna un tipo a una expresión utilizando la clase **Type**, además vean la clase **Type** para ver los métodos que tiene, porque les va a servir tenerlos en mente. También van a notar como está declarada la función **semant()** en los nodos del AST:

```java
public void semant(SymbolTable O, HashMap<String, Function> M) {
    // WRITE YOUR CODE HERE
}
```

**O** es la tabla de símbolos que utilizaron en el laboratorio anterior, **M** es el entorno de funciones que hace un mapping de nombre de función a **Function** \(esto es útil porque así se puede extraer fácilmente los formals y el tipo de retorno\). Algo así tendría que verse su método **semant*()** en su proyecto, solo que con las cosas de COOL y tomando en cuenta los nombres de clases y que son **AbstractSymbol**. En este laboratorio no vamos a utilizar a **M**.

Para que tengan una referencia, de que tanto tendrían que agregar a los diferentes archivos, aquí están las estadísticas de nuestra implementación:

```bash
 src/viper/tree/Add.java       |  7 ++++++-
 src/viper/tree/BoolConst.java |  2 +-
 src/viper/tree/Div.java       |  7 ++++++-
 src/viper/tree/Function.java  | 30 +++++++++++++++++++++++++++++-
 src/viper/tree/IntConst.java  |  4 ++--
 src/viper/tree/Mod.java       |  7 ++++++-
 src/viper/tree/Mul.java       |  7 ++++++-
 src/viper/tree/Return.java    |  3 ++-
 src/viper/tree/StrConst.java  |  4 ++--
 src/viper/tree/Sub.java       |  7 ++++++-
 10 files changed, 66 insertions(+), 12 deletions(-)
```

### Implementación

Para cada nodo del AST hay una serie de pasos que se tienen que realizar para garantizar que el análisis semántico capture todos los errores posibles, recuerden que esta es nuestra última barrera antes de **codegen** para atrapar errores.

#### Análisis de Function

El análisis de las funciones es lo más complejo que realizarán de este laboratorio. Recuerden que como se pueden declarar parámetros formales y variables dentro de una función, esta crea un **scope** nuevo, utilicen lo siguiente para crear un nuevo scope:

```java
O.enterScope()
```

!!! info "Scopes"
	Por lo general podemos decir que en Viper cada par de llaves `{}` crean un nuevo **scope**

Si la función se llama **main** hay un par de cosas que se tienen que considerar, una de ellas es que la función **main** no puede tener parámetros definidos y la otra es que siempre tiene que tener un tipo de retorno **int**. Cuando sea el caso que tenga parámetros definidos tienen que utilizar lo siguiente para imprimir un error:

```java
SemantErrors.mainFunctionShouldHaveNoArgs(line, col)
```

!!! info "Cantidad de formals de main"
	Utilicen `formals.size()` para verificar esto.


!!! info "line y col"
	Noten que todos los nodos en Viper tienen definido un **line** y un **col** ya que heredan de la clase **Node**.

Y cuando tenga un tipo de retorno diferente de **int**, tienen que utilizar lo siguiente:

```java
SemantErrors.mainFunctionShouldHaveAnIntRetType(line, col)
```

Ustedes tienen que agregar los formals al **scope** que acaban de crear, pero ustedes tienen que verificar que los nombres de los parámetros no se repitan, por ejemplo esto estaría incorrecto:

```python
def foo(x: int, x: int)
```

Cuando suceda lo anterior tienen que utilizar lo siguiente:

```java
SemantErrors.formalIsAlreadyDefined(formal.line, formal.col, formal.name)
```

!!! info "Agregar formal"
	Utilicen lo siguiente para agregar el formal al scope actual:

	```java
	O.add(formal.name, formal.type)
	```

	Noten que como valor mandamos el tipo del formal ¿Por qué?


!!! info "Tipo"
	`formal` tiene que ser una instancia de la clase **Formal**

Después deberían de mandar a llamar recursivamente a `semant` para los `statements` y la expresión de retorno `ret`. Cuando estas dos llamadas regresen si `ret` no tiene tipo **void** y la función si, tienen utilizar lo siguiente para reportar el error:

```java
SemantErrors.unexpectedReturnValue(ret.line, ret.col)
```

Si `ret` es **void** y la función tiene un tipo de retorno diferente de **void** utilizar lo siguiente:

```java
SemantErrors.missingReturnStatement(line, col)
```

Y en general si `ret` no es igual al tipo de retorno de la función entonces:

```java
SemantErrors.incompatibleTypes(ret.line, ret.col, ret.getType(), type)
```

Al finalizar el análisis deberían de cerrar el scope, para evitar otros errores:

```java
O.exitScope()
```

#### Constantes

Esto es bastante sencillo, si es una constante entera, el tipo de la expresión es **int**. Lo mismo pasa con las demás constantes, strings **str** y booleans **bool**.

#### Operaciones Aritméticas

Primero tienen que mandar a llamar recursivamente a `semant` de las expresiones que conforman el nodo. Las operaciones aritméticas siempre tienen que tener operandos de tipo **int** y siempre devuelven como resultado un **int**. Cuando algún operando no sea de tipo **int** tienen que utilizar el siguiente errror:

```java
SemantErrors.badOperandTypesForBinaryOp(line, col, operator)
```

!!! info "Operadores"
	`operator` tiene que ser un **String** que represente la operación binaria, por ejemplo `"+"`

Siempre tienen que asumir que el tipo de una expresión aritmética es un **int**, de lo contrario nos encontrariamos con errores en cascada poco informativos, consideren lo siguiente:

```bash
true + 2 + 2 + 2
```

Si no asumieramos lo de arriba pasaría lo siguiente:

```text
bad operand types for binary operator '+'
bad operand types for binary operator '+'
bad operand types for binary operator '+'
```

A pesar que solo el primer operando de esa serie de sumas es el incorrecto. Lo correcto sería desplegar lo siguiente:

```bash
bad operand types for binary operator '+'
```

Esto es una forma de recuperación de errores que también van a tener que realizar en su proyecto.

#### Return

Cuando hagan el análisis semántico de **Return** recuerden que el tipo del return es el mismo que el de la expresión `e` del return.

### Autograder

Para compilar un programa usando el semantic que acaba de implementar, use el comando:
```bash
sudo ./gradlew build
./run -viper examples/viper/nombreDelArchivo.vp
```

Para correr todas las pruebas, use el comando:

```bash
sudo ./gradlew build
./check viper
```

Si todo lo tienen bien, les debería de salir lo siguiente:

```bash
      Autograder


+1 (unexpectedreturn)  
+1    (diffrettype)    
+1       (arith)       
+1   (mainwithargs)    
+1       (basic)       
+1     (badarith)      
+1     (mainnoint)     
+1       (good)        
+1    (badformals)     
+1      (string)       
+1       (bool)        
+1    (missingret)     


=> You got a score of 12 out of 12.
```

Si necesitara compilar algún archivo con nuestro compilador de pruebas puede usar el siguiente comando. Esto le servirá principalmente cuando lleguemos a codegen.

```bash
./vipercl examples/viper/nombreDelArchivo.vp
```

Luego de haber compilado, use el siguiente comando para ejecutar.

```bash
./jupiter runtime/runtime.s examples/viper/nombreDelArchivo.s
```
