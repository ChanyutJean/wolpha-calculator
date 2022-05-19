package jean.wolpha;

import ch.obermuhlner.math.big.BigDecimalMath;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WolphaCalculator {
    private static final MathContext D = MathContext.DECIMAL128;
    private static final RoundingMode H = RoundingMode.HALF_UP;
    private static final int S = 20; // Intermediate precision
    private static final int F = 10; // Final precision
    private final CharacterIterator itr;

    public WolphaCalculator(CharacterIterator itr) {
        this.itr = itr;
    }

    public static BigDecimal calculate(String expr) {
        return calculateByOrderOfOperations(expr);
    }

    private static BigDecimal calculateByOrderOfOperations(String expr) {
        expr = expr.replaceAll("ln", "log");

        List<Integer> openIndices = getCharacterIndexes(expr, '(');
        List<Integer> closeIndices = getCharacterIndexes(expr, ')');
        List<Integer> range = findTopMostParenthesis(openIndices, closeIndices);

        if (range == null) {
            expr = insertOperationOrderNonParenthesis(expr);
            return calculateLeftToRight(expr, F);
        }
        return calculateByOrderOfOperations(evaluateTopMostParenthesis(expr, range));
    }

    private static String evaluateTopMostParenthesis(String expr, List<Integer> termRange) {
        String term = expr.substring(termRange.get(0) + 1, termRange.get(1));
        String termWithOrderOfOperations = insertOperationOrderNonParenthesis(term);

        // check if parenthesis is part of a function
        String beforeParenthesis = expr.substring(0, termRange.get(0));
        if (beforeParenthesis.equals("")) {
            return calculateLeftToRight(termWithOrderOfOperations, S).toPlainString()
                    + expr.substring(termRange.get(1) + 1);
        }

        if (WolphaSymbol.FUNCTION_ENDERS.contains(beforeParenthesis.charAt(beforeParenthesis.length() - 1))) {

            String funcName = functionPrefix(beforeParenthesis);
            return expr.substring(0, termRange.get(0) - funcName.length())
                    + calculateLeftToRight(funcName + "(" + term + ")", S).toPlainString()
                    + expr.substring(termRange.get(1) + 1);
        }
        return expr.substring(0, termRange.get(0))
                + calculateLeftToRight(termWithOrderOfOperations, S).toPlainString()
                + expr.substring(termRange.get(1) + 1);
    }

    private static String functionPrefix(String beforeParenthesis) {
        String lastThreeLetters = beforeParenthesis.substring(beforeParenthesis.length() - 3);

        switch (lastThreeLetters) {
            case "sin":
                return "sin";
            case "cos":
                return "cos";
            case "tan":
                return "tan";
            case "log":
                return "log";
            case "qrt":
                return "sqrt";
        }
        // unreachable
        throw new ArithmeticException(String.valueOf(beforeParenthesis.length()));
    }

    private static BigDecimal calculateLeftToRight(String expr, int scale) {
        expr += "=";
        CharacterIterator itr = new StringCharacterIterator(expr);
        WolphaCalculator calc = new WolphaCalculator(itr);
        return calc.readValue(false).setScale(scale, H);
    }

    private static List<Integer> getCharacterIndexes(String expr, char character) {
        List<Integer> indices = new ArrayList<>();
        int index = expr.indexOf(character);
        while (index >= 0) {
            indices.add(index);
            index = expr.indexOf(character, index + 1);
        }
        return indices;
    }

    // 1+(1+(1+1)) have indices of open at (2, 5) and close at (9, 10)
    // will be mapped to ((2, true), (5, true), (9, false), (10, false))
    // and will return (5, 9)
    private static List<Integer> findTopMostParenthesis(List<Integer> openIndices, List<Integer> closeIndices) {
        Map<Integer, Boolean> map = new TreeMap<>();
        openIndices.forEach(index -> map.put(index, true));
        closeIndices.forEach(index -> map.put(index, false));

        int openingParenthesisIndex = -1;
        for (int key : map.keySet()) {
            if (map.get(key)) {
                openingParenthesisIndex = key;
            } else {
                if (openingParenthesisIndex != -1) {
                    List<Integer> range = new ArrayList<>();
                    range.add(openingParenthesisIndex);
                    range.add(key);
                    return range;
                }
            }
        }

        return null;
    }

    // 1+1x1^1^1-1 becomes (1)+(1x1^1^1)-(1)
    // then becomes ((1))+((1x1^1^1))-((1))
    // and finally ((1))+((1)x(1^1^1))-((1))
    // 1^1^1 will be evaluated right to left in the implementation
    private static String insertOperationOrderNonParenthesis(String expr) {
        expr = expr.replaceAll("--", "+");
        expr = expr.replaceAll("\\+", ")+(");
        expr = replaceSubtraction(expr);
        expr = "(" + expr + ")";
        expr = expr.replaceAll("\\(\\)", "(0)");
        expr = expr.replaceAll("\\(", "((");
        expr = expr.replaceAll("\\)", "))");
        expr = expr.replaceAll("\\*", ")*(");
        expr = expr.replaceAll("x", ")x(");
        expr = expr.replaceAll("/", ")/(");
        return expr;
    }

    private static String replaceSubtraction(String expr) {
        for (int i = 0; i < expr.length(); i++) {
            if (WolphaSymbol.NEGATIVE.contains(expr.charAt(i))) {
                if (i == 0) {
                    continue;
                }
                if (WolphaSymbol.DIGITS.contains(expr.charAt(i-1))) {
                    expr = expr.substring(0, i) + ")-(" + expr.substring(i + 1);
                }
            }
        }
        return expr;
    }

    private BigDecimal readValue(boolean returnImmediately) {
        if (WolphaSymbol.DIGITS.contains(itr.current())
                || WolphaSymbol.DECIMAL_POINT.contains(itr.current())) {

            BigDecimal number = readNumber();
            if (returnImmediately) {
                return number;
            }
            return parseExpectingOperator(number);

        } else if (WolphaSymbol.CONSTANT_STARTERS.contains(itr.current())) {

            BigDecimal constant = readConstant();
            if (returnImmediately) {
                return constant;
            }
            return parseExpectingOperator(constant);

        } else if (WolphaSymbol.FUNCTION_STARTERS.contains(itr.current())) {

            String funcName = readFunction();
            itr.next();
            BigDecimal operand = readParenthesisValue();

            BigDecimal functionResult = applyFunction(funcName, operand);

            if (returnImmediately) {
                return functionResult;
            }
            return parseExpectingOperator(functionResult);

        }  else if (WolphaSymbol.OPEN_PARENTHESIS.contains(itr.current())) {

            itr.next();

            BigDecimal value = readParenthesisValue();

            if (returnImmediately) {
                return value;
            }
            return parseExpectingOperator(value);

        } else if (WolphaSymbol.NEGATIVE.contains(itr.current())) {

            itr.next();

            BigDecimal operand = readValue(true);

            return parseExpectingOperator(operand.multiply(BigDecimal.valueOf(-1)));

        } else if (WolphaSymbol.OPERATORS.contains(itr.current())
                || WolphaSymbol.NON_STARTER_CHARACTERS.contains(itr.current())
                || WolphaSymbol.CLOSE_PARENTHESIS.contains(itr.current())
                || WolphaSymbol.EQUALS.contains(itr.current())
                || !WolphaSymbol.VALID_CHAR.contains(itr.current())) {

            throw new ArithmeticException(String.valueOf(itr.getIndex()));

        } else {
            // unreachable
            throw new ArithmeticException(String.valueOf(itr.getIndex()));
        }
    }

    private BigDecimal parseExpectingOperator(BigDecimal value) {
        if (WolphaSymbol.EQUALS.contains(itr.current())
                || WolphaSymbol.CLOSE_PARENTHESIS.contains(itr.current())) {

            return value;

        } else if (WolphaSymbol.OPERATORS.contains(itr.current())) {

            WolphaCalculator powerCalculator = new WolphaCalculator((CharacterIterator) itr.clone());
            char operator = powerCalculator.readOperator();

            if (operator == '^') {
                powerCalculator.readValue(true);

                if (WolphaSymbol.EQUALS.contains(itr.current())
                        || WolphaSymbol.CLOSE_PARENTHESIS.contains(itr.current())) {

                    return applyOperator(value, readOperator(), readValue(true));

                } else if (WolphaSymbol.OPERATORS.contains(itr.current())) {

                    char nextOperator;

                    try {
                        nextOperator = powerCalculator.readOperator();
                    } catch (ArithmeticException e) {
                        return applyOperator(value, readOperator(), readValue(true));
                    }

                    if (nextOperator == '^') {
                        return applyOperator(value, readOperator(), parseExpectingOperator(readValue(true)));
                    }

                    return parseExpectingOperator(applyOperator(value, readOperator(), readValue(true)));
                }

            }

            BigDecimal result = applyOperator(value, readOperator(), readValue(true));
            return parseExpectingOperator(result);

        } else {
            throw new ArithmeticException(String.valueOf(itr.getIndex()));
        }
    }

    private BigDecimal readParenthesisValue() {
        StringBuilder parenthesisValue = new StringBuilder();
        while (!WolphaSymbol.CLOSE_PARENTHESIS.contains(itr.current())) {
            if (WolphaSymbol.FUNCTION_STARTERS.contains(itr.current())) {

                String funcName = readFunction();

                itr.next();
                BigDecimal operand = readParenthesisValue();
                parenthesisValue.append(applyFunction(funcName, operand).toPlainString());

            } else if (WolphaSymbol.OPEN_PARENTHESIS.contains(itr.current())) {

                itr.next();
                parenthesisValue.append(readParenthesisValue().toPlainString());

            } else {

                parenthesisValue.append(itr.current());
                itr.next();
            }
        }
        itr.next();

        return WolphaCalculator.calculateLeftToRight(parenthesisValue.toString(), S);
    }

    private BigDecimal applyOperator(BigDecimal left, char operation, BigDecimal right) {
        switch (operation) {
            case '+':
                return left.add(right);
            case '-':
                return left.subtract(right);
            case '*':
            case 'x':
                return left.multiply(right);
            case '/':
                return left.divide(right, S, H);
            case '^':
                return BigDecimalMath.pow(left, right, MathContext.DECIMAL128);
        }
        // unreachable
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private BigDecimal applyFunction(String funcName, BigDecimal operand) {
        switch (funcName) {
            case "sin":
                return BigDecimalMath.sin(operand, D);
            case "cos":
                return BigDecimalMath.cos(operand, D);
            case "tan":
                return BigDecimalMath.tan(operand, D);
            case "sqrt":
                return BigDecimalMath.sqrt(operand, D);
            case "log":
                return BigDecimalMath.log(operand, D);
        }
        // unreachable
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private Character readOperator() {
        char current = itr.current();
        if (WolphaSymbol.OPERATORS.contains(current)) {
            itr.next();
            return current;
        }
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private String readFunction() {
        List<String> candidates = WolphaSymbol.FUNCTIONS;
        return isNext(itr, candidates);
    }

    private String isNext(CharacterIterator itr, List<String> candidates) {
        DefaultMutableTreeNode node = createCandidateTree(candidates, '-');
        return isNext(itr, node, "");
    }

    private String isNext(CharacterIterator itr, DefaultMutableTreeNode node, String prefix) {
        List<DefaultMutableTreeNode> children = Collections.list(node.children()).stream()
                .map(child -> (DefaultMutableTreeNode) child)
                .collect(Collectors.toList());
        if (children.size() == 0) {
            return prefix;
        }
        for (DefaultMutableTreeNode child : children) {
            if ((char) child.getUserObject() == itr.current()) {
                itr.next();
                return isNext(itr, child, prefix + child.getUserObject());
            }
        }
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private DefaultMutableTreeNode createCandidateTree(List<String> candidates, char front) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(front);

        Map<Character, List<String>> map = new HashMap<>();
        candidates.stream()
                .filter(candidate -> !candidate.equals(""))
                .forEach(candidate -> {
                    if (!map.containsKey(candidate.charAt(0))) {
                        map.put(candidate.charAt(0), new ArrayList<>());
                    }
                    if (candidate.length() == 1) {
                        root.add(new DefaultMutableTreeNode(candidate.charAt(0)));
                    } else {
                        map.get(candidate.charAt(0)).add(candidate);
                    }
                });

        map.keySet().forEach(character -> {
            List<String> subCandidates = map.get(character);
            if (subCandidates.size() > 0) {
                root.add(createCandidateTree(subCandidates.stream()
                        .map(candidate -> candidate.substring(1))
                        .collect(Collectors.toList()), character
                ));
            }
        });

        return root;
    }

    private BigDecimal readConstant() {
        if (itr.current() == 'e') {
            itr.next();
            return BigDecimalMath.e(D);
        }
        if (itr.current() == 'p') {
            itr.next();
            if (itr.current() == 'i') {
                itr.next();
                return BigDecimalMath.pi(D);
            } else {
                throw new ArithmeticException(String.valueOf(itr.getIndex()));
            }
        }
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private BigDecimal readNumber() {
        return readNumber(true, "");
    }

    private BigDecimal readNumber(boolean start, String prefix) {
        if (WolphaSymbol.DIGITS.contains(itr.current())
                || (itr.current() == '.')) {
            char current = itr.current();
            itr.next();
            return readNumber(false, prefix + current);
        }
        return BigDecimal.valueOf(Double.parseDouble(prefix));
    }
}
