
package mx.ipn.escom.compiladores;

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

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        
        String lexema="";
        for(int i=0; i<source.length(); i++){
            if(source.charAt(i) == ' '){
                if(lexema.isEmpty()){
                    continue;
                }else{
                    if(isIdentifier(lexema))
                        tokens.add(new Token(palabrasReservadas.get(lexema) , lexema, null, linea));
                    else if(isNumber(lexema)){

                    }
                }
                lexema = "";
            }
            if(source.charAt(i) >= 'a' && source.charAt(i) <= 'z')

            lexema+=source.charAt(i);

        }

        /*
        Analizar el texto de entrada para extraer todos los tokens
        y al final agregar el token de fin de archivo
         */
        tokens.add(new Token(TipoToken.EOF, "", null, linea));

        return tokens;
    }

    boolean isIdentifier(String lexema){
        boolean[] q ={false, false, true};
        int state=0;
        for(int i=0; i<source.length(); i++){
            if(state==0){
                if(true);
            }else if(state==1){

            }
        }
        return q[state];
    }

    boolean isNumber(String lexema){
        boolean[] q ={true, false, true, false, true, false, false, true};
        int state=0;
        for(int i=0; i<source.length(); i++){
            if(state==0){

            }else if(state==1){

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