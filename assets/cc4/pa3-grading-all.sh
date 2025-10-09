#!/bin/bash
# Auto-clean CRLF if needed (so it works with Windows-edited scripts)

# Detect CRLF and convert in-memory
if grep -q $'\r' "$0"; then
    echo "Convirtiendo saltos de línea (CRLF → LF)..."
    exec perl -pe 's/\r$//' "$0" "$@"
fi

set -e  # Stop on first error

# --- Folder check ---
if [ ! -d ../PA1 ] || [ ! -d ../PA2 ]; then
    echo "Recuerde que la estructura de carpetas debe ser PA1, PA2, PA3, PA4"
    exit 1
fi

# --- Download grader if missing ---
if [ ! -f pa3-grading.pl ]; then
    echo "Descargando pa3-grading.pl..."
    wget -q http://raw.githubusercontent.com/CC-4/cc-4.github.io/master/proyectos/scripts/pa3-grading.pl
fi

chmod +x pa3-grading.pl

# --- Run grading with precompiled phases ---
make clean
make semant
./pa3-grading.pl
INIT_SCORE=$(cat grading/SCORE)

# --- Build lexer from PA1 ---
cd ../PA1
make lexer
cd ../PA3

mv lexer lexer.back
cat > lexer <<'EOF'
#!/bin/sh
java -classpath /usr/class/cs143/cool/lib/java-cup-11a.jar:/usr/class/cs143/

