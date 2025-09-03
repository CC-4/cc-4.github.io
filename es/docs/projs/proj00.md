# Instalación de material

Sus laboratorios y proyectos de CC4 se trabajarán en Ubuntu. Todas las entregas se realizarán a través de Github. Contamos con varias opciones para que trabaje:

1. Docker: Se le proveerá un contenedor con todo el material necesario listo para usarse. El staff de la clase dará soporte únicamente para esta opción.
2. Máquina virtual: Tendremos disponible una máquina virtual con todo el material instalado. No se dará soporte a VMware.
3. Nativo: Contamos también con un script para instalar el material necesario en Ubuntu 24.04. No se dará soporte a instalaciones nativas.

El material que se incluye es:

* Código base para empezar sus fases del proyecto.
* Compilador **coolc-rv** para compilar de COOL hacia RISC-V y **jupitercl** para ejectuar Jupiter desde la terminal con un runtime apto para nuestros programas de COOL.
* Herramientas varias como **git**, **make**, **gcc**, **jLex**, **cup** y **Java**.

## Opción 1: Docker y contenedor

Antes de empezar con Docker **lea todas las instrucciones de esta sección**.

#### Instalando Docker

* Paso 1: Descargue e instale Docker Desktop: [Docker Desktop](https://www.docker.com/products/docker-desktop/)

* Paso 2: Al finalizar el instalador le pedirá que reinicie su máquina. Reinicie para poder continuar.

* Paso 3: Después de reiniciar debería abrirse una terminal de Windows y una ventana de Docker Desktop. Si alguna de estas no se abre, ejecute manualmente Docker Desktop.

!!!warning "Terminal de Windows"
	No haga nada aún en Docker Desktop. Vaya primero a la terminal de Windows.

* Paso 4: La terminal le dará las instrucciones para instalar o activar WSL. Siga esas instrucciones, por lo general solo le pedirá que presione alguna tecla para continuar y espere mientras se descarga, instala y activa.

!!!warning "Terminal de Windows"
	Continue en Docker Desktop solo si ya tiene listo WSL.

* Paso 5: Docker Desktop le pedirá que cree una cuenta, puede usar cualquier correo para esto.

* Paso 6: Listo. Docker ya está instalado y listo para funcionar.

#### Usando Docker

* Paso 0: Instale Git en Windows si aún no lo tiene. [Git para Windows](https://git-scm.com/downloads/win)

!!!info "Git vs Github"
	Se le pide instalar Git. Git es una herramienta de código abierto que usaremos desde la terminal.

	Github también ofrece una herramienta llamada Github Desktop. En clase **no** usaremos esta herramienta, todo lo trabajamos desde la terminal. 

* Paso 1: Use su terminal de Windows para clonar el siguiente repositorio:

```
git clone https://github.com/cc-4/container
```

* Paso 2: Explore el repositorio recién clonado:

```
workspace		Carpeta que contiene un README.
			Dentro de esta carpeta quedarán todos sus archivos
			como laboratorios, proyectos, etc.
			Usted puede acceder a esta carpeta tanto
			desde Windows como desde Linux.

Dockerfile		Archivo de texto con las instrucciones para
			que Docker cree su contenedor.

create.bat		Archivo ejecutable para que Docker cree su
			contenedor siguiendo las instrucciones del
			Dockerfile.
			Si hace doble clic, este se ejecuta en la terminal
			de Windows.

run.bat			Archivo ejecutable para que su contenedor
			se ejecute.
			Si hace doble clic, abre una terminal de Windows
			que está "conectada" con su contenedor.

stop.bat		Archivo ejecutable para detener su contenedor.
			Cuando termine de trabajar use este archivo para
			detener el contenedor y que este no ocupe recursos
			de su máquina.
```

* Paso 3: Asegúrese que Docker Desktop está corriendo en su máquina. Ejecute **create.bat**. Esto de forma automática descargará e instalará todo el material dentro de un **contenedor**.

!!!warning "Cree el contenedor una sola vez"
	**Solo necesita crear el contenedor una vez.**
	
	Bueno... si sufre algún problema mayor, la solución más sencilla será eliminar el contenedor anterior y crearlo de nuevo.

* Paso 4: Después de unos minutos de espera, su contenedor debería ya estar listo. Notará que la terminal no muestra su usuario de Windows sino muestra **root**. Cuando vea esto sabrá que está dentro del contenedor, es decir, está trabajando en Linux.

* Paso 5: Si se le cierra esa terminal, o la próxima vez que vaya a trabajar, use **run.bat**. Este automáticamente detecta si el contenedor ya está iniciado y abre una terminal conectada a este.

* Paso 6: Cuando termine de trabajar puede usar **stop.bat** para detener su contenedor. Se recomienda siempre hacerlo para ahorrar recursos, sin embargo no hay mayor problema si se le olvida.

* Paso 7: Listo. Por el momento ya sabe lo necesario para empezar a trabajar en Docker. Más adelante en el semestre veremos un poco más sobre contenedores.

## Opción 2: Máquina Virtual

La máquina virtual la pueden descargar desde cualquiera de los siguientes enlaces.

* [Máquina virtual 2025 - Google Drive](https://drive.google.com/file/d/1zeJGlwkdhdKOuJ2u2mb9ACZeJmenn4qo/view?usp=drive_link)

Es una máquina virtual de **Ubuntu 24.04 LTS** para ejecutarse en VMware Workstation Pro, después de descargar el archivo debe descomprimirlo y abrir el archivo **.vmx** que le permitirá usar la máquina virtual desde su VMware.

Utilice VMware Workstation Pro, no utilice la versión Player. Si no lo tiene puede descargarlo en el siguiente enlace o buscarlo en el sitio de Broadcom.

* [VMware Workstation Pro - Google Drive](https://drive.google.com/file/d/1FOlcddkuRQk5Wg_6L7Zvl6kjoqHrqppg/view?usp=sharing)
* [VMware Workstation Pro - Broadcom](https://knowledge.broadcom.com/external/article/344595/downloading-and-installing-vmware-workst.html)

!!!info "Contraseña"
	La contraseña de la máquina virtual es **student**

!!!warning "Soporte"
	**La herramienta oficial para CC4 es Docker.**

	No daremos soporte de forma oficial a la máquina virtual. Si nos pregunta alguna duda intentaremos resolverla, pero si el problema persiste le pediremos que se cambie a Docker.

## Opción 3: Nativo

Para esta opción se asume que tienen ya una máquina con Ubuntu 24.04 LTS instalado. Debe descargar el script correspondiente desde **Material de Apoyo** en su GES. Luego abra una terminal y ejecute lo siguiente:

```bash
chmod u+x install24.sh
./install24.sh
```

Esto descargará e instalará todas las herramientas a su máquina.

!!!warning "Soporte"
	**La herramienta oficial para CC4 es Docker.**

	No daremos soporte de forma oficial a instalación nativa. Si nos pregunta alguna duda intentaremos resolverla, pero si el problema persiste le pediremos que se cambie a Docker.

## Estructura del proyecto

Su proyecto consta de cuatro fases, cada una se realizará dentro de su propia carpeta.

```
PA1			Análisis léxico (lexer)
PA2			Análisis sintáctico (parser)
PA3			Análisis semántico (semant)
PA4			Generación de código (codegen)
```

Estas cuatro carpetas se encontrarán dentro de la carpeta de su repositorio de proyecto. En clases se les dará instrucciones para crear los grupos y obtener esta carpeta.

Si usted está trabajando en Docker, la carpeta de su proyecto con las subcarpetas PA1...PA4 quedarán dentro de **workspace**.