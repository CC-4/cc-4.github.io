# Instalación Material

Para poder realizar las fases del proyecto del compilador se requiere que utilicen una distribución de Linux por lo que se les proporcionarán dos opciones:

1. Descargar una máquina virtual con el contenido necesario \(opción recomendada\).
2. Instalar una distribución de Linux y descargar e instalar el material en su computadora.

El material que se incluye es el código base que les serivirá para poder comenzar las fases del proyecto, el compilador **coolc-rv** que compilará de COOL hacia RISC-V, y **jupitercl** que nos permitirá usar Jupiter desde la línea de comandos y con un runtime apto para nuestros programas de COOL.

Luego de tener instalado el material deben de crear una carpeta en donde guardarán su código y que contendrá cada una de las fases del proyecto. Esta carpeta se puede llamar como ustedes quieram la carpeta debe contener las siguientes carpetas:

* **PA1** \(Análisis Léxico\)
* **PA2** \(Análisis Sintáctico\)
* **PA3** \(Análisis Semántico\)
* **PA4** \(Generación de Código\)

Cada una de estas carpetas corresponderá a una fase del proyecto. **Utilizaremos scripts automatizados para calificar por lo que si usted no respeta este convenio de nombres de carpetas, puede tener problemas con su nota**.

Después de haber realizado lo anterior, como se les mencionó en clase, el uso de Git será **obligatorio**. Por lo tanto, dentro de la carpeta inicial \(la cual nombraron como ustedes deseaban\), deben iniciar un repositorio de Git.

```bash
git init
```

## Opción 1: Máquina Virtual

La máquina virtual la pueden descargar desde cualquiera de los siguientes enlaces \(ocupa 4.9GB\):

* [Google Drive](https://drive.google.com/file/d/1-OAPQfv1rEqZbJut4Hj29kubMkiwLKp5/view?usp=sharing)
* [Mega](https://mega.nz/file/rRo2ELZK#Ak5sMz0YF_7QwlYuSAr45n9RP3EV1cInf6PehU3Y3Pg)

Es una máquina virtual de **Ubuntu 20.04 LTS** para ejecutarse en VMware, después de descargar el archivo debe descomprimirlo y abrir el archivo .vmx que le permitirá usar la máquina virtual desde su VMware.

!!!info "Contraseña"
	La contraseña de la máquina virtual es **student**

## Opción 2: Instalar el Material

Para esta opción se asume que tienen ya una máquina con Ubuntu 20 instalado. Debe descargar el script correspondiente desde **Material de Apoyo** en su GES. Luego abra una terminal y ejecute lo siguiente:

```bash
chmod +x install_ubuntu20.sh
./install_ubuntu20.sh
```

Esto descargará Java 11, JLex, cup, git, coolc-rv y jupitercl.

