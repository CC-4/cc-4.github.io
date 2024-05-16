import java.io.PrintStream;

public final class SemantErrors {

    // Cuando una clase ha sido declarada previamente
    public static void classPreviouslyDefined(AbstractSymbol name, PrintStream p) {
        p.println("Class " + name + " was previously defined.");
    }

    // Cuando una clase basica es redefinida

    public static void basicClassRedefined(AbstractSymbol name, PrintStream p) {
        p.println("Redefinition of basic class " + name + ".");
    }

    // Cuando el padre de una clase no existe
    public static void inheritsFromAnUndefinedClass(AbstractSymbol name, AbstractSymbol parent, PrintStream p) {
        p.println("Class " + name + " inherits from an undefined class " + parent + ".");
    }

    // Cuando una clase hereda de una clase basica
    public static void cannotInheritClass(AbstractSymbol name, AbstractSymbol parent, PrintStream p) {
        p.println("Class " + name + " cannot inherit class " + parent + ".");
    }

    // Cuando una clase esta en un ciclo de herencia
    public static void inheritanceCycle(AbstractSymbol name, PrintStream p) {
        p.println("Class " + name + ", or an ancestor of " + name + ", is involved in an inheritance cycle.");
    }

    // Cuando un metodo se define varias veces en una clase
    public static void methodMultiplyDefined(AbstractSymbol name, PrintStream p) {
        p.println("Method " + name + " is multiply defined.");
    }

    // Cuando un atributo se define varias veces en una clase
    public static void attrMultiplyDefined(AbstractSymbol name, PrintStream p) {
        p.println("Attribute " + name + " is multiply defined in class.");
    }

    // Cuando un atributo se redefine porque se heredo de alguna clase padre
    public static void attrOfAnInheritedClass(AbstractSymbol name, PrintStream p) {
        p.println("Attribute " + name + " is an attribute of an inherited class.");
    }

    // Cuando la clase main no esta definida
    public static void noClassMain(PrintStream p) {
        p.println("Class Main is not defined.");
    }

    // Cuando no hay un metodo main en la clase Main
    public static void noMainMethodInMainClass(PrintStream p) {
        p.println("No 'main' method in class Main.");
    }

    // Cuando el metodo main de la clase Main no deberia de llevar argumentos
    public static void mainMethodNoArgs(PrintStream p) {
        p.println("'main' method in class Main should have no arguments.");
    }

    // Cuando definen un formal con nombre self
    public static void formalCannotBeSelf(PrintStream p) {
        p.println("'self' cannot be the name of a formal parameter.");
    }

    // Cuando declaran un formal parameter con tipo SELF_TYPE
    public static void formalParamCannotHaveTypeSELF_TYPE(AbstractSymbol name, PrintStream p) {
        p.println("Formal parameter "+name+" cannot have type SELF_TYPE.");
    }

    // Cuando un parametro es definido multiples veces
    public static void formalMultiplyDefined(AbstractSymbol name, PrintStream p) {
        p.println("Formal parameter "+name+" is multiply defined.");
    }

    // Cuando un metodo es heredado tiene diferente numero de formals parameters
    public static void diffNumOfFormalsRedefinedMethod(AbstractSymbol name, PrintStream p) {
        p.println("Incompatible number of formal parameters in redefined method "+name+".");
    }

    // Cuando un metodo es heredado y sus formals tienen diferente tipo al original
    public static void diffTypeFormalRedefinedMethod(AbstractSymbol name, AbstractSymbol t1, AbstractSymbol t2, PrintStream p) {
        p.println("In redefined method "+name+", parameter type "+t1+" is different from original type "+t2);
    }

    // Cuando el tipo declarado de un metodo no es igual al retornado
    public static void diffReturnType(AbstractSymbol type, AbstractSymbol name, AbstractSymbol returnt, PrintStream p) {
        p.println("Inferred return type "+type+" of method "+name+" does not conform to declared return type "+returnt+".");
    }

    // cuando el return type no existe
    public static void undefinedReturnType(AbstractSymbol rt, AbstractSymbol methodName, PrintStream p) {
        p.println("Undefined return type " +rt+" in method " + methodName + ".");
    }

    // Cuando declaran un atributo con nombre self
    public static void selfCannotBeTheNameOfAttr(PrintStream p) {
        p.println("'self' cannot be the name of an attribute.");
    }

    // Cuando se declara un atributo con un tipo no definido
    public static void classOfAttrIsUndefined(AbstractSymbol t, AbstractSymbol name, PrintStream p) {
        p.println("Class "+t+" of attribute "+name+" is undefined.");
    }

    // Cuando se inicializa un atributo con un tipo diferente al declarado
    public static void diffInitType(AbstractSymbol it, AbstractSymbol name, AbstractSymbol decl, PrintStream p) {
        p.println("Inferred type "+it+" of initialization of attribute "+name+" does not conform to declared type "+decl+".");
    }

    // Cuando se le asigna un tipo diferente a una variable
    public static void diffAssignType(AbstractSymbol it, AbstractSymbol decl, AbstractSymbol name, PrintStream p) {
        p.println("Type "+it+" of assigned expression does not conform to declared type "+decl+" of identifier "+name+".");
    }

