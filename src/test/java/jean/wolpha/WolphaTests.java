package jean.wolpha;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class WolphaTests {
    private static final MathContext D = MathContext.DECIMAL128;
    private static final RoundingMode H = RoundingMode.HALF_EVEN;
    private static final int F = 10;


    private static BigDecimal round(BigDecimal value) {
        return value.setScale(F, H);
    }

    @Test
    public void returnNumber() {
        assertEquals(round(BigDecimal.ZERO), WolphaCalculator.calculate("0"));
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("1"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("10"));
    }

    @Test
    public void addition() {
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("3+7"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("5.5+4.5"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("0+10"));
    }

    @Test
    public void subtraction() {
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("15-5"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("10-0"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("12.8-2.8"));
    }

    @Test
    public void multiplication() {
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("2x5"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("2*5"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("20*0.5"));
    }

    @Test
    public void division() {
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("20/2"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("0.5/0.05"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("100/10"));
    }

    @Test
    public void mixed() {
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("8/2+5*2-4"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("2.5*2.5-5.5/2+6.5"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("2+2+2+2+2"));
    }

    @Test
    public void decimal() {
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate(".1/.1"));
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("0.1/0.1"));
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate(".1/0.1"));
    }

    @Test
    public void parenthesis() {
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("2*(2+3)"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("(10/5)/(4/20)"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("(2x(2+2))+2"));
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("(((1)))"));
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("((1+(1))-1)"));
    }

    @Test
    public void power() {
        assertEquals(round(BigDecimal.valueOf(8)), WolphaCalculator.calculate("2^3"));
        assertEquals(round(BigDecimal.valueOf(256)), WolphaCalculator.calculate("2^2^3"));
        assertEquals(round(BigDecimal.valueOf(64)), WolphaCalculator.calculate("(2^2)^3"));
        assertEquals(round(BigDecimal.valueOf(4)), WolphaCalculator.calculate("8^(2/3)"));
        assertEquals(round(BigDecimal.valueOf(2)), WolphaCalculator.calculate("2*2^2/4"));
    }

    @Test
    public void sqrt() {
        assertEquals(round(BigDecimal.valueOf(4)), WolphaCalculator.calculate("sqrt(16)"));
        assertEquals(round(BigDecimal.valueOf(3)), WolphaCalculator.calculate("sqrt(sqrt(81))"));
        assertEquals(round(BigDecimal.valueOf(2)), WolphaCalculator.calculate("sqrt(64)-sqrt(30+sqrt(36))"));
    }

    @Test
    public void constant() {
        assertEquals(round(BigDecimalMath.e(D)), WolphaCalculator.calculate("e"));
        assertEquals(round(BigDecimalMath.pi(D)), WolphaCalculator.calculate("pi"));
        assertEquals(round(BigDecimalMath.e(D).multiply(BigDecimalMath.pi(D))),
                WolphaCalculator.calculate("pixe"));
    }

    @Test
    public void ln() {
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("ln(e)"));
        assertEquals(round(BigDecimal.TEN), WolphaCalculator.calculate("ln(e^ln(e^10))"));
        assertEquals(round(BigDecimalMath.log(BigDecimalMath.pi(D), D)),
                WolphaCalculator.calculate("ln(pi)"));
    }

    @Test
    public void sin() {
        assertEquals(round(BigDecimal.ZERO), WolphaCalculator.calculate("sin(pi)"));
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("sin(pi/2)"));
        assertEquals(round(BigDecimal.ZERO), WolphaCalculator.calculate("sin(2*pi)"));
    }

    @Test
    public void cos() {
        assertEquals(round(BigDecimal.valueOf(-1)), WolphaCalculator.calculate("cos(pi)"));
        assertEquals(round(BigDecimal.ZERO), WolphaCalculator.calculate("cos(pi/2)"));
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("cos(2*pi)"));
    }

    @Test
    public void tan() {
        assertEquals(round(BigDecimal.ONE), WolphaCalculator.calculate("tan(pi/4)"));
        assertEquals(round(BigDecimal.ZERO), WolphaCalculator.calculate("tan(2*pi)"));
        assertEquals(round(BigDecimalMath.sqrt(BigDecimal.valueOf(3), D)), WolphaCalculator.calculate("tan(pi/3)"));
    }
}
