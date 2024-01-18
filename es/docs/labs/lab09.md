# Lab 9 \(CodeGen I\)

En este laboratorio van a generar código en lenguaje ensamblador RISC-V para algunos nodos del lenguaje **Viper** que utilizarón en el laboratorio 6 y 7. Debido a que este lenguaje no es orientado a objetos es más fácil generar código. 

Los archivos necesarios para este laboratorio los pueden encontrar en el siguiente enlace:

```bash
https://classroom.github.com/a/XnJyp8xe
```

Los archivos base tienen la misma estructura que vieron anteriormente en los laboratorios 6 y 7. Para este laboratorio el análisis semántico ya está hecho, así que no se tienen que preocupar de esta tarea, solamente se tienen que enfocar en generar código implementando un **Accumulator Machine**.

## Activation Record

El activation record para las funciones de Viper es muy similar al que tienen que utilizar para los métodos de COOL.

![Activation Record de Viper](/img/ar-viper.png)

Si tuvieramos la siguiente función de Viper

```python
def foo(x: int, y: int): void {
    int var = 10;
    print(var + x + y);
}
```

El activation record sería el siguiente:

![Activation Record de la Funci&#xF3;n foo](/img/ar-foo.png)

## Soporte

Tanto en el proyecto, como en este laboratorio, van a encontrar una clase de ayuda llamada **CgenSupport.java** que contiene algunas definiciones que sirven para generar código, revisen esta clase para hacerse una idea de lo que pueden hacer con ella. 

Aplicar ingeniería inversa es factible, siempre y cuando tengamos claro que está sucediendo. Utilizando el siguiente comando podemos compilar un archivo y ver lo que el generador de código ya hecho genera, para que podamos imitar ese comportamiento.

```bash
./vipercl <archivo>
```

Para compilar un archivo, utilizando su propio generador de código pueden hacer lo siguiente.

```bash
./compile <archivo>
```

Pueden correr un programa compilado utilizando lo siguiente.

```bash
./run <archivo>
```

## Generando Código

Ustedes tienen que generar código para los siguientes nodos de Viper:

* **Function**
* **Call**
* **BoolConst**
* **IntConst**
* **StrConst**
* **Add**
* **Sub**
* **Div**
* **Mod**
* **Mul**
* **Return**

Cada nodo de Viper tiene un método `code` con la siguiente firma:

```java
public void code(Counter locals, SymbolTable O, PrintStream p)
```

Los parámetros tienen el siguiente significado:

* **locals**: un contador que nos ayuda a saber cual es el siguiente espacio disponible en el área de variables locales dentro del activation record en relación al frame pointer \(no se utilizará en este laboratorio\).
* **O**: Es nuestra tabla de símbolos, esta nos sirve para guardar en que posición del activation record se encuentran los símbolos \(variables locales y parámetros\) en relación al frame pointer.
* **p**: Nos sirve para imprimir el código generado a un archivo.

Ustedes tienen que implementar el método `code` para los nodos antes mencionados con ayuda de la clase **CgenSupport.java**.

### Function

**Function** en cuestión de implementación es lo más complicado del laboratorio y lo primero que tienen que hacer. Usaremos este nodo como tutorial. Consideremos el mismo ejemplo anterior: 

```python
def foo(x: int, y: int): void {
    int var = 10;
    print(var + x + y);
}
```

El generador de código para una función de Viper tiene que hacer los siguientes puntos:

1. Definir una etiqueta que represente a la función.
2. Reservar espacio en el stack para las variables locales, el registro fp y el registro ra.
3. Crear un nuevo scope.
4. Guardar en **O** la dirección de los formals en relación al registro fp.
5. Mandar a llamar a `code` de los statements dentro de la función.
6. Mandar a llamar a `code` de la expresión de return.
7.  Destruir el scope creado.
8. Restaurar el stack, contemplando también el espacio que se reservo para los formals de parte del caller.

Para definir la etiqueta que represente a la función vamos a utilizar lo siguiente:

```java
CgenSupport.emitLabelDef(CgenSupport.DEF + name, p);
```

Esto imprimirá en el archivo donde estará el código generado lo siguiente:

```text
def_foo:
```

Luego tenemos que saber cuanto espacio necesitamos reservar para las variables locales y los dos registros `fp` y `ra`. El método `locals()` que está implementado en el nodo **Statements** nos ayuda a determinar la cantidad de espacio que se necesita reservar para las variables locales que van a aparecer dentro del cuerpo de la función y además sabemos que necesitamos 2 palabras adicionales para los registros `fp` y `ra`. Sabiendo eso podemos reservar espacio y crear el prólogo de la función utilizando lo siguiente:

```java
int size = statements.locals() + 2;
CgenSupport.emitPrologue(size, p);
```

Esto imprimirá lo siguiente:

```text
addi  sp sp -12
sw    fp 8(sp)
sw    ra 4(sp)
addi  fp sp 4
```

Noten que necesitamos 1 palabra para la variable local que aparece dentro del cuerpo de la función y además, 2 palabras adicionales para los registros `fp` y `ra` para un total de 3 palabras de 32 bits, es por eso que aparece el **-12** \(3 \* 4 = 12\). También noten como se guardan los registros y como se establece el frame pointer para que apunte hacia `ra`.

La función `locals()` ya está hecha en el laboratorio, pero para el proyecto ustedes la deben implementar. Esta función no es tan trivial como parece, recuerden que en un determinado tiempo hay variables vivas y variables muertas, por ejemplo:

```python
def foo(): void {
    int x = 10;
    while (x > 0) {
        int y = 20;
        ...
    }
    int z = 30;
    ...
}
```

Cuando llegamos a la declaración `int z = 30`, la variable `y` estará muerta y el espacio que utilizó se puede reciclar. Para este caso la función `locals()` tiene que devolver como resultado que se necesitan 2 palabras de 32 bits para las variables locales de la función, sabiendo que el espacio que utilizó `y` se puede reutilizar para `z`. Esto es lo que el compilador de referencia de cool **coolc** y **coolc-rv** hacen.

Ya habiendo impreso el prólogo podemos seguir con el paso 3 y 4, podemos hacer lo siguiente:

```java
O.enterScope();
for (int i = 0; i < formals.size(); i++) {
    Formal formal = formals.get(i);
    O.add(formal.name, size + i);
}
```

Noten como vamos indicando que los formals quedan arriba de los locals con `size + i`, siguiendo con el ejemplo si `size = 3`, entonces el parámetro `x` queda 3 posiciones arriba del frame pointer e `y` 4 posiciones arriba.

Para los pasos 5 y 6 basta con llamar a `code` para los statements y la expresión de retorno:

```java
statements.code(new Counter(2), O, p);
ret.code(new Counter(2), O, p);
```

Se preguntarán ¿por qué el `new Counter(2)`? Recuerden que el primer parámetro de la función `code` es un contador que nos indica el siguiente espacio disponible en el área de variables locales en relación al frame pointer. La primer variable siempre se encontrará 2 posiciones arriba del `fp` porque `ra` está al mismo nivel que `fp` y `old fp` está una posición arriba.

Luego hay que cerrar el scope:

```java
O.exitScope();
```

Y por último restaurar el stack, el epílogo de la función en otras palabras:

```java
CgenSupport.emitEpilogue(size + formals.size(), p);
```

Esto imprimirá lo siguiente:

```text
lw    fp 8(sp)
lw    ra 4(sp)
addi  sp sp 20
ret
```

Noten que estamos mandando `size + formals.size()`, ya que necesitamos restaurar también el espacio reservado por el caller que utilizó para meter los parámetros de la función. Por eso el **20**, porque se reservaron 3 palabras en el prólogo del callee y 2 parámetros tiene la función para un total de 5 palabras de 32 bits \(5 \* 4 = 20\). 

### Call

Para call ustedes tendrían que recorrer los actuals en el orden inverso, mandar a llamar a `code` y hacer push del resultado \(que está en `a0`\) en el stack. Luego hacer un `jal` hacia la función que se está mandando a llamar utilizando:

```java
CgenSupport.emitJal(CgenSupport.DEF + name, p);
```

En el nodo call hay un caso especial cuando se está mandando a llamar a la función `print/println`,  ya que estas aceptan imprimir enteros, booleans y strings, es necesario saber el tipo del argumento, para mandar a llamar a la función correcta:

* `def_print_int`/`def_println_int` &gt; para un argumento de tipo `int`
* `def_print_bool`/`def_println_bool` &gt; para un argumento de tipo `bool`
* `def_print_str`/`def_println_str` &gt; para un argumento de tipo `str`

!!! info "isPrint()"
	Utilicen el método `isPrint()` del nodo `Call` para saber si se está mandando a llamar ya sea a `print` o a `println`.

### Constantes

Para las constantes simplemente es cargar al acumulador \(registro `a0`\) la constante utilizando los siguientes métodos que están en **CgenSupport.java**:

```java
public static void emitLoadBool(String dest, boolean val, PrintStream p);
public static void emitLoadString(String dest, String val, PrintStream p);
public static void emitLoadInt(String dest, String val, PrintStream p);
```

### Nodos Aritméticos

Para los nodos aritméticos basta seguir las reglas del accumulator machine vistas en clase, por ejemplo si tuvieramos lo siguiente:

```text
2 + (3 * 2)
```

Un accumulator machine haría lo siguiente:

```text
li a0, 2
sw a0, 0(sp)
addi sp, sp, -4
li a0, 3
sw a0, 0(sp)
addi sp, sp, -4
li a0, 2
lw t1, 4(sp)
addi sp, sp, 4
mul a0, t1, a0
lw t1, 4(sp)
addi sp, sp, 4
add a0, t1, a0
```

Vean los métodos dentro de **CgenSupport.java** que les ayuden a implementar estos nodos, para el nodo **Div** hay algo adicional para manejar la división por cero, miren lo que el compilador de referencia genera.

!!! info "Label adicional
	Van a tener que utilizar `CgenSupport.nextLabel()`en **Div**

### Return

Facilito, simplemente llamar a `code` de la expresión del return.

## Autograder

Para probar su implementación pueden correr lo siguiente:

```bash
sudo ./gradlew build
./check
```

Si todo está bien les tendría que salir lo siguiente:

```bash
      Autograder


+1       (call)        
+1       (arith)       
+1     (constants)     
+1      (return)       


=> You got a score of 100 out of 100.
```

