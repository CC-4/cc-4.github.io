# Generación de código

## 1. Introducción

En esta asignación, ustedes van a implementar un generador de código para COOL. Cuando hayan terminado satisfactoriamente esta parte, ustedes van a tener un compilador de COOL totalmente funcional.

!!!info "Última fase"
	Esperamos que se den cuenta, que al finalizar esta fase, resolvieron un problema de ingeniería sustancialmente complejo. De ahora en adelante cualquier reto en cuestión de diseño e implementación debería de ser por lo menos un poco más fácil.

El generador de código hace uso del Abstract Syntax Tree (AST) construido durante la fase 2 del proyecto (Parser) y anotado en la fase 3 del proyecto (Semantic). Su generador de código debería de producir código de ensamblador, que representa cualquier programa de COOL correcto para la arquitectura RISC-V.

En esta fase (casi) no hay recuperación de errores, en la generación de código ya podemos estar seguros de que cualquier programa erroneo de COOL ya ha sido detectado en las fases anteriores del compilador.

Al igual que la anterior, esta fase tiene bastantes decisiones de diseño que tomar. Nosotros les haremos varias recomendaciones, pero la decisión final de que estructuras utilizar, que algoritmos implementar, etc. es suya. Se recomienda que la estructura de esta fase sea lo más parecida posible a la anterior.

### Generar código válido

En varios momentos quizás mencionemos "generar código válido" o "generar código equivalente". Su código no necesita ser 100% igual al generado por coolc-rv, sin embargo sí debe funcionar igual.

Recuerde que el autograder se basa en prints, entonces al final de cuentas los programas que usted compile con su proyecto deberían imprimir exactamente lo mismo que los compilados con coolc-rv.

## 2. Ambiente

Debe tener instalado coolc-rv y jupitercl, si no los tiene, revise la seccion de Instalacion de Material.

Para comprobar que todo este instalado correctamente, compilen y corran un programa simple de COOL:
```
class Main inherits IO {

  main(): Object {
    out_string("hello world")
  };

};
```

Utilizando coolc-rv compilamos:
```
coolc-rv main.cl
```

Y al ejecutar veremos un resultado como este:
```
jupitercl main.s
hello world
COOL program successfully executed

Jupiter: exit(0)
```

!!!info "jupitercl"
    Noten que se está haciendo uso del comando **jupitercl**, este a diferencia del comando **jupiter** carga automáticamente el runtime system de COOL.

## 3. Archivos y directorios

A diferencia de las fases anteriores no comenzaremos con un make, sino que obtendremos nuestros archivos base desde este repositorio:
```
git clone https://github.com/CC-4/PA4.git
```

Casi todos estos archivos han sido explicados en las fases anteriores del proyecto. Esta es una lista de los archivos que ustedes tal vez quieran modificar.

* **CgenClassTable.java** y **CgenNode.java**: Estos archivos proveen una implementación del grafo de herencia para el generador de código. Ustedes van a tener que completar CgenClassTable para poder construir su generador de código. Ustedes pueden utilizar el código que se les provee o pueden reemplazarlo con el suyo o con el que hicieron en la fase anterior.
* **StringSymbol.java**, **IntSymbol.java**, y **BoolConst.java**: Estos archivos proveen soporte para las constantes de COOL. Ustedes van a tener que completar el método para generar definiciones de estas constantes.
* **cool-tree.java**: Este archivo contiene la definición del los nodos del AST. Ustedes van a tener que añadir rutinas de generación de código code(PrintStream) para todas las expresiones en este archivo. **El generador de código es mandado a llamar, empezando por `code(PrintStream)` que está en la clase program**. Ustedes tal vez quieran añadir más métodos, pero no modifiquen los métodos ya existentes.
* **TreeConstants.java**: Como antes, este archivo define algunas constantes útiles, siéntanse libres de añadir las suyas y lo que quieran.
* **CgenSupport.java**: Este archivo contiene soporte general para generar código. Van a encontrar una serie de funciones que se vuelven bastantes útiles para emitir instrucciones de RISC-V, aquí está todo lo que necesitan. Pueden añadir también las que ustedes quieran, pero no modifiquen nada de lo que ya está definido.
* **example.cl**: Este es su archivo de prueba para ver si lo que genera su generador de código funciona correctamente. Hagan bastantes pruebas con este archivo como lo requieran.

## 4. Diseño

En consideración a su diseño, así a grandes rasgos, su generador de código deberá hacer las siguientes tareas:

* Determinar y emitir código para las constantes globales, como los prototipos de objeto.
* Determinar y emitir código para las tablas globales, como la ` class_nameTab`, la `class_objTab` y las dispatch tables.
* Determinar y emitir código para las inicializaciones de atributos.
* Determinar y emitir código para cada método de cada clase.

