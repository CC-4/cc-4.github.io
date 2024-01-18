# Lab 10 \(CodeGen II\)

Para este laboratorio van a completar la generación de código para el lenguaje Viper

Los archivos necesarios para este laboratorio los pueden encontrar en el siguiente enlace:

```bash
https://classroom.github.com/a/dyAqFODr
```

## Recordatorio

Por favor revisen el siguiente enlace para recordarse cómo está estructurado el **Activation Record** en Viper:

[Lab 9: Codegen 1](lab09.md)

## Variables Locales

Si no se recuerden de la firma del método `code` aquí está nuevamente:

```java
public void code(Counter locals, SymbolTable O, PrintStream p)
```

Los parámetros tienen el siguiente significado:

* **locals**: un contador que nos ayuda a saber cual es el siguiente espacio disponible en el área de variables locales dentro del activation record en relación al frame pointer.
* **O**: Es nuestra tabla de símbolos, esta nos sirve para guardar en que posición del activation record se encuentran los símbolos \(variables locales y parámetros\) en relación al frame pointer.
* **p**: Nos sirve para imprimir el código generado a un archivo.Activation Record

Cuando ustedes implementaron el nodo **Function** se dieron cuenta que fue necesario llamar al método `locals()` para preparar parte del prólogo de la función \(reservar espacio\). Esta función solo tiene una tarea en específico y es decir la cantidad de espacio necesario a reservar dentro del Activation Record para todas las variables locales \(sin contar parámetros formales\). Esta función lo que hace es visitar cada nodo dentro de los statements de una función \(recursivamente\) y verificar si son una instancia del nodo **Declaration** e ir incrementando la cantidad de espacio con `+1` cada vez que esto suceda. Si ustedes revisan la implementación de este método realmente hace más cosas, por ejemplo en el caso de un **If**:

```python
if (true) {
    int x = 10;
    int y = 20;
} else {
    int z = 30;
}
```

`locals()` devolvería 2 en vez de 1, porque en tiempo de compilación no se sabe si se va a tomar la consecuencia o la alternativa \(a menos que se hiciera una optimización que por el momento está fuera del objetivo de este laboratorio\), por lo que es necesario devolver el **máximo** entre los dos bloques de statements del **If**. Una implementación un poco más ineficiente en términos de memoria sería que `locals()` devolviera 3 para `x` , `y` y `z`, esto es ineficiente porque el espacio que se reserva para `x` y `y` se puede reutilizar para `z` porque el **scope** de la consecuencia no es el mismo ni se comparte con el de la alternativa. Ustedes son libres de implementar esto en su proyecto como ustedes crean que les permita terminarlo al 100%. 

Otra cosa importante que deben tener claro es que el único nodo que puede aumentar la cantidad de espacio a reservar en Viper es **Declaration** y los lugares donde puede aparecer este nodo es dentro de una función, dentro de los bloques de un **If**, y dentro del bloque de un **While**. En COOL ustedes van a tener que considerar a **Let** y a **Typecase** como los responsables de incrementar la cantidad de espacio. 

Bien, ahora que ya está un poco más claro como funciona `locals()` es hora de hablar un poco de como utilizar ese espacio \(lo que necesitan realmente para este laboratorio\). Como el espacio se reserva en el activation record de la función, para referenciar una posición de ese espacio de manera fácil es utilizando el frame pointer \(el registro`fp`\).  Volvamos al ejemplo del laboratorio pasado:

```python
def foo(x: int, y: int): void {
    int var = 10;
    print(var + x + y);
}
```

