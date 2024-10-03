# Análisis Semántico

![](/img/semant.png)

![](/img/dragon_meme.png)

En está asignación, ustedes van a implementar el análisis semántico para COOL. Ustedes van a utilizar el AST construido por el parser para verificar si el programa está correcto semánticamente siguiendo la especificación de COOL. Su analizador semántico debería de rechazar programas erroneos. Para programas que estén correctos, debería reunir información que va a ser utilizada por el generador de código. La salida del analizador semántico va a ser un **AST anotado** utilizado por su generador de código.

En esta asignación tiene más libertad para tomar decisiones de diseño que en las últimas dos asignaciones. Su semantic va a ser correcto si puede verificar programas reflejando la especificación. No hay una sola solución para esta asignación, pero si hay muchas malas maneras de implementarla. Hay un número de practicas estándar que creemos que hacen la vida más fácil, y les vamos a tratar de inculcar estas prácticas a ustedes. Sin embargo, lo que ustedes hagan es su responsabilidad, cualquier cosa que ustedes decidan hacer, estén preparados para justificarla y explicarla.

Ustedes van a necesitar referirse a las reglas de inferencia de tipos descritas en el manual de referencia de COOL. Ustedes también van a necesitar agregar métodos y atributos a los nodos del AST para esta fase. Las funciones que el paquete tree provee están documentadas en el Manual del código de soporte de COOL.

Hay mucha información en este documento y ustedes necesitan saber la mayoría de esta información para crear un analizador semántico funcional. Por favor lean esta asignación detenida y cuidadosamente. A alto nivel, su analizador semántico debería de poder lograr las siguientes tareas:

* Ver todas las clases y construir un grafo de herencia.
* Verificar que el grafo esté bien formado.
* Anotar el AST con tipos.
* Para cada clase:
  * Atravesar el AST, reuniendo todas las declaraciones visibles en una tabla de símbolos.
  * Verificar que los tipos de cada expresión sean correctos.

Esta lista de tareas no es exhaustiva, es responsabilidad de ustedes de implementar la especificación descrita en el manual.

## 1. Archivos y Directorios

Para empezar, creen el directorio **PA3** junto a sus carpetas anteriores.

**ADENTRO** de esta carpeta ejecuten el siguiente comando:
```bash
make -f /usr/class/cc4/assignments/PA3/Makefile
```

Este comando va a copiar un número de archivos en su directorio que han creado. Algunos de los archivos que van a ser copiados van a ser de solo lectura \(representando a estos con archivos que realmente son enlaces simbólicos hacia otros archivos\). Ustedes no deberían de editar estos archivos. De hecho, si ustedes modifican estos archivos, van a encontrar imposible terminar y completar esta asignación. Vean las instrucciones en el archivo README.

Adicionalmente también tienen que bajar el archivo **SemantErrors.java**:

[Descargar SemantErrors](/cc4/SemantErrors.java)

Los únicos archivos que tienen permitido modificar para esta asignación son:

* **cool-tree.java**: Este archivo contiene definiciones de los nodos del AST y es el archivo principal de su implementación. Ustedes van a necesitar agregar código para su analizador semántico en este archivo. El analizador semántico es llamado utilizando el método `semant()` de la clase `programc`. No modifiquen las declaraciones existentes.
* **ClassTable.java**: Esta clase es un placeholder para algunos métodos útiles \(incluyendo reporte de errores e inicialización de las clases básicas\). Pueden utilizar este archivo y mejorarlo para su analizador semántico.
* **TreeConstants.java**: Este archivo define algunos AbstractSymbol útiles.
* **good.cl y bad.cl**: Estos archivos prueban algunas características semánticas de COOL. Ustedes deberían de agregar tests que aseguren que good.cl tome en cuenta combinaciones semánticas legales \(tantas como se puedan\) y en bad.cl lo contrario, combinaciones semánticas ilegales. No es posible tomar en cuenta todas estas combinaciones en un solo archivo, ustedes son responsables de cubrir la mayoría de estas. Expliquen sus pruebas en estos archivos y pongan cualquier comentario en el archivo README.
* **SemantErrors.java**: Este archivo contiene métodos que generan los errores correspondientes para el análisis semántico, pueden agregar otros errores que talvez no están cubiertos en este archivo.
* **README**: Este archivo contiene instrucciones detalladas para la asignación, así como también un número de recomendaciones útiles. Agregue en este archivo cualquier explicación adicional que considere relevante.

## 2. Atravesar el AST

Como resultado de la asignación 2, su parser construye un AST. El método `dump_with_types`, definido en la mayoría de nodos del AST, ilustra como atravesar el AST y reunir información de él. Esto refleja un estilo de algoritmo recursivo para atravesar el árbol. Esto es bastante importante, porque es una manera natural de estructurar varias operaciones en un AST.

