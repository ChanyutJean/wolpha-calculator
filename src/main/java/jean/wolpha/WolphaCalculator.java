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
    private static final MathContext U = MathContext.UNLIMITED;
    private long parenthesisStack;
    private final CharacterIterator itr;

    public WolphaCalculator(CharacterIterator itr) {
        this.itr = itr;
    }

    public static BigDecimal calculate(String expr) {
        CharacterIterator itr = new StringCharacterIterator(expr);
        WolphaCalculator calc = new WolphaCalculator(itr);
        return calc.readValue();
    }

    private BigDecimal readValue() {
        if (WolphaSymbol.DIGITS.contains(itr.current())
                || WolphaSymbol.DECIMAL_POINT.contains(itr.current())) {
            BigDecimal number = readNumber();

            if (WolphaSymbol.EQUALS.contains(itr.current())) {
                return number;
            } else if (WolphaSymbol.OPERATORS.contains(itr.current())) {
                return applyOperator(readNumber(), readOperator(), readValue());
            } else {
                throw new ArithmeticException(String.valueOf(itr.getIndex()));
            }

        } else if (WolphaSymbol.CONSTANT_STARTERS.contains(itr.current())) {
            BigDecimal constant = readConstant();

            if (WolphaSymbol.EQUALS.contains(itr.current())) {
                return constant;
            } else if (WolphaSymbol.OPERATORS.contains(itr.current())) {
                return applyOperator(readConstant(), readOperator(), readValue());
            } else {
                throw new ArithmeticException(String.valueOf(itr.getIndex()));
            }

        } else if (WolphaSymbol.FUNCTION_STARTERS.contains(itr.current())) {
            String funcName = readFunction();

            if (!WolphaSymbol.OPEN_PARENTHESIS.contains(itr.current())) {
                throw new ArithmeticException(String.valueOf(itr.getIndex()));
            }
            parenthesisStack += 1;

            return applyFunction(funcName, readValue());

        }  else if (WolphaSymbol.OPEN_PARENTHESIS.contains(itr.current())) {
            parenthesisStack += 1;
            return readValue();

        } else if (WolphaSymbol.CLOSE_PARENTHESIS.contains(itr.current())) {
            if (parenthesisStack == 0) {
                throw new ArithmeticException(String.valueOf(itr.getIndex()));
            }
            parenthesisStack -= 1;
            return readValue();

        } else if (WolphaSymbol.OPERATORS.contains(itr.current())
                || WolphaSymbol.NON_STARTER_CHARACTERS.contains(itr.current())
                || WolphaSymbol.EQUALS.contains(itr.current())
                || !WolphaSymbol.VALID_CHAR.contains(itr.current())) {
            throw new ArithmeticException(String.valueOf(itr.getIndex()));
        } else {
            // unreachable
            throw new ArithmeticException(String.valueOf(itr.getIndex()));
        }
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
                return left.divide(right, RoundingMode.HALF_UP);
            case '^':
                return BigDecimalMath.pow(left, right, U);
        }
        // unreachable
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private BigDecimal applyFunction(String funcName, BigDecimal operand) {
        switch (funcName) {
            case "sin":
                return BigDecimalMath.sin(operand, U);
            case "cos":
                return BigDecimalMath.cos(operand, U);
            case "tan":
                return BigDecimalMath.tan(operand, U);
            case "sqrt":
                return BigDecimalMath.sqrt(operand, U);
            case "ln":
                return BigDecimalMath.log(operand, U);
        }
        // unreachable
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private Character readOperator() {
        if (WolphaSymbol.OPERATORS.contains(itr.current())) {
            itr.next();
            return itr.current();
        }
        throw new ArithmeticException(String.valueOf(itr.getIndex()));
    }

    private String readFunction() {
        List<String> candidates = WolphaSymbol.FUNCTIONS;
        String funcName = isNext(itr, candidates);
        IntStream.range(0, funcName.length()).forEach(i -> itr.next());
        return funcName;
    }

    private String isNext(CharacterIterator itr, List<String> candidates) {
        DefaultMutableTreeNode node = createCandidateTree(candidates);
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

    private DefaultMutableTreeNode createCandidateTree(List<String> candidates) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();

        Map<Character, List<String>> map = new HashMap<>();
        candidates.stream()
                .filter(candidate -> !candidate.equals(""))
                .forEach(candidate -> {
                    if (map.containsKey(candidate.charAt(0))) {
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
                        .collect(Collectors.toList())
                ));
            }
        });

        return root;
    }

    private BigDecimal readConstant() {
        if (itr.current() == 'e') {
            itr.next();
            return BigDecimalMath.e(U);
        }
        if (itr.current() == 'p') {
            itr.next();
            if (itr.current() == 'i') {
                itr.next();
                return BigDecimalMath.pi(U);
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
            if (start && current == '0') {
                throw new ArithmeticException(String.valueOf(itr.getIndex()));
            }
            itr.next();
            readNumber(false, prefix + current);
        }
        return BigDecimal.valueOf(Double.parseDouble(prefix));
    }
}
