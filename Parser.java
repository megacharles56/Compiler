import java.util.List;

public class Parser {

    private final List<Token> tokens;

    private final Token identificador = new Token(TipoToken.ID, "");
    private final Token cadena = new Token(TipoToken.CADENA, "");
    private final Token numero = new Token(TipoToken.NUMERO, "");
    private final Token parentesisIzq = new Token(TipoToken.PARENTESIS_IZQ, "(");
    private final Token parentesisDer = new Token(TipoToken.PARENTESIS_DER, ")");
    private final Token corcheteIzq = new Token(TipoToken.CORCHETE_IZQ, "{");
    private final Token corcheteDer = new Token(TipoToken.CORCHETE_DER, "}");
    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token puntoYComa = new Token(TipoToken.PUNTO_Y_COMA, ";");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token asterisco = new Token(TipoToken.ASTERISCO, "*");
    private final Token menos = new Token(TipoToken.MENOS, "-");
    private final Token mas = new Token(TipoToken.MAS, "+");
    private final Token diagonal = new Token(TipoToken.DIAGONAL, "/");
    private final Token exclamacion = new Token(TipoToken.EXCLAMACION, "!");
    private final Token menor = new Token(TipoToken.MENOR, "<");
    private final Token mayor = new Token(TipoToken.MAYOR, ">");
    private final Token igual = new Token(TipoToken.IGUAL, "=");
    private final Token comparacion = new Token(TipoToken.COMPARACION, "==");
    private final Token menorIgual = new Token(TipoToken.MENOR_IGUAL, "<=");
    private final Token mayorIgual = new Token(TipoToken.MAYOR_IGUAL, ">=");
    private final Token diferente = new Token(TipoToken.DIFERENTE, "!=");
    private final Token y = new Token(TipoToken.Y, "and");
    private final Token clase = new Token(TipoToken.CLASE, "class");
    private final Token ademas = new Token(TipoToken.ADEMAS, "also");
    private final Token falso = new Token(TipoToken.FALSO, "false");
    private final Token para = new Token(TipoToken.PARA, "for");
    private final Token fun = new Token(TipoToken.FUN, "fun");
    private final Token si = new Token(TipoToken.SI, "if");
    private final Token nulo = new Token(TipoToken.NULO, "null");
    private final Token o = new Token(TipoToken.O, "or");
    private final Token imprimir = new Token(TipoToken.IMPRIMIR, "print");
    private final Token retornar = new Token(TipoToken.RETORNAR, "return");
    private final Token supert = new Token(TipoToken.SUPER, "super");
    private final Token este = new Token(TipoToken.ESTE, "this");
    private final Token verdadero = new Token(TipoToken.VERDADERO, "true");
    private final Token var = new Token(TipoToken.VAR, "var");
    private final Token mientras = new Token(TipoToken.MIENTRAS, "while");
    private final Token finCadena = new Token(TipoToken.EOF, "");

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        PROGRAM();

        if(!hayErrores && !preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.linea + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            System.out.println("Consulta válida");
        }

