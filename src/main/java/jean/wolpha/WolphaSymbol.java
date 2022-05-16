package jean.wolpha;

import java.util.List;

public final class WolphaSymbol {

    public static final List<String> FUNCTIONS = List.of(
            "sin", "cos", "tan", "sqrt", "ln", "log"
    );

    public static final List<Character> VALID_CHAR = List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '+', '-', '*', 'x', '/', '.', '(', ')', '!', '^',
            's', 'i', 'n', 'q', 'r', 't', 'c', 'o', 'a', 'l',
            'g', 'e', 'p', '_'
    );

    public static final List<Character> DIGITS = List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    );

    public static final List<Character> FUNCTION_STARTERS = List.of(
            's', 'c', 't', 'l'
    );

    public static final List<Character> CONSTANT_STARTERS = List.of(
            'e', 'p'
    );

    public static final List<Character> BINARY_OPERATORS = List.of(
            '+', '-', '*', 'x', '/', '.', '^'
    );

    public static final List<Character> UNARY_OPERATORS = List.of('!');

    public static final List<Character> OPEN_PARENTHESIS = List.of('(');

    public static final List<Character> CLOSE_PARENTHESIS = List.of(')');

    public static final List<Character> LOG_BASE = List.of('_');

    public static final List<Character> NON_STARTER_CHARACTERS = List.of(
            'i', 'n', 'q', 'r', 'o', 'a', 'g'
    );
}
