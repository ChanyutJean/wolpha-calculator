package jean.wolpha;

import ch.obermuhlner.math.big.BigDecimalMath;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.stream.Collectors;

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

    private BigDecimal readValue(CharacterIterator itr) throws Exception {
        if (!WolphaSymbol.VALID_CHAR.contains(itr.current())) {
            throw new Exception(String.valueOf(itr.getIndex()));
        }
        if (WolphaSymbol.CHARACTERS.contains(itr.current())) {
            String fn = readFunction(itr);
            //...
        }
        return BigDecimal.ZERO;
    }

    private String readFunction(CharacterIterator itr) throws Exception {
        if (itr.current() == 's') {
            itr.next();
            if (itr.current() == 'i') {
                if (assertNext('n')) {
                    return "sin";
                }
            }
            if (itr.current() == 'q') {
                if (assertNext('r') && assertNext('t')) {
                    return "sqrt";
                }
            }
            throw new Exception(String.valueOf(itr.getIndex()))
        }
        if (itr.current() == 'c') {

        }
    }

    private boolean assertNext(char c) throws Exception {
        if (itr.current() == c) {
            itr.next();
            return true;
        }
        throw new Exception(String.valueOf(itr.getIndex()));
    }

    private String isNext(CharacterIterator itr, List<String> candidates) {

    }

    private DefaultMutableTreeNode createCandidateTree(List<String> candidates) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();

        Map<Character, List<String>> map = new HashMap<>();
        candidates.forEach(candidate -> {
            if (map.containsKey(candidate.charAt(0))) {
                map.put(candidate.charAt(0), new ArrayList<>());
            }
            map.get(candidate.charAt(0)).add(candidate);
        });

        map.keySet().forEach(character -> {
            List<String> subCandidates = map.get(character);
            root.add(createCandidateTree(subCandidates));
        });

        return root


//
//        candidates.sort(Comparator.comparingInt(c -> c.charAt(0)));
//
//        Set<Character> firstChars = candidates.stream().map(c -> c.charAt(0)).collect(Collectors.toSet());
//        for (Character ch : firstChars) {
//            valid candidates.stream().filter(c -> c.charAt(0) == ch).map(c ->).collect(Collectors.toList());
//
//
//
//            children.add(new DefaultMutableTreeNode(ch));
//        }
//        for (DefaultMutableTreeNode child : children) {
//            root.add(child);
//        }
    }

    private static BigDecimal readConstant(CharacterIterator itr) throws Exception {
        if (!WolphaSymbol.VALID_CHAR.contains(itr.current())) {
            throw new Exception(String.valueOf(itr.getIndex()));
        }
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
    }

    private static String readOperator(CharacterIterator itr) throws Exception {

    }
}