![Activation Record de la Funci&#xF3;n foo](/img/ar-foo.png)

Si se quisiera acceder al valor de la variable `var` bastaría con hacer lo siguiente en el ensamblador:

```text
lw a0, 8(fp)
```

!!!info "Para su proyecto"
	Como COOL es orientado a objetos y pueden haber atributos en cada clase, cuando se hace referencia a un identificador, este podría ser un atributo o variable local por lo que para hacer referencia a su espacio de memoria podría ser utilizando el frame pointer o el registro `s0` que guarda el self object.

## Soporte

Se les provee como en el laboratorio pasado un compilador completo de Viper:

```bash
./vipercl <archivo .vp>
```

pueden probar esto para tener una referencia de como es que se tiene que generar código para los diferentes nodos de este laboratorio \(pueden hacer lo mismo en el proyecto con `coolc-rv`\).

Para correr un archivo compilado, pueden utilizar:

```bash
./run <archivo .s>
```

Adicionalmente pueden utilizar los métodos y variables estáticas de la clase  **CgenSupport** para ayudarse a generar código.

## Generando Código

Los nodos restantes que ustedes tienen que completar son los siguientes:

* **Assign**
* **Declaration**
* **Equal**
* **LessEqual**
* **LessThan**
* **Not**
* **Id**
* **If**
* **While**

Para que tengan una idea de que tanto tienen que realizar, aquí van unas estadísticas:

```bash
 src/main/java/viper/tree/Assign.java      |  4 +++-
 src/main/java/viper/tree/Declaration.java |  6 +++++-
 src/main/java/viper/tree/Equal.java       | 11 ++++++++++-
 src/main/java/viper/tree/Id.java          |  3 ++-
 src/main/java/viper/tree/If.java          | 15 ++++++++++++++-
 src/main/java/viper/tree/LessEqual.java   | 11 ++++++++++-
 src/main/java/viper/tree/LessThan.java    | 11 ++++++++++-
 src/main/java/viper/tree/Not.java         |  8 +++++++-
 src/main/java/viper/tree/While.java       | 12 +++++++++++-
 9 files changed, 72 insertions(+), 9 deletions(-)
```

### Assign

La generación de código para **Assign** es de pocos pasos. Primero se tiene que mandar recursivamente a `code` de la expresión del nodo y cuando regrese el resultado estará en `a0`, luego determinar a que distancia del frame pointer esta guardada esa variable utilizando a `O` y por último guardar el resultado en ese espacio.

!!!info "Reutilizando la tabla O"
	Utilicen `O.lookup(name)`para determinar esa distancia. ¿Por qué lookup y no probe?

### Declaration

En **Declaration** casi se tiene que hacer lo mismo que en **Assign**, la diferencia es que en **Declaration** se está creando una variable y se le está asignando un valor inicial y en **Assign** la variable ya fue creada y solo se le está asignado un nuevo valor. Tienen que llamar a `code` de `init` recursivamente, el resultado cuando regrese estará en `a0`. Tienen que reservar el siguiente espacio disponible en el área de variables locales para la variable que está siendo creada y agregar esta información en la tabla `O`. Luego  guardar `a0` en ese espacio. Por último como este nodo no es una expresión, tenemos que cargar en `a0` el valor de void que es simplemente un 0.

!!!info "Usando el contador locals"
	Utilicen `locals.inc()` para reservar el siguiente espacio y `O.add(name, offset)` para guardar esa información en la tabla de símbolos.

!!!info "Cargando en a0"
	Para cargar en `a0` void pueden hacer lo siguiente:

	```java
	CgenSupport.emitLoadImm(CgenSupport.A0, 0, p);
	```

### Comparaciones

Para **LessEqual**, **LessThan** e **Equal**, la generación de código es igual en Viper. Primero tienen que crear una nueva etiqueta, llamar recursivamente a `code` del primer operando, hacer push de `a0` en el stack, luego llamar recursivamente a `code` del segundo operando, mover el resultado al registro `t1` hacer pop del stack hacia el registro `t2` , cargar en `a0` el boolean `true` efectuar la comparación correspondiente \(&lt;, &lt;=, ==\) utilizando los registros `t1` y `t2` , cargar el boolean `false` en caso de que la comparación no se cumpla y por último definir la etiqueta que crearon.

!!!info "Etiquetas"
	Para crear una nueva etiqueta pueden utilizar el método `nextLabel()` de **CgenSupport**. Para definir la etiqueta pueden utilizar `emitLabelDef(label, p)` también de **CgenSupport**.

### Not

El **Not** es similar a las comparaciones, lo único es que aquí solo mandamos a llamar a `code` del único operando que hay. Luego se tienen que realizar los mismos pasos que se mencionaron anterior mente.

!!!info "Branch on equal zero"
	Van a encontrar útil el método `CgenSupport.emitBeqz()`

### Id

Como sabemos la distancia desde el frame pointer en la que el **Id** fue guardado podemos utilizar la tabla `O` para obtener esa distancia y cargar en el registro `a0` el valor de ese espacio en memoria.

### While

Primero se tienen que crear dos etiquetas, una para iterar y otra para salir del ciclo, definir una de las primeras etiquetas \(la de iterar\), hacer `code` de la condición, comparar `a0` con 0 para saltar a la etiqueta para salir \(recurden 0 es falso y 1 verdadero\), crear un nuevo scope para el cuerpo del ciclo, hacer `code` del body \(para esto tienen que crear una copia del contador de variables locales\), salir del scope que se acaba de crear, saltar a la etiqueta que nos sirve para iterar, definir la etiqueta para salir, por último cargar `void`al registro `a0`.

!!!info "Copiando el contador locals"
	Utilicen el método `locals.copy()` para crear una copia del contador de variables locales.

### If

El **If** es similar al **While**, primero se crean dos etiquetas, una para la alternativa y otra para la salir de la consecuencia, luego se manda a llamar a `code` del predicado del nodo, luego se tiene que comparar `a0` con 0 para saltar a la alternativa, luego se crear un scope y se manda a llamar a `code` de `then` \(se tiene que crear una copia de locals como se hizo en el **While**\), salir del scope que se acaba de crear, saltar a la etiqueta de salir de la consecuencia, definir la etiqueta de la alternativa, crear un nuevo scope \(aquí también se tiene que copiar locals\), salir del scope que se acaba de crear, definir la etiqueta de salir y por último cargar en `a0` el valor `void` .

Listo han terminado de generar código para un lenguaje de bastante modesto, pero que puede hacer cosas interesantes todavía, por ejemplo:

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

def main(): int {
  int n = input('enter a number: ');
  println();
  print('fact(');
  print(n);
  print(') = ');
  print(factorial(n));
  return 0;
}
```

## Pruebas

Para probar lo que genera su implementación pueden utilizar lo siguiente:

```bash
sudo ./gradlew build # para compilar su laboratorio
sudo ./gradlew clean assemble # no deberia necesitarlo, pero si hay algun problema que no se quita
./compile <archivo.vp>  # compilar usando su laboratorio
./vipercl <archivo.vp>  # usar el compilador de prueba
```

En la carpeta **examples/** hay un par de ejemplos.

## Autograder

Después de hacer build, puede obtener su nota con este comando:

```bash
./check
```

Si todo está bien, tendrían que obtener la siguiente salida:

```bash
          Autograder


+4.55      (while2)       
+4.55        (leq)        
+4.55        (lt)         
+4.55        (eq)         
+4.55       (call)        
+4.55       (arith)       
+4.55       (fibo)        
+4.55   (declaration2)    
+4.55     (factorial)     
+4.55   (declaration3)    
+4.55        (if2)        
+4.55        (not)        
+4.55      (assign2)      
+4.55     (constants)     
+4.55      (return)       
+4.55        (lt2)        
+4.55       (while)       
+4.55        (if1)        
+4.55    (declaration)    
+4.55      (assign)       
+4.55       (leq2)        
+4.55       (not2)        


=> You got a score of 100 out of 100.
```

