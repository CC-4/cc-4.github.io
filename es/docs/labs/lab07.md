# Lab 7 \(Análisis Semántico II\)

En este laboratorio van a continuar con el análisis semántico del lenguaje Viper y así ganar aún más intuición en esta fase del compilador y que plasmar estas ideas en su proyecto sea bastante sencillo. 

Antes de empezar, vamos a obtener los archivos necesarios desde Github Classroom:

```bash
https://classroom.github.com/a/bs9-cgvz
```

!!! danger ""
	Por favor lean todas las instrucciones del lab, antes de empezar.

## Reglas de Inferencia

Vamos a continuar utilizando el archivo que tiene todas las reglas de inferencia del lenguaje, pueden descargar este archivo aquí:

[Reglas de inferencia de Viper](../cc4/viper_language_type_rules.pdf)

### ¿Qué tienen que hacer?

Como se pudieron dar cuenta en el laboratorio pasado en **src/viper/tree** están definidos los nodos que representan el AST de Viper. En este laboratorio ustedes tienen que completar los 10 nodos restantes, estos nodos son los siguientes:

1. **Assign**
2. **Call**
3. **Declaration**
4. **Equal**
5. **Id**
6. **If**
7. **LessEqual**
8. **LessThan**
9. **Not**
10. **While**

!!! danger "Lab anterior"
	Ustedes tienen que copiar lo que implementaron en el laboratorio pasado, de lo contrario el autograder no les dará todos los puntos, simplemente tienen que copiar y reemplazar los archivos. 

Para algunos de estos nodos como **LessEqual**, **LessThan**, **Not** su código debería de ser bastante similar a lo que hicieron con las operaciones artiméticas, incluso para **Equal**, solo que aquí tenemos la restricción que ambos operandos tienen que ser del mismo tipo ya sea **int**, **bool** o **str**. Para los demás nodos si van a tener que hacer un par de cosas más.

Para que tengan una referencia de que tanto tendrían que agregar a los diferentes archivos, aquí están las estadísticas de nuestra implementación:

```bash
 src/viper/tree/Assign.java      | 12 +++++++++++-
 src/viper/tree/Call.java        | 22 +++++++++++++++++++++-
 src/viper/tree/Declaration.java | 10 +++++++++-
 src/viper/tree/Equal.java       |  7 ++++++-
 src/viper/tree/Id.java          |  8 +++++++-
 src/viper/tree/If.java          | 11 ++++++++++-
 src/viper/tree/LessEqual.java   |  7 ++++++-
 src/viper/tree/LessThan.java    |  7 ++++++-
 src/viper/tree/Not.java         |  6 +++++-
 src/viper/tree/While.java       |  8 +++++++-
 10 files changed, 88 insertions(+), 10 deletions(-)
```

### Implementación

Para cada nodo del AST hay una serie de pasos que se tienen que realizar para garantizar que el análisis semántico capture todos los errores posibles, recuerden que esta es nuestra última barrera antes de **codegen** para atrapar errores.

#### Assign

Para el assign primero deben llamar recursivamente a semant de `e`. Luego verificar que la variable a la se se le está asignando un valor exista en algún scope, si esta variable no existe deberían devolver el siguiente error e indicar que el tipo de la expresión es **void**.

```java
SemantErrors.cannotFindSymbol(line, col, name);
```

!!!info "void"
	En general cuando no se pueda recuperar del error y no se le pueda asignar un tipo a una expresión, el tipo de la expresión será **void**. En el proyecto haremos algo similar con **Object**.

Si la variable a la que se le está asignando un valor existe, pero tiene un tipo diferente que el de la expresión, deberían de devolver el siguiente error:

```java
SemantErrors.incompatibleTypes(e.line, e.col, e.getType(), t);
// t debería de ser el tipo que devolvio su entorno O
```

Como en este error si se pueden recuperar porque si saben cual es el tipo de la variable, tienen que poner que el tipo del nodo es el tipo de la variable. Si ambos tipos coinciden de igual forma tienen que indicar que el tipo del nodo es el tipo de la variable.

#### Call

El análisis semántico de **Call** es lo más complicado que realizarán en este laboratorio,  primero deberían llamar recursivamente a `semant` de cada **actual** que tenga el nodo, y atrapar sus errores. Luego  si la función que se está mandando a llamar no existe deberían de devolver el siguiente error e indicar que el tipo es **void**:

```java
SemantErrors.cannotFindSymbol(line, col, name);
```

!!!info "O"
	Van a encontrar útil el entorno **O** en esta parte del laboratorio.

Si la función si existe, ustedes deberían verificar que la cantidad de argumentos \(actuals\) sea igual a la cantidad de parámetros formales, si no es así devolver el siguiente error:

```java
SemantErrors.functionCalledWithWrongNumberOfArgs(line, col, name);
```

Si ocurre este error todavía es posible recuperarse del error porque sabemos el tipo de retorno de la función, entonces tienen que indicar que el tipo del nodo es el tipo de retorno de la expresión. Por último \(si el tamaño de actuals es igual al de los formals\) deberían de verificar que cada actual sea del mismo tipo, si no es así indicar el siguiente error:

```java
SemantErrors.incompatibleTypes(e.line, e.col, e.getType(), f.type);
```

!!!info "Tipo de retorno"
	Siempre que sepan que la función existe, el tipo del nodo debería de ser igual al tipo de retorno de la función.

#### Declaration

