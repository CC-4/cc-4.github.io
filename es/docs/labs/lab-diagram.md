# Extra: AST de forma visual

Esta asignación puede trabajarse con su pareja de proyecto.

## Diagrama para nuestro AST

Tanto en clase como en una asignación anterior hemos dibujados árboles que representan nuestros programas de COOL, ahora queremos automatizar este proceso.

## Ejemplo

Para el siguiente programa:

```
class Gato {
	nombre : String <- "Cheddy";
	color : String <- "naranja";

	comer (c : Comida) : Int {
		let i : Int <- 0 in 
			while i < 5 loop {
				(new IO).out_string("om nom nom");
				i <- i + 1;
			}
	};
};
```

Nosotros queremos generar un árbol como el siguiente:

![](/img/arbol.png)

Recuerde que en clase hemos mencionado que este árbol puede tener pequeñas variaciones, según cómo usted considere conveniente mostrar la información.

## Pasos

Lea las instrucciones completas, en especial la sección **Entregable** para saber cómo implementar este proyecto.

Puede utilizar IA para investigar lo que necesite. Puede utilizar IA para generar código. Usted es el responsable de todas las decisiones tomadas en el proyecto y debe poder justificarlas en una calificación personal. Guarde todos los prompts y archivos adicionales usados (`AGENTS.md`, skills, etc.) y utilícelos durante su calificación personal.

- Cree un branch nuevo en el repo de su proyecto, por favor no realice esta asignación sobre el branch principal.
- Decida y documente: ¿Sobre qué fase implementará la generación de diagramas? Podría ser sobre su Parser o su Semantic; debe justificar su decisión.
- Decida y documente: ¿Qué tipo de diagramas generará? Podría ser Graphviz (DOT), Mermaid, etc.; debe justificar su decisión.
- Decida y documente: ¿Cómo le indicaremos al proyecto que debe generar diagramas? Podría modificar el Makefile, crear un script adicional, etc.; debe justificar su decisión.
- Implemente la generación de diagramas.
- Documente de forma breve y clara cómo se debe probar el proyecto.

## Entregable

Suba al GES un ZIP que incluya lo siguiente:

- Link de su repositorio, indique también en qué branch trabajó.
- Documentación completa con todas las decisiones que tomó que considere relevante.
- Un `README.md` simple con la información necesaria para ejecutar su proyecto, incluyendo instrucciones de instalación y de ejecución.
- Algún programa `ejemplo.cl` que se pueda usar para probar su implementación.
