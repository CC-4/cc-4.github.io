# Lab 0 \(Java\)

En este laboratorio practicaremos estructuras de datos en Java. Este laboratorio será revisado manualmente para ver que tal programa, por favor agregue suficientes comentarios para que su código sea fácil de entender.

## Archivos base

Acepte el siguiente repositorio para acceder a los archivos base.

```
https://classroom.github.com/a/3ck4n3fG
```

## 1. Categories

Implemente en Java la clase **Categories** según la siguiente descripción.

#### Campos
- Una hashtable llamada **three**. Esta tabla se inicializa vacía cuando llamamos al constructor de la clase, y es encapsulada. La tabla contendrá listas encadenadas que a su vez contendrán cadenas de caracteres.
- Un entero llamado **counter**. Nos indicará cuántos elementos se han logrado guardar en la tabla.

#### Métodos
- Un constructor que no recibe argumentos. Se encarga de inicializar a **three** como una tabla vacía y a **counter** como cero.
- **void classify(String input)** recibe una cadena de caracteres, y utilizando expresiones regulares de Java, la identificará en una de las siguientes categorías

    - Correo
        - luiscu@galileo.edu
        - aiken@cs.stanford.edu
        - sysadmin@banco.com.gt
    - Teléfono
        - +502 5585 2133
        - 5979-5503
    - Fecha
        - 27/03/1993
        - 09-07-2024

    Considere únicamente los formatos mostrados en los ejemplos anteriores, no se complique con formatos adicionales. Tras identificar a qué categoría perteneces la cadena, debe guardarla en el lugar correspondiente de la tabla **three** y actualizar el contador.

    La tabla **three** tendrá únicamente tres posibles llaves: **mail**, **phone** y **date**. Cada una de estas llaves nos servirá para identificar una lista encadenada donde guardaremos las cadenas. Si es la primera vez que se está ingresando un dato de cierto tipo, en ese momento se debe crear la lista e ingresar el dato. Si la lista ya existe, el nuevo dato se agrega al final de esta.

    Si el dato ingresado no corresponde a alguno de los tres tipos, ignórelo.

- **void printMails()** imprime en pantalla de forma ordenada las direcciones de correo almacenadas en la tabla. Si no hay ninguna guardada, imprime "NO MAILS".

- **void printPhones()** imprime en pantalla de forma ordenada los números telefónicos guardados en la tabla. Si no hay ninguno guardado, imprime "NO PHONES".

- **void printDates()** imprime a pantalla de forma ordenada las fechas contenidas en la tabla. Si no hay ninguna guardada, imprime "NO DATES".

- **void countByCategory()** imprime a pantalla de forma ordenada cuántos datos de cada tipo están guardados (no los valores).

Al completar la clase anterior, implemente la clase **CategoryUserInterface**.

Esta clase tiene un main el cuál usará para probar la clase anterior. En el main hay un ciclo que le pide al usuario ingresar una cadena, esta será enviada al método **classify** de la clase anterior para que sea guardado en la tabla. Tras ingresar exitosamente ocho datos a la tabla, use **countByCategory** para desplegar cuántos se ingresaron de cada tipo. Luego use los prints que se crearon para desplegar el contenido de la tabla de forma ordenada.

Podemos visualizar la tabla de esta manera:

![](/img/tabla0.png)

## 2. BinaryTree

Implemente la clase **BinaryTree** que represente un árbol binario, implemente también cualquier otra clase que pueda necesitar para el árbol. Este árbol debe ser capaz de guardar enteros.

Agregue los métodos necesarios para poder agregar elementos al árbol, y desde su clase **BinaryTreeMain** construya el siguiente árbol:

![](/img/arbol0.png)

Agregue un método para que su árbol se pueda desplegar de forma de recursiva, y que al hacerlo los números queden en el siguiente orden:

31, 44, 47, 48, 64, 65, 67, 74, 76, 78

## Entrega

Haga add + commit + push de sus archivos. Envíe en el GES el link de su repositorio.