Como este nodo no es una expresión, no es necesario anotarlo con un tipo, de todos modos es necesario hacerle su respectivo análisis semántico. Primero deberían de comenzar llamando recursivamente a `semant` de **init**. Luego verificar que la variable no haya sido declarada ya en el **scope** actual, si ya existe indicar el siguiente error:

```java
SemantErrors.variableIsAlreadyDefined(line, col, name);
```

!!!info "Shadowing"
	El error anterior solo debería ocurrir si la variable es declarada más de una vez en el scope actual, algo como esto:

	```python
	def foo(x: int): void {
	    int x = 10;
	}
	```

	y no en el siguiente caso:

	```python
	def foo(x: int): void {
	    int y = 10;
	    while (true) {
		int y = 20;
	    }
	}
	```

	ya que el lenguaje **Viper**,  lastimosamente,  permite **shadowing** como en el lenguage **COOL** y otros lenguajes populares como **java** o **C**. ¿Qué método de **O** les conviene más utilizar?.

Si no es el caso que la variable está declarada más de una vez en el **scope** actual, deberían de agregarla a este. Recuerden que lo que nos interesa guardar de las variables en **O** es el tipo y nada más. Por último verificar que el tipo de **init** sea el mismo tipo que el tipo declarado, de lo contrario indicar el siguiente error:

```java
SemantErrors.incompatibleTypes(init.line, init.col, init.getType(), type);
```

#### Equal

Para este nodo tienen que hacer algo similar a lo que hicieron en las expresiones aritméticas, llamar recursivamente a `semant` de cada operando, en este caso **ambos** operandos tienen que ser del mismo tipo para poderse comparar, si no indicar el siguiente error:

```java
SemantErrors.incomparableTypes(line, col, e1.getType(), e2.getType());
```

Si este error sucede es fácil recuperarse porque sabemos que el tipo que devuelve una comparación siempre es **bool**, deberían de anotar este nodo con el tipo **bool**.

#### LessEqual, LessThan

Lo mismo que **Equal**, lo único que cambia aquí es que ambos operandos del nodo tienen que ser de tipo **int**, si alguno no lo es indicar el siguiente error:

```java
SemantErrors.badOperandTypesForBinaryOp(line, col, operator);
// cambien operator con el operador del nodo, i.e, <= o <
```

No se olviden anotar el nodo con el tipo **bool.** 

#### Id

Este es nodo es bastante fácil, unicamente tienen que buscar en el entorno **O** si la variable existe en algún **scope** si no existe indicar el siguiente error y anotar el nodo con **void**:

```java
SemantErrors.cannotFindSymbol(line, col, name);
```

Si la variable existe, anotar el nodo con el tipo que nos indica **O**.

#### If

Para el if ustedes deberían de mandar a llamar recursivamente a `semant` de **pred** verificar si este es de tipo **bool** y si no lo es indicar el siguiente error:

```java
SemantErrors.incompatibleTypes(pred.line, pred.col, pred.getType(), Type.BOOL);
```

Luego mandar a llamar recursivamente a `semant` de **thenp** y luego **elsep**, recuerden que en Viper cada par de llaves `{}` abre un **scope** nuevo. Nuevamente este nodo no es una expresión entonces no es necesario anotarlo.

!!!info "Scope nuevo"
	Cuando nos referimos a que se abre un scope nuevo, su código debería de verse algo así:

	```java
	O.enterScope();
	// analisis semantico de algo
	O.exitScope()
	```

#### While

El while es muy similar al **if**, tienen que llamar recursivamente de **cond**, verificar que sea de tipo **bool**, si no lo es indicar el siguiente error:

```java
SemantErrors.incompatibleTypes(cond.line, cond.col, cond.getType(), Type.BOOL);
```

Luego mandar a llamar recursivamente a **body**, nuevamente abriendo un **scope** nuevo. Nuevamente este nodo no es una expresión entonces no es necesario anotarlo.

### Autograder

Para probar su implementación pueden utitlizar lo siguiente:

```bash
sudo ./gradlew build
./check
```

Si todo lo tienen bien, les debería de salir lo siguiente:

```bash
      Autograder


+1    (badassign2)     
+1      (compare)      
+1 (unexpectedreturn)  
+1    (diffrettype)    
+1       (call)        
+1       (arith)       
+1   (mainwithargs)    
+1     (baddecl2)      
+1       (badid)       
+1       (fact)        
+1       (basic)       
+1     (badarith)      
+1        (if)         
+1     (mainnoint)     
+1     (badcall2)      
+1     (badcall3)      
+1        (id)         
+1     (badassign)     
+1       (good)        
+1       (while)       
+1      (assign)       
+1     (shadowing)     
+1       (badif)       
+1     (badcall1)      
+1    (badformals)     
+1     (badequal)      
+1       (decl)        
+1      (string)       
+1     (badwhile)      
+1      (nomain)       
+1       (bool)        
+1       (equal)       
+1    (missingret)     
+1     (baddecl1)      
+1    (badcompare)     


=> You got a score of 35 out of 35.
```

Si ustedes desean probar en un archivo en específico pueden hacer lo siguiente:

```bash
sudo ./gradlew build
./run <archivo>
```

En la carpeta **examples/** hay algunos ejemplos para que puedan probar. 

**¡LISTO!** Han terminado el análisis semántico de **Viper**, es hora de lograr terminar el análisis semántico de **COOL**, esperamos que esta serie de laboratorios les hayan ayudado bastante.

