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
        palabrasReservadas.put("y", TipoToken.Y);
        palabrasReservadas.put("clase", TipoToken.CLASE);
        palabrasReservadas.put("ademas", TipoToken.ADEMAS);
        palabrasReservadas.put("falso", TipoToken.FALSO);
        palabrasReservadas.put("para", TipoToken.PARA);
        palabrasReservadas.put("fun", TipoToken.FUN); //definir funciones
        palabrasReservadas.put("si", TipoToken.SI);
        palabrasReservadas.put("nulo", TipoToken.NULO);
        palabrasReservadas.put("o", TipoToken.O);
        palabrasReservadas.put("imprimir", TipoToken.IMPRIMIR);
        palabrasReservadas.put("retornar", TipoToken.RETORNAR);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("este", TipoToken.ESTE);
        palabrasReservadas.put("verdadero", TipoToken.VERDADERO);
        palabrasReservadas.put("var", TipoToken.VAR); //definir variables
        palabrasReservadas.put("mientras", TipoToken.MIENTRAS);
    }

    private static final Map<Character, TipoToken> signosSistema;
    static {
        signosSistema = new HashMap<>();
        signosSistema.put('(', TipoToken.PARENTESIS_IZQ);
        signosSistema.put(')', TipoToken.PARENTESIS_DER);
        signosSistema.put('{', TipoToken.CORCHETE_IZQ);
        signosSistema.put('}', TipoToken.CORCHETE_DER);
        signosSistema.put(',', TipoToken.COMA);
        signosSistema.put('.', TipoToken.PUNTO);
        signosSistema.put(';', TipoToken.PUNTO_Y_COMA);
        signosSistema.put('-', TipoToken.MENOS);
        signosSistema.put('+', TipoToken.MAS);
        signosSistema.put('*', TipoToken.ASTERISCO);
        signosSistema.put('/', TipoToken.DIAGONAL);
        signosSistema.put('!', TipoToken.EXCLAMACION);
        signosSistema.put('=', TipoToken.IGUAL);
        signosSistema.put('<', TipoToken.MENOR);
        signosSistema.put('>', TipoToken.MAYOR);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        char curr;
        String lexema="";
        for(int i=0; i<source.length(); i++){
            curr = source.charAt(i);
            if(curr == '.' || isDigit(curr) || isLetter(curr)){
                lexema+=source.charAt(i);
            }else{
                if(lexema.isEmpty()){
                    System.out.println(curr + " vacio");
                    if(signosSistema.containsKey(curr)){
                        System.out.println("caracter");
                        tokens.add(new Token(signosSistema.get(curr), String.valueOf(curr), curr, linea));
                    }
                    continue;
                }else{
                    System.out.println(lexema + " cadena");
                    if(isNumber(lexema)){
                        tokens.add(new Token(TipoToken.NUMERO , lexema, null, linea));
                    }else if(isLexema(lexema)){
                        if(palabrasReservadas.containsKey(lexema)){
                            tokens.add(new Token(palabrasReservadas.get(lexema) , lexema, lexema, linea));
                        }else{
                            tokens.add(new Token(TipoToken.ID, lexema, lexema, linea));
                        }
                    }
                    i--;
                }
                lexema = "";
            }
        }
        /*
        Analizar el texto de entrada para extraer todos los tokens
        y al final agregar el token de fin de archivo
         */
        tokens.add(new Token(TipoToken.EOF, "", null, linea));
        linea++;
        return tokens;
    }
    
    boolean isLexema(String lexema){
        boolean[] q ={false, true, false};
        int state=0;
        char curr;
        for(int i=0; i<lexema.length(); i++){
            curr = lexema.charAt(i);
            switch(state){
                case 0:{
                    if(isLetter(curr)){
                        state=1;
                    }else{
                        state=2;
                    }
                    break;
                }
                case 1:{
                    if(isLetter(curr) || isDigit(curr)){
                        state=1;
                    }else{
                        state=2;
                    }
                    break;
                }
                case 2:{
                    break;
                }
            }
        }
        return q[state];
    }

    boolean isNumber(String lexema){
        boolean[] q ={false, true, false, true, false, false, false, true};
        int state=0;
        char curr;
        for(int i=0; i<lexema.length(); i++){
            curr = lexema.charAt(i);
            switch(state){
                case 0:{//false 
                    if(isDigit(curr))
                        state=1;
                    else
                        state=4;
                    break;
                }
                case 1:{//true 1
                    if(curr == '.')
                        state=2;
                    else if(curr == 'E')
                        state=5;
                    break;
                }
                case 2:{//false 1.
                    if(isDigit(curr))
                        state=3;
                    else
                        state=4;
                    break;
                }
                case 3:{//true 1.1
                    if(curr == 'E')
                        state=5;
                    else
                        state=4;
                    break;
                }
                case 4:{//false
                    break;
                }
                case 5:{//false 1.1E or 1E
                    if(curr == '+' || curr == '-'){
                        state=6;
                    }else
                        state=4;
                    break;
                }
                case 6:{//false 1.1E+ or 1E+
                    if(isDigit(curr))
                        state=7;
                    else
                        state=4;
                    break;
                }
                case 7:{//true 1.1E+1 or 1E+1
                    if(isDigit(curr))
                        state=7;
                    else
                        state=4;
                    break;
                }
            }

        }
        return q[state];
    }

    boolean isDigit(char c){
        if(c >= '0' && c <= '9')
            return true;
        else return false;
    }

    boolean isLetter(char c){
        if(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')
            return true;
        else return false;
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