if [ ! -d ../PA1 ] || [ ! -d ../PA2 ]; then
    echo "Recuerde que la estructura de carpetas debe ser PA1, PA2, PA3, PA4"
    exit 1
fi

if [ ! -f pa2-grading.pl ]; then
    wget http://raw.githubusercontent.com/CC-4/cc-4.github.io/master/proyectos/scripts/pa2-grading.pl
fi

chmod +x pa2-grading.pl

# Run grading with pre-compiled phases
make clean
make parser
make
./pa2-grading.pl
INIT_SCORE=$(cat grading/SCORE)

cd ../PA1
make lexer
cd ../PA2

mv lexer lexer.back

cat > lexer <<'EOF'
#!/bin/sh
java -classpath /usr/class/cc4/cool/lib/java-cup-11a.jar:/usr/class/cc4/cool/lib/jlex.jar:$(dirname "$0")/../PA1/:/usr/java/lib/rt.jar:`dirname $0` Lexer "$@"
EOF

chmod +x lexer

./pa2-grading.pl

mv lexer.back lexer

echo "Usando fases compiladas" "$INIT_SCORE"
echo "Usando todas sus fases" "$(cat grading/SCORE)"