Su tarea de programación en esta asignación es, primero atravesar el árbol, segundo manejar varias piezas de información que ustedes reunan del árbol y tercero utilizar esa información para forzar la semántica de COOL. Atravesar el árbol una vez se le suele llamar una "pasada". Ustedes probablemente deban realizar tres pasadas sobre el AST para verificar todo.

Ustedes van a necesitar agregar información adicional en los nodos del AST. Para hacer esto, van a necesitar editar el archivo **cool-tree.java** directamente.

## 3. Herencia

Se recomienda que empiece su semantic construyendo un grafo que muestre las relaciones de herencia entre clases. En este grafo no deberían existir ciclos, pues esto le causaría muchos problemas más adelante cuando necesite obtener los features de alguna clase padre.

Adicionalmente, COOL tiene restricciones en heredar de las clases básicas \(vean el manual\). Es también un error si la clase `A` hereda de la clase `B` pero la clase `B` no está definida.

El esqueleto del proyecto incluye definiciones apropiadas de todas las clases básicas. Ustedes van a necesitar incorporar estas clases en el grafo de herencia. 

Les sugerimos que dividan su análisis semántico en varios pequeños componentes. Comience creando un grafo de herencia y revise que este esté bien definido, si hubiera un error, deténgase y no continúe analizando el resto de los errores. Cuando esta estructura ya esté completa y bien revisada, ya podrá usarla para cualquier otra comprobación que necesite más adelante.

## 4. Scopes y Variables

Una gran porción del análisis semántico es manejar los nombres de variables. El problema en específico es determinar qué declaración está activa para cada uso de una variable o identificador, especialmente cuando un nombre de variable puede ser reutilizado. Por ejemplo, si `i` es declarado en dos expresiones `let`, una anidada dentro de la otra, entonces en cualquier momento que `i` sea referenciado la semántica de COOL especifica que declaración de estas dos está activa. Es trabajo del analizador semántico guardar un registro de que declaración hace referencia a una variable.

Como se discutió en clase, una tabla de símbolos es una estructura de datos conveniente para manejar nombres de variable y scopes. Ustedes pueden utilizar nuestra implementación de una tabla de símbolos para este proyecto. Nuestra implementación provee métodos para entrar, salir y aumentar los scopes como sea posible. Ustedes son libres de implementar su propia tabla de símbolos también, es como ustedes prefieran.

Además del identificador `self`, que está implicitamente definido en cada clase, hay cuatro maneras que un objeto pueda ser introducido en COOL:

1. Definiciones de atributos de clase.
2. Parámetros formales en los métodos.
3. Expresiones let.
4. Los branches de un case.

Adicionalmente a los nombres de variables, hay nombres de métodos y nombres de clases. Es un error utilizar cualquier nombre que no tenga una declaración correspondiente. En este caso, sin embargo, el analizador semántico no debería de abortar la compilación después de descubrir este tipo de errores. Recuerden: ni los métodos, ni las clases, ni los atributos necesitan ser declarados antes de ser utilizados. Por ejemplo: es posible que dentro del método `main` se mande a llamar al método `foo` y este método esté declarado más abajo en el archivo. Piensen cómo esto afecta su análisis semántico.

## 5. Verificación de Tipos

La verificación de tipos es otra función principal del analizador semántico. El analizador semántico tiene que verificar que tipos válidos sean declarados en donde sea requerido. Por ejemplo, los tipos de retorno de los métodos tienen que ser declarados. Utilizando esta información, el analizador semántico tiene que verificar también que la expresión dentro del método tiene un tipo válido de acuerdo a las reglas de inferencia. Las reglas de inferencia están detalladas en el manual de referencia de COOL y también fueron explicadas en clase.

Un problema difícil es qué hacer si una expresión no tiene un tipo válido de acuerdo a las reglas. Primero, un error se debería de imprimir con el número de línea y una descripción de que fue lo que estuvo mal.

!!!info "Errores"
	Utilicen la clase de ayuda **SemantErrors.java** para imprimir los errores necesarios durante el análisis semántico.


Es relativamente fácil dar mensajes de error coherentes, porque generalmente es obvio que error es. Nosotros esperamos que ustedes den mensajes de error informativos de acuerdo a lo que se encuentra en **SemantErrors.java**. Segundo, el analizador semántico tiene que tratar de recuperarse y continuar. Nosotros sí esperamos que su analizador semántico se recupere, pero no esperamos que evite errores en cascada. Un mecanismo de recuperación simple es asignar el tipo `Object` a cualquier expresión que no se le pueda dar un tipo \(nostros utilizamos esto en coolc\).

