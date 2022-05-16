package jean.wolpha;

import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.List;

public final class WolphaCalculator {
    public static BigDecimal calculate(String expr) {
        CharacterIterator itr = new StringCharacterIterator(expr);

        return BigDecimal.ZERO;
    }

    private static BigDecimal readValue(CharacterIterator itr) throws Exception {
        if (!WolphaSymbol.VALID_CHAR.contains(itr.current())) {
            throw new Exception(String.valueOf(itr.getIndex()));
        }
        if (WolphaSymbol.CHARACTERS.contains(itr.current())) {
            String fn = readFunction(itr);
            //...
        }
        return BigDecimal.ZERO;
    }

    private static String readFunction(CharacterIterator itr) throws Exception {

    }

    private static BigDecimal readConstant(CharacterIterator itr) throws Exception {}

    private static String readOperator(CharacterIterator itr) throws Exception {}
}
