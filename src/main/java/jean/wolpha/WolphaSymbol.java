package jean.wolpha;

import java.util.List;

public final class WolphaSymbol {

    public static final List<String> FUNCTIONS = List.of(
            "sin", "cos", "tan", "sqrt", "ln", "log"
    );

    public static final List<String> CONSTANTS = List.of(
            "e", "pi"
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

    public static final List<Character> OPERATORS = List.of(
            '+', '-', '*', 'x', '/', '.', '!', '^'
    );

    public static final List<Character> UNARY_OPERATORS = List.of('!');

    public static final List<Character> OPEN_PARENTHESIS = List.of('(');

    public static final List<Character> CLOSE_PARENTHESIS = List.of(')');

    public static final List<Character> CHARACTERS = List.of(
            's', 'i', 'n', 'q', 'r', 't', 'c', 'o', 'a', 'l', 'g', 'e', 'p', '_'
    );
}