## 6. Interfaz con Codegen

Para que el analizador semántico funcione correctamente con el resto del compilador de COOL, algunas precauciones tienen que tomarse en cuenta para que la interfaz con el generador de código sea correcta. Nostros hemos adoptado una simple e ingenua interfaz para evitar reducir sus impulsos de creatividad en el análisis semántico. Sin embargo, una cosa más tienen que hacer. Para cada nodo expression, su campo de tipo tiene que ser cambiado al `AbstractSymbol` que fue inferido por su analizador semántico. Este `AbstractSymbol` debería de ser el resultado de utilizar el método `addString` en fases anteriores en la tabla `idtable`. La expresión especial `no_expr` tiene que ser asignada con el tipo `No_type` que es un símbolo predefinido en el esqueleto del proyecto.

## 7. Salida Esperada

Para programas que estén incorrectos semánticamente, la salida de su analizador semántico son mensajes de error. Nosotros esperamos de ustedes que se puedan recuperar de la mayoría de errores exceptuando errores de herencia. También se espera de ustedes que produzcan mensajes de error informativos de acuerdo a **SemantErrors.java** vean este archivo para imprimir los errores. Asumiendo que la herencia está bien formada, el analizador semántico debería de capturar y reportar todos los errores semánticos en el programa.

Para programas que estén correctos semánticamente, la salida es un AST anotado. Ustedes van a ser calificados si su analizador semántico anota correctamente el AST con tipos y cuando funcione correctamente con el generador de código de coolc.

## 8. Probando el Analizador

Van a necesitar un analizador léxico y sintáctico para probar su analizador semántico. Pueden utilizar sus implementaciones a estas fases o utilizar las que nosotros les proveemos. Por defecto, las que nosotros les proveemos son utilizadas, para cambiar este comportamiento tienen que cambiar los archivos `lexer` y `parser` con sus propias implementaciones. De todas maneras el autograder principal de este proyecto utiliza los analizadores del compilador de COOL **coolc**.

Ustedes pueden probar su analizador semántico utilizando `./mysemant`, que es un shell script que "pega" el analizador con las fases anteriores. Noten que `./mysemant` puede tomar una bandera `-s` para depurar el analizador. Utilizar esta bandera hace que la bandera `debug` se cambie a verdadero, esta está definida en el archivo **Flags.java**. Agregar el código que hace la depuración es su responsabilidad. Vean el README para más detalles e información.

Una vez que estén bastante confiados de que su analizador está funcionando correctamente, intenten ejecutar `./mycoolc` para invocar su analizador con todas las fases del compilador. Ustedes deberían de probar este compilador en archivos de entrada buenos y malos, para ver si funciona correctamente. Recuerden, los bugs en el análisis semántico pueden manifestarse en el código generado o solo cuando el programa compilado sea ejecutado con spim.

## 9. Observaciones

El análisis semántico es la fase más larga y compleja hasta el momento del compilador. Nuestra solución es de aproximadamente 1000 líneas de código de Java bien documentado. Ustedes van a encontrar esta asignación fácil si se toman un tiempo para diseñar el verificador de tipos antes de programar. Pregúntense a ustedes mismos lo siguiente:

* ¿Qué requerimientos necesito verificar?
* ¿Cuándo necesito verificar un requerimiento?
* ¿Cuándo la información es requerida para verificar un requerimiento?
* ¿Dónde está la información que necesito para verificar un requerimiento?


## 10. Autograder

Ustedes deberían de descargar el siguiente script en el mismo directorio donde tienen sus archivos de la tercera fase, y volverlo ejecutable:

```bash
wget https://cc-4.github.io/cc4/pa3-grading.pl
chmod +x pa3-grading.pl
```

Luego lo ejecutan usando:

```bash
./pa3-grading.pl
```

Esto califica su parser utilizando el analizador léxico de coolc. 

Si ustedes quieren probar su parser utilizando su fase 1 y 2 descarguen el siguiente archivo, y vuélvanlo ejecutable:

```bash
wget https://cc-4.github.io/cc4/pa3-grading-all.sh
chmod +x pa3-grading-all.sh
```

Luego lo ejecutan usando:

```bash
./pa3-grading-all.sh
```

## Referencias

1. [The Tree Package](https://web.stanford.edu/class/archive/cs/cs143/cs143.1112/javadoc/cool_ast/index.html) - Javadoc del paquete Tree.
2. [The Cool Reference Manual](http://web.stanford.edu/class/cs143/materials/cool-manual.pdf) - Manual de COOL.
3. [Tour of the Cool Support Code](http://web.stanford.edu/class/cs143/materials/cool-tour.pdf) - Manual del Código de Soporte de COOL.
