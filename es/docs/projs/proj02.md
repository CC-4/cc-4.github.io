# Análisis Sintáctico

![](/img/parser.png)

En esta asignación ustedes van a escribir un parser para COOL. La asignación hace uso de dos herramientas: el generador de parser cup y el paquete de Java con las clases que representan los nodos de un árbol sintáctico. La salida de su parser va a ser un árbol sintáctico abstracto o AST por sus siglas en inglés. Van a construir un AST utilizando acciones semánticas del generador de parser cup.

Inicien esta fase consultando la estructura sintáctica de COOL en la figura 1 del manual. Consulte también la documentación de cup y del paquete tree, en los links al final de la página. La documentación del paquete tree le servirá durante el resto del proyecto.

Por favor lean este documento detenida y cuidadosamente, poniéndole mucha atención a los detalles.

## 1. Archivos y Directorios

Para empezar, creen el directorio **PA2** tal como se explicó al inicio.

**ADENTRO** de esa carpeta ejecuten el siguiente comando:

```bash
make -f /usr/class/cc4/assignments/PA2/Makefile
```

Este comando va a copiar bastantes archivos en su directorio. Algunos de los archivos que van a ser copiados van a ser de solo lectura \(representando a estos con archivos que realmente son enlaces simbólicos hacia otros archivos\). Ustedes no deberían de editar estos archivos. De hecho, si ustedes modifican estos archivos, van a encontrar imposible terminar y completar esta asignación. Vean las instrucciones en el archivo README. Los únicos archivos que tienen permitido modificar para esta asignación son:

* **cool.cup**: Este archivo contiene un esqueleto que describe un parser para COOL. La sección de declaraciones está casi completa, pero van a necesitar agregar alguna que otra declaración para definir nuevos **no terminales**. Nosotros les hemos dado ya los nombres y tipos de declaración para los terminales de la gramática. Ustedes tienen que agregar declaraciones de precedencia también. La sección de reglas, sin embargo, está incompleta. Les hemos proveído algunas partes para algunas reglas, pero estas son solo un ejemplo. Piense bien qué reglas necesitará.
* **good.cl y bad.cl**: Estos archivos prueban algunas características de la gramática de COOL. Pueden modificar estos archivos como ustedes quieran para probar su parser.
* **README**: Este archivo contiene instrucciones detalladas para la asignación, así como también un número de recomendaciones útiles. Realmente este archivo no lo van a modificar, pero es bueno mencionar que lo tienen que leer.

!!!warning "Otros archivos"
	No modifiquen ningún archivo que no se menciona en el listado, cuando se evalue la asignación únicamente se va a probar el archivo **cool.cup** en un nuevo entorno.

## 2. Probando el Parser

Ustedes van a necesitar un lexer completamente funcional para probar el parser. Pueden utilizar su propio analizador léxico del proyecto pasado o utilizar el lexer de coolc-rv. Por default, el lexer de coolc-rv es utilizado, para cambiar este comportamiento, cambien el archivo ejecutable **lexer** \(que es un enlace simbólico en su directorio de proyecto\) con su propio lexer. No asuman automáticamente que el lexer que utilicen está libre de errores. Algunos bugs latentes en el analizador léxico pueden generar problemas misteriosos en el parser.

Ustedes van a correr su parser utilizando `./myparser`, un shell script que pega el parser con un analizador léxico \(el de su elección\). Noten que `./myparser` puede recibir una bandera **-p** para depurar el parser. Utilizar esta bandera causa que un montón de información de lo que el parser está haciendo sea impreso en la terminal. cup produce tablas de parseo de una gramática LALR\(1\) bastante leíbles en un archivo llamado **cool.output**. Examinar este archivo a veces puede ser útil para depurar la definición del parser.

Ustedes deberían de probar este parser tanto en archivos bien definidos de COOL, como en malos, para ver si todo está funcionando correctamente. Recuerden, los bugs en su parser se pueden manifestar en alguna otra parte más adelante. Su parser va a ser calificado utilizando nuestro analizador léxico, entonces si ustedes escogen utilizar unicamente su parser, sepan de antemano que esto está sucediendo en el autograder.

!!!info "Lexer"
  Al inicio probaremos nuestra fase 2 usando el analizador léxico de coolc-rv que viene por defecto, ya que este está menos propenso a errores. Cuando ya tengan un parser funcional utilicen su propio analizador léxico para verificar que todo siga funcionando bien.

## 3. Salida del Parser

Sus acciones semánticas deberían de construir un AST. La raíz \(y solamente la raíz\) del AST debería de ser de tipo `programc`. Para los programas que son parseados satisfactoriamente, la salida del parser es un listado del AST.

Para programas que contengan errores \(léxicos o sintácticos\), la salida son mensajes de error del parser. Nosotros les hemos proveído con una función que reporta errores imprimiendo los mensajes en un formato estándar, por favor **NO** modifiquen esto. Ustedes no deberían de invocar esta función directamente en las acciones semánticas, cup automáticamente invoca a esta función cuando un error es detectado.

