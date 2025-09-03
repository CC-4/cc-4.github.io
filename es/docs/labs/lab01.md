# Lab 1 \(COOL\)

Para este lab, deben hacer los ejercicios que se les pide a continuación, estos ejercicios son bastante básicos y están con el único propósito de que ustedes sepan qué se puede hacer en el lenguaje COOL. Este lab será la única vez en que sea necesario programar en COOL, pero no significa que luego pueda olvidar el lenguaje, ya que para los proyectos es necesario que ustedes sean capaces de crear sus propios archivos de pruebas para probar sus implementaciones.

## Archivos base

Acepte el siguiente repositorio para acceder a los archivos base.

```
https://classroom.github.com/a/RoJ_bFrb
```

## 1. Hola Mundo

Cree un archivo llamado **hello.cl** e imprima "Hola mundo" en consola. Esto ya lo tenemos en algún ejemplo, escríbalo sin consultar esos archivos.

## 2. IO

En un archivo llamado **io.cl**, escriban un programa en COOL que pregunte al usuario su nombre, luego su edad, luego que despliegue el nombre de la persona y cuantos años tendrá al graduarse. \(Sean optimistas, no le sumen tantos años a la edad actual\).

Su programa debe de funcionar de la siguiente manera:

```bash
Ingrese su nombre: Timmy
Ingrese su edad: 19

Timmy tendrá 22 años al graduarse :)
```

## 3. Métodos

En un archivo llamado **methods.cl**, hagan un programa que le pida al usuario un número en grados Celsius y los convierta en Fahrenheit. Ustedes tiene que crear un método llamado `toFahrenheit` que efectúe esta conversión:

```kotlin
toFahrenheit(x: Int): Int {
  // su codigo aqui
};
```

Su programa debe de funcionar de la siguiente manera:

```bash
Ingrese grados Celsius: 32

32 Celsius son 89 Fahrenheit
```

!!!info "Punto flotante"
	COOL no tiene punto flotante, por lo tanto su implementación no tiene que ser exacta. Por ejemplo el usuario ingresa 32 Celsius, su salida puede ser 89 Fahrenheit a pesar de que la respuesta exacta es 89.6


## 4. Ciclos

En un archivo llamado **loop.cl**, escriban un programa que despliegue una tabla de conversion de -50 a 150 grados Celsius hacia grados Fahrenheit en incrementos de 10. Para este ejercico es obligatorio que utilicen un `while`.

Su programa debe de producir el siguiente resultado:

```bash
-50 Celsius son -58 Fahrenheit
-40 Celsius son -40 Fahrenheit
-30 Celsius son -22 Fahrenheit
-20 Celsius son -4 Fahrenheit
-10 Celsius son 14 Fahrenheit
0 Celsius son 32 Fahrenheit
10 Celsius son 50 Fahrenheit
20 Celsius son 68 Fahrenheit
30 Celsius son 86 Fahrenheit
40 Celsius son 104 Fahrenheit
50 Celsius son 122 Fahrenheit
60 Celsius son 140 Fahrenheit
70 Celsius son 158 Fahrenheit
80 Celsius son 176 Fahrenheit
90 Celsius son 194 Fahrenheit
100 Celsius son 212 Fahrenheit
```

## Entrega

Al finalizar realice add + commit + push, coloque el link de su repositorio en la asignación correspondiente del GES.