        /*if(!preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }else if(!hayErrores){
            System.out.println("Consulta válida");
        }*/
    }
    void PROGRAM(){
        DECLARATION();
    }
    void DECLARATION(){
        if(hayErrores) return;

        if(preanalisis.equals(clase)){
            CLASS_DECL();
        }else if(preanalisis.equals(fun)){
            FUN_DECL();
        }else if(preanalisis.equals(var)){
            VAR_DECL();
        }else if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
                || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
                || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)
                || preanalisis.equals(para) || preanalisis.equals(si) || preanalisis.equals(imprimir) || preanalisis.equals(retornar) || preanalisis.equals(mientras) || preanalisis.equals(corcheteIzq)){
            STATEMENT();
        }
    }
    
    void CLASS_DECL(){
        if(hayErrores) return;
        
        if(preanalisis.equals(clase)){
            coincidir(clase);
            coincidir(identificador);
            CLASS_INHER();
            coincidir(corcheteIzq);
            FUNCTIONS();
            coincidir(corcheteDer);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba CLASS.");
        }
    }

    void CLASS_INHER(){
        if(hayErrores) return;
        
        if(preanalisis.equals(menor)){
            coincidir(menor);
            coincidir(identificador);
        }
    }

    void FUN_DECL(){
        if(hayErrores) return;
        
        if(preanalisis.equals(fun)){
            coincidir(fun);
            FUNCTION();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba FUN.");
        }
    }

    void VAR_DECL(){
        if(hayErrores) return;
        
        if(preanalisis.equals(var)){
            coincidir(var);
            coincidir(identificador);
            VAR_INIT();
            coincidir(puntoYComa);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba VAR.");
        }
    }

    void VAR_INIT(){
        if(hayErrores) return;
        
        if(preanalisis.equals(igual)){
            coincidir(igual);
            EXPRESSION();
        }
    }

    void STATEMENT(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EXPR_STMT();
        }else if(preanalisis.equals(para)){
            FOR_STMT();
        }else if(preanalisis.equals(si)){
            IF_STMT();
        }else if(preanalisis.equals(imprimir)){
            PRINT_STMT();
        }else if(preanalisis.equals(retornar)){
            RETURN_STMT();
        }else if(preanalisis.equals(mientras)){
            WHILE_STMT();
        }else if(preanalisis.equals(corcheteIzq)){
            BLOCK();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un !, -, (, {, SUPER, FALSE, TRUE, NULL, THIS, FOR, IF, PRINT, RETURN, WHILE, un numero, una cadena o un identificador.");
        }
    }

    void EXPR_STMT(){
        if(hayErrores) return;
        
        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EXPRESSION();
            coincidir(puntoYComa);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void FOR_STMT(){
        if(hayErrores) return;
        
        if(preanalisis.equals(para)){
            coincidir(para);
            coincidir(parentesisIzq);
            FOR_STMT1();
            FOR_STMT2();
            FOR_STMT3();
            coincidir(parentesisDer);
            STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba FOR.");
        }
    }

    void FOR_STMT1(){
        if(hayErrores) return;
        
        if(preanalisis.equals(var)){
            VAR_DECL();
        }else if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
                || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
                || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EXPR_STMT();
        }else if(preanalisis.equals(puntoYComa)){
            coincidir(puntoYComa);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba var, ;, !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void FOR_STMT2(){
        if(hayErrores) return;
        
        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EXPRESSION();
            coincidir(puntoYComa);
        }else if(preanalisis.equals(puntoYComa)){
            coincidir(puntoYComa);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba ;, !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void FOR_STMT3(){
        if(hayErrores) return;
        
        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EXPRESSION();
        }
    }

    void IF_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(si)){
            coincidir(si);
            coincidir(parentesisIzq);
            EXPRESSION();
            coincidir(parentesisDer);
            STATEMENT();
            ELSE_STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba IF.");
        }
    }

    void ELSE_STATEMENT(){
        if(hayErrores) return;

        if(preanalisis.equals(ademas)){
            coincidir(ademas);
            STATEMENT();
        }
    }

    void PRINT_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(imprimir)){
            coincidir(imprimir);
            EXPRESSION();
            coincidir(puntoYComa);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba PRINT.");
        }
    }

    void RETURN_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(retornar)){
            coincidir(retornar);
            RETURN_EXP_OPC();
            coincidir(puntoYComa);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba RETURN.");
        }
    }

    void RETURN_EXP_OPC(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EXPRESSION();
        }
    }

    void WHILE_STMT(){
        if(hayErrores) return;

        if(preanalisis.equals(mientras)){
            coincidir(mientras);
            coincidir(parentesisIzq);
            EXPRESSION();
            coincidir(parentesisDer);
            STATEMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba WHILE.");
        }
    }

    void BLOCK(){
        if(hayErrores) return;

        if(preanalisis.equals(corcheteIzq)){
            coincidir(corcheteIzq);
            BLOCK_DECL();
            coincidir(corcheteDer);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba {.");
        }
    }

    void BLOCK_DECL(){
        if(hayErrores) return;

        if(preanalisis.equals(clase) || preanalisis.equals(fun) || preanalisis.equals(var) || preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)
            || preanalisis.equals(para) || preanalisis.equals(si) || preanalisis.equals(imprimir) || preanalisis.equals(retornar) || preanalisis.equals(mientras) || preanalisis.equals(corcheteIzq)){
            DECLARATION();
            BLOCK_DECL();
        }
    }

    void EXPRESSION(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            ASSIGNMENT();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void ASSIGNMENT(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            LOGIC_OR();
            ASSIGNMENT_OPC();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void ASSIGNMENT_OPC(){
        if(hayErrores) return;

        if(preanalisis.equals(igual)){
            coincidir(igual);
            EXPRESSION();
        }
    }

    void LOGIC_OR(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            LOGIC_AND();
            LOGIC_OR_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }
    
    void LOGIC_OR_2(){
        if(hayErrores) return;

        if(preanalisis.equals(o)){
            coincidir(o);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    void LOGIC_AND(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EQUALITY();
            LOGIC_AND_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void LOGIC_AND_2(){
        if(hayErrores) return;

        if(preanalisis.equals(y)){
            coincidir(y);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    void EQUALITY(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            COMPARISON();
            EQUALITY_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void EQUALITY_2(){
        if(hayErrores) return;

        if(preanalisis.equals(diferente)){
            coincidir(diferente);
            COMPARISON();
            EQUALITY_2();
        }else if(preanalisis.equals(comparacion)){
            coincidir(comparacion);
            COMPARISON();
            EQUALITY_2();
        }
    }

    void COMPARISON(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            TERM();
            COMPARISON_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void COMPARISON_2(){
        if(hayErrores) return;

        if(preanalisis.equals(mayor)){
            coincidir(mayor);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(mayorIgual)){
            coincidir(mayorIgual);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(menor)){
            coincidir(menor);
            TERM();
            COMPARISON_2();
        }else if(preanalisis.equals(menorIgual)){
            coincidir(menorIgual);
            TERM();
            COMPARISON_2();
        }
    }
    
    void TERM(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            FACTOR();
            TERM_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void TERM_2(){
        if(hayErrores) return;

        if(preanalisis.equals(menos)){
            coincidir(menos);
            FACTOR();
            TERM_2();
        }else if(preanalisis.equals(mas)){
            coincidir(mas);
            FACTOR();
            TERM_2();
        }
    }

    void FACTOR(){
        if(hayErrores) return;

        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            UNARY();
            FACTOR_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void FACTOR_2(){
        if(hayErrores) return;

        if(preanalisis.equals(diagonal)){
            coincidir(diagonal);
            UNARY();
        }else if(preanalisis.equals(asterisco)){
            coincidir(asterisco);
            UNARY();
        }
    }

    void UNARY(){
        if(hayErrores) return;

        if(preanalisis.equals(verdadero) || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero)
            || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            CALL();
        }else if(preanalisis.equals(exclamacion)){
            coincidir(exclamacion);
            UNARY();
        }else if(preanalisis.equals(menos)){
            coincidir(menos);
            UNARY();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void CALL(){
        if(hayErrores) return;

        if(preanalisis.equals(verdadero) || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero)
            || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            PRIMARY();
            CALL_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void CALL_2(){
        if(hayErrores) return;

        if(preanalisis.equals(parentesisIzq)){
            coincidir(parentesisIzq);
            ARGUMENTS_OPC();
            coincidir(parentesisDer);
            CALL_2();
        }else if(preanalisis.equals(punto)){
            coincidir(punto);
            coincidir(identificador);
            CALL_2();
        }
    }

    void CALL_OPC(){
        if(hayErrores) return;

        if(preanalisis.equals(verdadero) || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero)
            || preanalisis.equals(cadena) || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            CALL();
            coincidir(punto);
        }
    }

    void PRIMARY(){
        if(hayErrores) return;

        if(preanalisis.equals(verdadero)){
            coincidir(verdadero);
        }else if(preanalisis.equals(falso)){
            coincidir(falso);
        }else if(preanalisis.equals(nulo)){
            coincidir(nulo);
        }else if(preanalisis.equals(este)){
            coincidir(este);
        }else if(preanalisis.equals(numero)){
            coincidir(numero);
        }else if(preanalisis.equals(identificador)){
            coincidir(identificador);
        }else if(preanalisis.equals(cadena)){
            coincidir(cadena);
        }else if(preanalisis.equals(parentesisIzq)){
            coincidir(parentesisIzq);
            EXPRESSION();
            coincidir(parentesisDer);
        }else if(preanalisis.equals(supert)){
            coincidir(supert);
            coincidir(punto);
            coincidir(identificador);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void FUNCTION(){
        if(hayErrores) return;
        
        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            coincidir(parentesisIzq);
            PARAMETERS_OPC();
            coincidir(parentesisDer);
            BLOCK();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un identificador.");
        }
    }

    void FUNCTIONS(){
        if(hayErrores) return;
        
        if(preanalisis.equals(identificador)){
            FUNCTION();
            FUNCTIONS();
        }
    }

    void PARAMETERS_OPC(){
        if(hayErrores) return;
        
        if(preanalisis.equals(identificador)){
            PARAMETERS();
        }
    }

    void PARAMETERS(){
        if(hayErrores) return;
        
        if(preanalisis.equals(identificador)){
            coincidir(identificador);
            PARAMETERS_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un identificador.");
        }
    }

    void PARAMETERS_2(){
        if(hayErrores) return;
        
        if(preanalisis.equals(coma)){
            coincidir(coma);
            coincidir(identificador);
            PARAMETERS_2();
        }
    }

    void ARGUMENTS_OPC(){
        if(hayErrores) return;
        
        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            ARGUMENTS();
        }
    }

    void ARGUMENTS(){
        if(hayErrores) return;
        
        if(preanalisis.equals(exclamacion) || preanalisis.equals(menos) || preanalisis.equals(falso) || preanalisis.equals(verdadero)
            || preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena)
            || preanalisis.equals(identificador) || preanalisis.equals(parentesisIzq) || preanalisis.equals(supert)){
            EXPRESSION();
            ARGUMENTS_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.linea + ". Se esperaba un !, -, (, SUPER, FALSE, TRUE, NULL, THIS, un numero, una cadena o un identificador.");
        }
    }

    void ARGUMENTS_2(){
        if(hayErrores) return;
        
        if(preanalisis.equals(coma)){
            coincidir(coma);
            EXPRESSION();
            ARGUMENTS_2();
        }
    }


    void coincidir(Token t){
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la linea " + preanalisis.linea + ". Se esperaba un " + t.tipo);

        }
    }

}