Para algunas construcciones que puedan abarcar varias líneas de código, por ejemplo:

```python
foo(
  1,
  2,
  3
)
```

Este dispatch abarca 5 líneas, de la 1 a la 5. Ustedes cuando construyan algún nodo que abarque múltiples líneas son libres de indicar a que número de línea pertence este nodo, siempre y cuando, el número esté en el rango que abarque el nodo, en el ejemplo anterior podría ser 1, 2, 3, 4 o 5. No se preocupen si las líneas reportadas por su parser no hacen match exactamente como el compilador de referencia coolc-rv. Su parser solo debería de funcionar para programas que estén contenidos en un solo archivo. No se preocupen por compilar múltiples archivos.

!!!info "Número de línea"
	Siempre que reporten el número de línea utilicen la función de ayuda `curr_lineno()` que se encuentra en **cool.cup**.

## 4. Manejo de Errores

Ustedes deberían de utilizar el pseudo no terminal **error** (en minúscula, el que ignoramos durante lexer) para manejar errores en el parser. El propósito de **error** es permitirle al parser continuar después de un error anticipado. No es una solución garantizada y el parser puede volverse completamente confuso. Vean la documentación de cup para saber como utilizar **error** de forma eficiente. Para recibir toda la nota, su parser debería de recuperarse por lo menos en las siguientes situaciones:

* Si hay algún error en una definición de clase pero la clase es terminada correctamente y la siguiente clase está correcta sintácticamente, el parser debería de ser capaz de empezar de nuevo en la siguiente definición de clase.
* El parser debería de recuperarse de errores en los features \(debe irse a analizar el siguiente feature\).
* En un let, debe pasar a la siguiente variable.
* En un bloque, debe ir a la siguiente expresión.

No se preocupen demasiado por los números de línea que aparecen en los mensajes de error que su parser genera. Si su parser está funcionando correctamente, el número de línea generalmente va a ser la línea donde se encontró el error. Para construcciones erroneas que abarquen múltiples líneas, el número de línea por lo general va a ser la última línea de esa construcción.

## 5. Observaciones

Ustedes van a necesitar declaraciones de precedencia, pero solo para las expresiones. No las utilicen para resolver otro tipo de problemas.

El let de COOL se maneja de forma especial, revise la documentación del tree y notará que cada let puede tener únicamente una variable asociada a este. Deberá escribir sus reglas de tal forma que los let queden anidados para que su parser funcione.

Cuando esté listo para probar el parser usando el autograder, quite cualquier print extra que haya agregado. De igual forma, cualquier print extra o cualquier caracter extraño puede causar problemas en fases siguientes.

## 6. Notas de Java

Ustedes deberían de declarar tipos para sus no terminales y los terminales que tengan valor, por ejemplo, en el archivo **cool.cup** está la declaración:

```java
nonterminal programc program;
```

Esta declaración dice que el no terminal `program` tiene tipo `programc`.

Es crítico que ustedes declaren el tipo correcto para los atributos de los símbolos de la gramática.Si no lo hace, su parser no funcionará correctamente. Note que también hay varios elementos que no llevan tipo.

Es probable que **javac** se queje si utilizan los constructores de los nodos del árbol con el tipo incorrecto. Si ustedes hacen algún casting para resolver esto por fuerza bruta, es probable que su parser sí compile, pero lance una excepción al ejecutarse.

## 7. Autograder

Ustedes deberían de descargar el siguiente script en el mismo directorio donde tienen sus archivos de la segunda fase, y darle permisos.

```bash
wget https://raw.githubusercontent.com/CC-4/cc-4.github.io/master/projects/grading/pa2-grading.pl
chmod +x pa2-grading.pl
```

Lo ejecutan de la siguiente manera:

```bash
./pa2-grading.pl
```

Esto califica su parser utilizando el analizador léxico de coolc. Si ustedes quieren probar su parser junto con su propia fase 1:


```bash
wget https://raw.githubusercontent.com/CC-4/cc-4.github.io/master/projects/grading/pa2-grading-all.sh
chmod +x pa2-grading-all.sh
```

Lo ejecutan de la siguiente manera:

```bash
./pa2-grading-all.sh
```

## Referencias

1. [The Tree Package](http://web.stanford.edu/class/cs143/javadoc/cool_ast/) - Javadoc del paquete Tree.
2. [cup Manual](http://www2.cs.tum.edu/projects/cup/manual.html) - Manual de JCup.
3. [The Cool Reference Manual](http://web.stanford.edu/class/cs143/materials/cool-manual.pdf) - Manual de COOL.
4. [Tour of the Cool Support Code](http://web.stanford.edu/class/cs143/materials/cool-tour.pdf) - Manual del Código de Soporte de COOL.
5. [cup Javadoc](http://web.stanford.edu/class/cs143/javadoc/java_cup/index.html) - Javadoc de JCup.

