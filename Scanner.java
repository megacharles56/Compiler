import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.Y);
        palabrasReservadas.put("class", TipoToken.CLASE);
        palabrasReservadas.put("else", TipoToken.ADEMAS);
        palabrasReservadas.put("false", TipoToken.FALSO);
        palabrasReservadas.put("for", TipoToken.PARA);
        palabrasReservadas.put("fun", TipoToken.FUN); //definir funciones
        palabrasReservadas.put("if", TipoToken.SI);
        palabrasReservadas.put("null", TipoToken.NULO);
        palabrasReservadas.put("or", TipoToken.O);
        palabrasReservadas.put("print", TipoToken.IMPRIMIR);
        palabrasReservadas.put("return", TipoToken.RETORNAR);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("this", TipoToken.ESTE);
        palabrasReservadas.put("true", TipoToken.VERDADERO);
        palabrasReservadas.put("var", TipoToken.VAR); //definir variables
        palabrasReservadas.put("while", TipoToken.MIENTRAS);
    }

    private static final Map<String, TipoToken> signosSistema;
    static {
        signosSistema = new HashMap<>();
        signosSistema.put("(", TipoToken.PARENTESIS_IZQ);
        signosSistema.put(")", TipoToken.PARENTESIS_DER);
        signosSistema.put("{", TipoToken.CORCHETE_IZQ);
        signosSistema.put("}", TipoToken.CORCHETE_DER);
        signosSistema.put(",", TipoToken.COMA);
        signosSistema.put(";", TipoToken.PUNTO_Y_COMA);
        signosSistema.put("-", TipoToken.MENOS);
        signosSistema.put("+", TipoToken.MAS);
        signosSistema.put("*", TipoToken.ASTERISCO);
        signosSistema.put("/", TipoToken.DIAGONAL);
        signosSistema.put("!", TipoToken.EXCLAMACION);
        signosSistema.put("=", TipoToken.IGUAL);
        signosSistema.put("<", TipoToken.MENOR);
        signosSistema.put(">", TipoToken.MAYOR);
        signosSistema.put("==", TipoToken.COMPARACION);
        signosSistema.put(">=", TipoToken.MAYOR_IGUAL);
        signosSistema.put("<=", TipoToken.MENOR_IGUAL);
        signosSistema.put("!=", TipoToken.DIFERENTE);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        int estado = 0;
        char caracter = 0;
        String lexema = "";

        for(int i=0; i<source.length(); i++){
            caracter = source.charAt(i);

            switch (estado){
                case 0:
                    if(caracter == '*'){
                        tokens.add(new Token(TipoToken.ASTERISCO, "*", "*", linea));
                    }
                    else if(caracter == '+'){
                        tokens.add(new Token(TipoToken.MAS, "+", "+", linea));
                    }
                    else if(caracter == '-'){
                        tokens.add(new Token(TipoToken.MENOS, "-", "-", linea));
                    }
                    else if(caracter == '/'){
                        estado = 10;
                    }
                    else if(caracter == '('){
                        tokens.add(new Token(TipoToken.PARENTESIS_IZQ, "(", "(", linea));
                    }
                    else if(caracter == ')'){
                        tokens.add(new Token(TipoToken.PARENTESIS_DER, ")", ")", linea));
                    }
                    else if(caracter == '='){
                        estado = 8;
                        lexema += caracter;
                    }
                    else if(caracter == '{'){
                        tokens.add(new Token(TipoToken.CORCHETE_IZQ, "{", "{", linea));
                    }
                    else if(caracter == '}'){
                        tokens.add(new Token(TipoToken.CORCHETE_DER, "}", "}", linea));
                    }
                    else if(caracter == ','){
                        tokens.add(new Token(TipoToken.COMA, ",", ",", linea));
                    }
                    else if(caracter == ';'){
                        tokens.add(new Token(TipoToken.PUNTO_Y_COMA, ";", ";", linea));
                    }
                    else if(caracter == '!'){
                        estado = 8;
                        lexema += caracter;
                    }
                    else if(caracter == '<'){
                        estado = 8;
                        lexema += caracter;
                    }
                    else if(caracter == '>'){
                        estado = 8;
                        lexema += caracter;
                    }
                    else if(Character.isAlphabetic(caracter)){
                        estado = 1;
                        lexema = lexema + caracter;
                    }
                    else if(Character.isDigit(caracter)){
                        estado = 2;
                        lexema = lexema + caracter;
                    }
                    else if(caracter == '\"'){
                        estado = 9;
                    }
                    break;

                case 1:
                    if(Character.isAlphabetic(caracter) || Character.isDigit(caracter) ){
                        lexema = lexema + caracter;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            tokens.add(new Token(TipoToken.ID, lexema, lexema, linea));
                        }
                        else{
                            tokens.add(new Token(tt, lexema, lexema, linea));
                        }

                        estado = 0;
                        i--;
                        lexema = "";
                    }
                    break;
                case 2:
                    if(Character.isDigit(caracter)){
                        estado = 2;
                        lexema = lexema + caracter;
                    }
                    else if(caracter == '.'){
                        estado = 3;
                        lexema = lexema + caracter;
                    }
                    else if(caracter == 'E'){
                        estado = 5;
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.NUMERO, lexema, Double.valueOf(lexema), linea));
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 3:
                    if(Character.isDigit(caracter)){
                        estado = 4;
                        lexema = lexema + caracter;
                    }
                    else{
                        //Lanzar error
                    }
                    break;
                case 4:
                    if(Character.isDigit(caracter)){
                        estado = 4;
                        lexema = lexema + caracter;
                    }
                    else if(caracter == 'E'){
                        estado = 5;
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.NUMERO, lexema, Double.valueOf(lexema), linea));
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 5:
                    if(caracter == '+' || caracter == '-'){
                        estado = 6;
                        lexema = lexema + caracter;
                    }
                    else if(Character.isDigit(caracter)){
                        estado = 7;
                        lexema = lexema + caracter;
                    }
                    else{
                        // Lanzar error
                    }
                    break;
                case 6:
                    if(Character.isDigit(caracter)){
                        estado = 7;
                        lexema = lexema + caracter;
                    }
                    else{
                        // Lanzar error
                    }
                    break;
                case 7:
                    if(Character.isDigit(caracter)){
                        estado = 7;
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.NUMERO, lexema, Double.valueOf(lexema), linea));
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 8:
                    if(caracter == '='){
                        System.out.println("Hola " + lexema);
                        if(lexema.equals("=")){
                            tokens.add(new Token(TipoToken.COMPARACION, "==", "==", linea));
                        }else if(lexema.equals("<")){
                            tokens.add(new Token(TipoToken.MENOR_IGUAL, "<=", "<=", linea));
                        }else if(lexema.equals(">")){
                            tokens.add(new Token(TipoToken.MAYOR_IGUAL, ">=", ">=", linea));
                        }else if(lexema.equals("!")){
                            tokens.add(new Token(TipoToken.DIFERENTE, "!=", "!=", linea));
                        }
                        estado = 0;
                        lexema = "";
                    }
                    else{
                        tokens.add(new Token(signosSistema.get(lexema), lexema, lexema, linea));
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 9:
                    if(caracter == '"'){
                        tokens.add(new Token(TipoToken.CADENA, lexema, lexema, linea));
                        estado = 0;
                        lexema = "";
                    }else{
                        lexema += caracter;
                    }    
                    break;
                case 10:
                    if(caracter == '/'){
                        return tokens;
                    }else if(caracter == '*'){
                        estado = 11;
                    }else{
                        tokens.add(new Token(TipoToken.DIAGONAL, lexema, lexema, linea));
                    }     
                    break;
                case 11:
                    if(caracter == '*')
                        estado = 12;
                    break;
                case 12:
                    if(caracter == '/'){
                        lexema = "";
                        estado = 0;
                    }else if(caracter != '*'){
                        estado = 11;
                    }
                    break;
            }
        }
        tokens.add(new Token(TipoToken.EOF, "", null, linea));

        return tokens;
    }
}

/*
Signos o símbolos del lenguaje:
(
)
{
}
,
.
;
-
+
*
/
!
!=
=
==
<
<=
>
>=
// -> comentarios (no se genera token)
/* ... * / -> comentarios (no se genera token)
Identificador,
Cadena
Numero
Cada palabra reservada tiene su nombre de token
 */