if [ ! -d ../PA1 ] || [ ! -d ../PA2 ] || [ ! -d ../PA3 ]; then
	echo "Recuerde que la estructura de carpetas debe ser PA1, PA2, PA3, PA4";
	exit 1;
fi

if [ ! -f pa4-grading.py ]; then
	wget https://raw.githubusercontent.com/CC-4/cc-4.github.io/master/projects/grading/pa4-grading.py
fi

chmod +x pa4-grading.py

# Run grading with pre-compiled phases
make clean
make cgen
make
./pa4-grading.py
INIT_SCORE=$(cat grading/SCORE)

cd ../PA1
make lexer
cd ../PA4

mv lexer lexer.back
echo '#!/bin/sh' >> lexer
echo 'java -classpath /usr/class/cs143/cool/lib/java-cup-11a.jar:/usr/class/cs143/cool/lib/jlex.jar:'$(pwd)'/../PA1/:/usr/java/lib/rt.jar:`dirname $0` Lexer $*' >> lexer
chmod +x lexer


cd ../PA2
make parser
cd ../PA4

mv parser parser.back
echo '#!/bin/sh' >> parser
echo 'java -classpath /usr/class/cs143/cool/lib:'$(pwd)'/../PA2/:`dirname $0` Parser $*' >> parser
chmod +x parser

cd ../PA3
make semant
cd ../PA4

mv semant semant.back
echo '#!/bin/sh' >> semant
echo 'java -classpath /usr/class/cs143/cool/lib:'$(pwd)'/../PA3/:/usr/java/lib/rt.jar Semant $*' >> semant
chmod +x semant

# Run grading with your phases
./pa4-grading.py

mv lexer.back lexer
mv parser.back parser
mv semant.back semant

echo "Usando fases compiladas" $INIT_SCORE
echo "Usando todas sus fases" $(cat grading/SCORE)