Hay varias maneras posibles de implementar un generador de código. Una estrategia razonable es hacer el generador de código en dos pasadas. La primera pasada decide el layout para cada clase, particularmente el offset en donde cada atributo es guardado en un objeto. Utilizando esta información, la segunda pasada recursivamente visita cada feature y genera código de accumulator machine para cada expresión.

Hay un número de cosas que ustedes tienen que mantener en mente mientras diseñan su generador de código:

* Su generador de código tiene que funcionar correctamente con el Runtime de COOL (**runtime.s**) que se le provee.
* Debe entender el convenio utilizado, este se basa en dos principios: 1) Todo espacio que pedimos en el stack debemos devolverlo. 2) Los resultados que obtenemos van quedando en el acumulador (**a0**).

### 4.1. Runtime Error Checking

Al final del manual de COOL existe una lista de errores que pueden terminar un programa antes de lo esperado. De esta lista su generador de código debería de ser capaz de atrapar las primeras tres: **dispatch on void**, **case on void** y **missing branch** e imprimir un mensaje de error antes de abortar. Atrapar errores de **substring out of range** y **heap overflow** es responsabilidad del runtime system que está en runtime.s. Finalmente, la división entre 0 también es su responsabilidad, la maneja con **_div_by_zero**.

### 4.2. Garbage Collection

Cerca del final del curso hablaremos sobre Garbage Collection. Si encuentra este término en algún lugar ignórelo por el momento, probablemente es un remanente de cuando el proyecto se trabajaba en MIPS.

Todo lo necesario para que el Garbage Collector funcione ya va implementado en el runtime que se le provee. Usted solo debe encargarse que sus objetos tengan la forma correcta (tag, size, pointer, attr).

## 5. Probando su Codegen

Van a necesitar un analizador léxico, sintáctico y semántico para probar su generador de código. Pueden utilizar sus implementaciones a estas fases o utilizar las que nosotros les proveemos. Por defecto, las que nosotros les proveemos son utilizadas, para cambiar este comportamiento tienen que cambiar los archivos lexer, parser y semant con sus propias implementaciones.

Ustedes van a tener que correr su generador de código utilizando `./mycoolc`, que es un shell script que pega el generador de código que ustedes implementaron con el resto de fases. Tomen en cuenta que `./mycoolc` puede tomar una bandera `-c` para depurar el generador de código, utilizando esta bandera, se cambia la bandera debug a verdadero, que está en **Flags.java**. Agregar código para producir información de depuración es tarea de ustedes si quieren utilizarlo.

### 5.1. Jupiter

El ejecutable de Jupiter `jupitercl` es el simulador para la arquitectura RISC-V en donde pueden probar su generador de código. Tiene muchas funcionalidades que les permite examinar el estado de la memoria, símbolos globales y locales, registros, etc del programa. Ustedes también pueden poner breakpoints y hacer step de su programa. Para poder utilizar el debugger de Jupiter ustedes pueden hacer lo siguiente:

```
jupitercl -g <archivo.s>
>>> help
Available commands:

[General Commands]
 help/?                - show this help message
 exit/quit/q           - exit the simulator and debugger
 !                     - execute previous command

[Display]
 rvi                   - print all RVI registers
 rvi <reg>             - print RVI register reg
 rvf                   - print all RVF registers
 rvf <reg>             - print RVF register reg
 memory <addr>         - print 12 x 4 cells of memory starting at the given address
 memory <addr> <rows>  - print rows x 4 cells of memory starting at the given address
 globals               - print global symbol table
 locals                - print local symbol tables

[Execution Control]
 step/s                - continue until another instruction reached
 backstep/b            - back to the previous instruction
 continue/c            - continue running
 reset                 - reset

[Breakpoints]
 break <addr>          - set a breakpoint at the given address
 clear                 - delete all breakpoints
 delete <addr>         - delete breakpoint at the given address
 list                  - show defined breakpoints
```

## 6. Autograder

Descargue el autograder en la misma carpeta donde tiene su cuarta fase:

```
wget https://cc-4.github.io/cc4/pa4-grading.py
chmod +x pa4-grading.py
```

Luego ejecute el siguiente comando:
```
./pa4-grading.py
```

Si quiere probar todas sus fases juntas, utilice el siguiente autograder:
```
wget https://cc-4.github.io/cc4/pa4-grading-all.sh
chmod +x pa4-grading-all.sh
```

Luego ejecute el siguiente comando:
```
./pa4-grading-all.sh
```

Si no está conforme con la nota del autograder, es su responsabilidad elaborar y presentar testcases propios al momento de la calificación. Estos testcases deben mostrar de qué es capaz su Codegen. Realizarlos no le garantiza que tendrá la misma nota que alguien a quién el autograder sí le funcionó, pero le ayudará a quitarse el cero.