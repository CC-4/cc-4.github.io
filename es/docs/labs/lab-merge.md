# Extra: Merge conflict

Esta asignación debe trabajarse en parejas. Si está trabajando el proyecto en pareja, trabaje con la misma persona. Si está trabajando individual, busque una pareja para esta asignación.

## Merge conflict

Un merge conflict ocurre cuando dos personas modifican exactamente las mismas líneas de un archivo al mismo tiempo y luego intentan unir sus cambios. Git es excelente combinando código automáticamente, pero cuando detecta dos versiones diferentes para las mismas líneas Git no intentará adivinar cuál es la versión correcta. En cambio detiene el proceso, marca claramente donde ocurrió el conflicto y le pide al programador que tome la decisión de con cuál versión quedarse.

## Pasos

Lea las instrucciones completas, en especial la sección **Entregable** para saber qué información debería recopilar en cada paso.

Puede utilizar IA para investigar lo que necesite para llevar a cabo estos pasos. Si lo hace, utilice la IA como chat (no como agente), usted debe ser quien ejecute los comandos necesarios en su terminal.

- Cree un repo nuevo, por favor no cause el merge conflict en su repo de proyecto. Incluya el link de este repo en su entregable.
- En su repo vaya a **Settings > Collaborators** y agregue al usuario **cc4-grades** y a su compañero de equipo.
- Coloquen algo de contenido inicial en el repo, puede ser algo muy sencillo como un Hola Mundo en su lenguaje favorito o algún ejercicio de algún laboratorio pasado.
- La primera vez que haga un pull conflictivo, git le mostrará tres opciones: `pull.rebase false`, `pull.rebase true`, `pull.ff only`. Cuando estas aparezcan, seleccione `pull.rebase false` para trabajar esta actividad, al terminar puede cambiarse a la que considere mejor.
- Investigue y explique por qué `pull.rebase false` es la opción por defecto. Explique en qué casos las otras opciones pueden resultar más útiles.
- Investigue qué secuencia de pushes debe realizar para causar un merge conflict. Cáuselo y muéstrelo.
- En específico debe encontrar los marcadores `<<<<<<< HEAD`, `=======`, `>>>>>>>` en su código. Muestre y explique qué es lo que aparece señalado por estos.
- Investigue cómo limpiar el archivo. Elija uno de los posibles fragmentos de código y quédese con este.
- Finalice haciendo `add`, `commit`, `push` tras haber arreglado el problema.

## Entregable

Muestre la ejecución de sus pasos y las explicaciones solicitadas en un video o un PDF. Recuerde incluir el link de su repo en su entregable.

Si elige realizar video tendrá que alternar entre dos máquinas (una por usuario de Github) al momento de mostrar cómo causa el merge conflict. En su video ambas personas participarán en narrar qué pasos están realizando, así como narrar las explicaciones solicitadas.

Si elige realizar PDF tendrá que colocar capturas de pantalla suficientes para mostrar todos los pasos que llevó a cabo, algunos pasos podrían necesitar varias capturas. En su documento deberá redactar las explicaciones solicitadas.

Puede utilizar IA para investigar lo solicitado, sin embargo las explicaciones deben ser con sus propias palabras sin importar si es video o PDF.

Al finalizar suba su entregable al GES.