
# Lab 8 \(RISC-V\)

En este laboratorio van a programar en lenguaje ensamblador para practicar y reforzar los conocimientos que adquirieron durante CC3.

Antes de empezar, vamos a obtener los archivos necesarios desde Github Classroom:

```bash
https://classroom.github.com/a/Ye5uh7oc
```

## Jupiter

Jupiter ya viene instalado en el contenedor que estamos usando. Para ejecutar un archivo utilice:

```bash
jupiter archivo.s
```

Nota: cuando queremos ejecutar algún programa de COOL que usamos `jupitercl archivo.s`, para el laboratorio usamos `jupiter archivo.s` sin el `cl`, pronto veremos en clase por qué

## Recordando RISC-V

* Los programas de RISC-V van en un archivo con extension **.s**.
* Los programas deberían de llevar un label global **\_\_start** que se utilizará como punto de inicio.
* Los programas deberían de terminar de la siguiente manera:

```python
li a0, 10
ecall
```

* Las etiquetas o labels terminal con dos puntos.
* Los comentarios comienzan con un numeral o con punto y coma.
* No pueden poner más de una instrucción por línea.

## Instrucciones importantes

Uno de los requisitos de CC4 es dominar los temas de CC3, incluyendo programar en lenguaje ensamblador. RISC-V es una arquitectura RISC por lo cual es muy fácil de utilizar. Algunas instrucciones que deberían conocer hasta el momento son:

```python
# carga el contenido de memoria en la direccion (pc + 8) y lo guarda en t1
lw t1, 8(sp)
# guarda a0 en memoria en la direccion (pc + 8)
sw a0, 8(sp)

# guarda el inmediato 5 en el registro t1
li t1, 5

# guarda la direccion de foo en el registro t1
la t1, foo

# suma los registros t1 y t2 y el resultado lo guarda en t3
add t3, t1, t2
# suma el registro sp con el inmediato 4 y guarda el resultado en sp
addi sp, sp, 4

# salta a la etiqueta label
j label
# salta a la etiqueta label y guarda en el registro ra PC + 4
jal label
# salta a la direccion contenida en el registro ra
jr ra

# si t1 y t2 son iguales, realizar salto hacia foo
beq t1, t2, foo
```

Cuando realizamos llamadas a funciones en assembler debemos ser cuidadosos de no perder las direcciones de retorno. Este y otros datos deben ser guardados en el stack al inicio de la llamada y restaurados cuando esta termina.

El convenio de RISC-V que conocimos en CC3 el siguiente:

* Los registros aX se utilizan como argumentos cuando se manda a llamar a una función.
* El registro a0 se utiliza para devolver resultados.
* Los registros tX se utilizan como temporales, cuyo valor puede perderse entre llamadas.
* Los registros sX sobreviven a llamadas.
* El registro sp es el puntero hacia el stack.
* El registro ra contiene la dirección de retorno \(pc + 4\).

Este convenio cambiará en CC4 pues usaremos un accumulator machine, pero aún nos sirve para este laboratorio.

Veamos un ejemplo sencillo de un ciclo en RISC-V:

```python
.text
.globl __start           # indicamos que __start es global y punto de partida

__start:
    li t0, 0             # i
    li t1, 10            # max
cond:
    bge t0, t1, endLoop  # terminamos si i >= max
body:
    mv a1, t0            # movemos t0 a a1 para imprimirlo
    li a0, 1             # codigo ecall para imprimir un entero
    ecall                # imprimimos i
step:
    addi t0, t0, 1       # step del loop
    j cond               # salto hacia la condicion
endLoop:
    li a0, 10            # codigo ecall para salir de un programa
    ecall                # salimos del programa
```

Veamos ahora un programa con llamadas recursivas:

```python
.data
    msg: .string "El resultado es: "

.text
.globl __start            # indicamos que __start es global y punto de partida

__start:
    li a0, 5              # queremos calcular factorial de 5
    jal factorial         # llamamos a factorial
    mv s0, a0             # guardamos el resultado en s0
    
    la a1, msg            # cargamos la direccion de msg en a1
    li a0, 4              # codigo ecall para imprimir un string
    ecall                 # imprimimos el string
    
    mv a1, s0             # movemos s0 a a1 (que tenia el resultado de factorial)
    li a0, 1              # codigo ecall para imprimir un entero
    ecall                 # imprimimos el entero
    
    li a0, 10             # codigo ecall para salir del programa
    ecall                 # salimos del programa

factorial:
    # a0 trae el resultado
    # ra tiene la direccion de retorno
    # sp apunta hacia el tope del stack
    bne a0, x0, notZero    # si a0 != 0 saltar a notZero
    li a0, 1               # de lo contrario devolver 1
    jr ra                  # saltar a la direccion de retorno

notZero:
    addi sp, sp, -8        # protegemos algunos valores en el stack (2 words)
    sw s0, 0(sp)           # guardamos s0 porque lo vamos a utilizar en la funcion
    sw ra, 4(sp)           # guardamos ra para no perder la direccion de retorno
    
    mv s0, a0              # guardamos a0 en s0 para no perderlo
    
    addi a0, a0, -1        # decrementamos a0 para la siguiente llamada: fact(n - 1)
    jal factorial          # llamamos recursivamente a factorial
    
    mul a0, a0, s0         # efectuamos n * fact(n - 1)
    
    lw s0, 0(sp)           # restauramos s0
    lw ra, 4(sp)           # restauramos ra
    addi sp, sp, 8         # liberamos el espacio
    
    jr ra                  # saltamos a la direccion de retorno
```

Para el laboratorio realizaremos dos ejercicios, ambos utilizan recursión. La recursión es importante en ensamblador pues nos transmite la idea de **al finalizar una tarea, el stack debe quedar tal como llegó originalmente.**

## Ejercicio 1: fibonacci.s

En un archivo llamado **fibonacci.s** implementen la función de fibonacci en lenguaje ensamblador RISC-V.

## Ejercicio 2: hanoi.s

Otra función recursiva, traduzca a RISC-V el siguiente código que sirve para resolver el juego de torres de Hanoi.

```c
void hanoi(int numeroDeDiscos, int T_origen, int T_destino, int T_alterna) {
    if (numerodeDiscos == 1) {
        printf("mueva el disco de la torre: ");
        printf("%i", T_origen);
        printf(" hacia la torre: ");
        printf("%i\n", T_destino);
    } else {
        hanoi(numeroDeDiscos - 1, T_origen, T_alterna, T_destino);
        hanoi(1, T_origen, T_destino, T_alterna);
        hanoi(numeroDeDiscos - 1, T_alterna, T_destino, T_origen);
    }
}
```
