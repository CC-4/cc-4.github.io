if [ ! -d ../PA1 ] || [ ! -d ../PA2 ]; then
	echo "Recuerde que la estructura de carpetas debe ser PA1, PA2, PA3, PA4";
	exit 1;
fi

if [ ! -f pa3-grading.pl ]; then
	wget http://raw.githubusercontent.com/CC-4/cc-4.github.io/master/proyectos/scripts/pa3-grading.pl
fi 

chmod +x pa3-grading.pl

# Run grading with pre-compiled phases
make clean
make semant
./pa3-grading.pl
INIT_SCORE=$(cat grading/SCORE)

cd ../PA1
make lexer
cd ../PA3

mv lexer lexer.back
echo '#!/bin/sh' >> lexer
echo 'java -classpath /usr/class/cs143/cool/lib/java-cup-11a.jar:/usr/class/cs143/cool/lib/jlex.jar:'$(pwd)'/../PA1/:/usr/java/lib/rt.jar:`dirname $0` Lexer $*' >> lexer
chmod +x lexer


cd ../PA2
make parser
cd ../PA3

mv parser parser.back
echo '#!/bin/sh' >> parser
echo 'java -classpath /usr/class/cs143/cool/lib:'$(pwd)'/../PA2/:`dirname $0` Parser $*' >> parser
chmod +x parser


# Run grading with your phases 
./pa3-grading.pl

mv lexer.back lexer
mv parser.back parser


echo "Usando fases compiladas" $INIT_SCORE
echo "Usando todas sus fases" $(cat grading/SCORE)


