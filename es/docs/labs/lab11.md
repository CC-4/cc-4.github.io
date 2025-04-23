# Lab 11 \(Optimizaciones\)

## Introducción

El objetivo de este laboratorio es preparar el **AST** para la generación de código, aplicandole la optimización de **Constant Folding**.

Antes de empezar, obtener los archivos desde Github Classroom:

```text
https://classroom.github.com/a/GJXSN7iR
```

## Descripción

En lugar de generar código de forma directa, realizaremos una de las optimizaciones mas simples: aplicaremos **Constant Folding** a la grmática siguiente, la cual solo tiene definiciones de funciones, operaciones matemáticas y condiciones. En los archivos que descargaron, se les proporciona el codigo base del **lexer**, **parser** y **AST** para que ustedes puedan agregar el código necesario para implementar las optimizaciones. Esta es la gramatica:

```text
  P -> D P | D
  D -> def id(ARGS) = E;
  ARGS -> id, ARGS | id | lambda
  E -> int | id | if E = E then E else E | E + E | E - E | id(E,...,E)
```

## Constant Folding

Para poder realizar esta fase, ustedes deben recorrer el **AST** de forma similar a la fase de semant, y la de cgen. El archivo que deben modificar es **cool-tree.java**, en la clase **Main**. Allí deben implementar el método llamado **optimize()**, el cual es su punto de inicio. Además de esto son libres de modificar cualquier otra clase que deseen. Hint: otra clase que les puede ser útil es **TreeNode.java**. Una vez implementada la función, si tienen el siguiente fragmento de código:

```text
  def foo() = 2 + 3
```

su programa debera optimizarlo, operando la suma y luego escribiendo una unica constante, de la siguente manera:

```text
  def foo() = 5
```

Tomen en cuenta que para las llamadas a funciones tambien pueden realizar optimizaciones:

```text
  foo(2*x,3*3)
```

lo cual deberian cambiarlo a:

```text
  foo(2*x,9)
```

El output de su código debe ser el **AST** optimizado por lo que las operaciones matemáticas entre constantes deben aparecer como una sola constante \(usted no debe preocuparse por desplegar el **AST**, esto ya lo hace el código que se le proporciona, usted solo debe modificar la estructura del **AST**\).

## 4. Autograder

Para comprobar que su código funciona correctamente, pueden escribir **make check**:

```text
make check 
echo '#!/bin/sh' >> lab11
echo 'java -classpath /usr/class/cs143/cool/lib:.:/usr/java/lib/rt.jar:`dirname $0` Lab11 $*' >> lab11
chmod 755 lab11
python grading.py
##################### Autograder #######################
       +0 (test3.test)
       +0 (test2.test)
       +0 (test5.test)
       +0 (test4.test)
       +0 (test1.test)

+------------------------------------------------------+
|                                            Score: 0/5|
+------------------------------------------------------+
```

Si desea probar únicamente un archivo, encuentra varios programas en la carpeta **grading**. Luego de hacer **make**, ejecute lo siguiente:

```text
./lab11 grading/archivoDePrueba.test
```


