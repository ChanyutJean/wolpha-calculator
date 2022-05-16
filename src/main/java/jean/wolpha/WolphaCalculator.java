package jean.wolpha;

import ch.obermuhlner.math.big.BigDecimalMath;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WolphaCalculator {
    private static final MathContext U = MathContext.UNLIMITED;

    private BigDecimal current;
    private CharacterIterator itr;

    public WolphaCalculator(CharacterIterator itr) {
        this.current = BigDecimal.ZERO;
        this.itr = itr;
    }

    public BigDecimal calculate() {
        return BigDecimal.ZERO;
    }

    private BigDecimal readValue() throws Exception {
        if (!WolphaSymbol.VALID_CHAR.contains(itr.current())) {
            throw new Exception(String.valueOf(itr.getIndex()));
        }
        if (WolphaSymbol.DIGITS.contains(itr.current())) {
            current = readNumber();
        }


        return BigDecimal.ZERO;
    }

    private String readFunction() throws Exception {
        List<String> candidates = WolphaSymbol.FUNCTIONS;
        String funcName = isNext(itr, candidates);
        IntStream.range(0, funcName.length()).forEach(i -> {
            itr.next();
        });
        return funcName;
    }

    private String isNext(CharacterIterator itr, List<String> candidates) throws Exception {
        DefaultMutableTreeNode node = createCandidateTree(candidates);
        return isNext(itr, node, "");
    }

    private String isNext(CharacterIterator itr, DefaultMutableTreeNode node, String prefix) throws Exception {
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
        throw new Exception(String.valueOf(itr.getIndex()));
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

    private BigDecimal readConstant() throws Exception {
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
                throw new Exception(String.valueOf(itr.getIndex()));
            }
        }
        throw new Exception(String.valueOf(itr.getIndex()));
    }

    private BigDecimal readNumber() throws Exception {
        return readNumber(true, "");
    }

    private BigDecimal readNumber(boolean start, String prefix) throws Exception {
        if (WolphaSymbol.DIGITS.contains(itr.current())) {
            char current = itr.current();
            if (start && current == '0') {
                throw new Exception(String.valueOf(itr.getIndex()));
            }
            itr.next();
            readNumber(false, prefix + current);
        }
        return BigDecimal.valueOf(Long.parseLong(prefix));
    }
}
