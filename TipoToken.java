
public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)

    // Tipos de token
    ID, CADENA, NUMERO,

    // Signos del lenguaje
    PARENTESIS_IZQ, PARENTESIS_DER, CORCHETE_IZQ, CORCHETE_DER, COMA, PUNTO, PUNTO_Y_COMA, MENOS, MAS, ASTERISCO, DIAGONAL, EXCLAMACION, MENOR, MAYOR, IGUAL, COMPARACION, MENOR_IGUAL, MAYOR_IGUAL, DIFERENTE,

    // Palabras clave:
    Y, CLASE, ADEMAS, FALSO, PARA, FUN, SI, NULO, O, IMPRIMIR, RETORNAR, SUPER, ESTE, VERDADERO, VAR, MIENTRAS,

    // Final de cadena
    EOF
}