    // Cuando se trata asignar a una variable no definida
    public static void assignToUndeclaredVariable(AbstractSymbol name, PrintStream p) {
        p.println("Assignment to undeclared variable "+name+".");
    }

    // Cuando se trata de llamar a un staticdispatch con expr diferente tipo
    public static void exprNotConformToDeclaredSDType(AbstractSymbol et, AbstractSymbol sdt, PrintStream p) {
        p.println("Expression type "+et+" does not conform to declared static dispatch type "+sdt+".");
    }

    // Cuando se hace un new con una clase no definida
    public static void newUndefinedClass(AbstractSymbol type_name, PrintStream p) {
        p.println("'new' used with undefined class "+type_name+".");
    }

    // Cuando se llama a un static dispatch no definido
    public static void staticDispatchUndefined(AbstractSymbol name, PrintStream p) {
        p.println("Static dispatch to undefined method "+name+".");
    }

    // Cuando se manda a llamar un metodo con diferente numero de argumentos (Static dispatch)
    public static void methodInvokedWithWrongNumberOfArgs(AbstractSymbol name, PrintStream p) {
        p.println("Method "+name+" invoked with wrong number of arguments");
    }

    // Cuando el argumento enviado a un metodo es diferente tipo al declarado en el formal
    public static void parameterDiffType(AbstractSymbol name, AbstractSymbol pname, AbstractSymbol t1, AbstractSymbol dl, PrintStream p) {
        p.println("In call of method "+name+", type "+t1+" of parameter "+pname+" does not conform to declared type "+dl+".");
    }

    // Cuando se manda a llamar a un metodo no definido
    public static void dispatchToUndefinedMethod(AbstractSymbol name, PrintStream p) {
        p.println("Dispatch to undefined method "+name+".");
    }

    // Cuando se manda a llamar a un metodo con diferente numero de argumentos (dispatch)
    public static void methodCalledWithWrongNbofArgs(AbstractSymbol name, PrintStream p) {
        p.println("Method "+name+" called with wrong number of arguments.");
    }

    // Cuando el if no tiene un predicate bool
    public static void ifNoBoolPredicate(PrintStream p) {
        p.println("Predicate of 'if' does not have type Bool.");
    }

    // Cuando el while no tiene condition de tipo bool
    public static void whileNoBoolCondition(PrintStream p){
        p.println("Loop condition does not have type Bool.");
    }

    // Cuando el tipo de un branch del case se duplica
    public static void duplicateBranch(AbstractSymbol t, PrintStream p) {
        p.println("Duplicate branch "+t+" in case statement.");
    }

    // Cuando una variable en el let se declara con un tipo no definido
    public static void letUndefinedType(AbstractSymbol t, AbstractSymbol id, PrintStream p) {
        p.println("Class "+t+ " of let-bound identifier "+id+" is undefined.");
    }

    // Cuando el nombre de alguna variable declarada en let es llamada self
    public static void selfCannotBeBoundInALet(PrintStream p) {
        p.println("'self' cannot be bound in a 'let' expression.");
    }


    public static void selfCannotAssign(PrintStream p) {
        p.println("'self' cannot be bound in a 'let' expression");
    }

    // Cuando en un let se inicializa una variable con un tipo diferente al declarado
    public static void letDiffInitType(AbstractSymbol t, AbstractSymbol id, AbstractSymbol decl, PrintStream p) {
        p.println("Inferred type "+t+" of initialization of "+id+" does not conform to identifier's declared type "+decl+".");
    }

    // Cuando se manda a llamar una operacion con diferente tipo de int
    public static void noIntArguments(AbstractSymbol t1, AbstractSymbol t2, String op, PrintStream p) {
        p.println("non-Int arguments: "+t1+" "+ op +" "+t2);
    }

    // Cuando el neg tiene un tipo diferente de Int
    public static void argumentOfNegNoIntType(AbstractSymbol t, PrintStream p) {
        p.println("Argument of '~' has type "+t+" instead of Int.");
    }

    // Cuando se compara ilegalmente con tipos basicos
    public static void illegalCompWithABasicType(PrintStream p) {
        p.println("Illegal comparison with a basic type.");
    }

    // Cuando se asigna self
    public static void cannotAssignSelf(PrintStream p) {
        p.println("Cannot assign to 'self'.");
    }

    /**
     * Si un identifier(variable) no se encuentra en la symbol Table
     */
    public static void undeclaredIdentifiers(AbstractSymbol id_name, PrintStream p) {
        p.println("Undeclared identifier " + id_name + ".");
    }

    /**
     * Si se realiza un new pero la clase que se intenta crear no se definio
     */
    public static void newUsedWithUndefinedClass(AbstractSymbol class_name, PrintStream p){
        p.println("'new' used with undefined class " + class_name + ".");
    }

    /**
     * Si el not tiene como parametro algo que no sea Bool
     */
    public static void notNotBoolType(AbstractSymbol invalid_type, PrintStream p) {
        p.println("Argument of 'not' has type " + invalid_type + " instead of Bool.");
    }
}
