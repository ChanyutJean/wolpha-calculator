package jean.wolpha;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class WolphaTests {
    private static final MathContext U = MathContext.UNLIMITED;

    @Test
    public void returnNumber() {
        assertEquals(BigDecimal.ZERO, WolphaCalculator.calculate("0"));
        assertEquals(BigDecimal.ONE, WolphaCalculator.calculate("1"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("10"));
    }

    @Test
    public void addition() {
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("3+7"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("5.5+4.5"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("0+10"));
    }

    @Test
    public void subtraction() {
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("15-5"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("10-0"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("12.8-2.8"));
    }

    @Test
    public void multiplication() {
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("2x5"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("2*5"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("20*0.5"));
    }

    @Test
    public void division() {
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("20/2"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("0.5/0.05"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("100/10"));
    }

    @Test
    public void mixed() {
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("8/2+5*2-4"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("2.5*2.5-5.5/2+6.5"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("2+2+2+2+2"));
    }

    @Test
    public void parenthesis() {
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("2*(2+3)"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("(10/5)/(4/20)"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("(2*(2+2))+2"));
        assertEquals(BigDecimal.ONE, WolphaCalculator.calculate("(((1)))"));
        assertEquals(BigDecimal.ONE, WolphaCalculator.calculate("((1+(1))-1)"));
    }

    @Test
    public void power() {
        assertEquals(BigDecimal.valueOf(8), WolphaCalculator.calculate("2^3"));
        assertEquals(BigDecimal.valueOf(256), WolphaCalculator.calculate("2^2^3"));
        assertEquals(BigDecimal.valueOf(64), WolphaCalculator.calculate("(2^2)^3"));
        assertEquals(BigDecimal.valueOf(4), WolphaCalculator.calculate("8^(2/3)"));
        assertEquals(BigDecimal.valueOf(2), WolphaCalculator.calculate("2*2^2/4"));
    }

    @Test
    public void sqrt() {
        assertEquals(BigDecimal.valueOf(4), WolphaCalculator.calculate("sqrt(16)"));
        assertEquals(BigDecimal.valueOf(3), WolphaCalculator.calculate("sqrt(sqrt(81))"));
        assertEquals(BigDecimal.valueOf(2), WolphaCalculator.calculate("sqrt(64)-sqrt(30+sqrt(36)"));
    }

    @Test
    public void factorial() {
        assertEquals(BigDecimal.valueOf(120), WolphaCalculator.calculate("5!"));
        assertEquals(BigDecimal.valueOf(20), WolphaCalculator.calculate("5!/3!"));
        assertEquals(BigDecimal.valueOf(48), WolphaCalculator.calculate("3!*3!+4!/2"));
    }

    @Test
    public void constant() {
        assertEquals(BigDecimalMath.e(U), WolphaCalculator.calculate("e"));
        assertEquals(BigDecimalMath.pi(U), WolphaCalculator.calculate("pi"));
        assertEquals(BigDecimalMath.e(U).multiply(BigDecimalMath.pi(U)),
                WolphaCalculator.calculate("pixe"));
    }

    @Test
    public void ln() {
        assertEquals(BigDecimal.ONE, WolphaCalculator.calculate("ln(e)"));
        assertEquals(BigDecimal.TEN, WolphaCalculator.calculate("ln(e^ln(e^10))"));
        assertEquals(BigDecimalMath.log(BigDecimalMath.pi(U), U),
                WolphaCalculator.calculate("ln(pi)"));
    }

    @Test
    public void log() {
        assertEquals(BigDecimal.valueOf(2), WolphaCalculator.calculate("log_10(100)"));
        assertEquals(BigDecimal.valueOf(2), WolphaCalculator.calculate("log_3(log_2(512))"));
        assertEquals(BigDecimalMath.log(BigDecimalMath.pi(U), U),
                WolphaCalculator.calculate("log_4(pi)/log_4(e)"));
    }

    @Test
    public void sin() {
        assertEquals(BigDecimal.ZERO, WolphaCalculator.calculate("sin(pi)"));
        assertEquals(BigDecimal.ONE, WolphaCalculator.calculate("sin(pi/2)"));
        assertEquals(BigDecimal.ZERO, WolphaCalculator.calculate("sin(2*pi)"));
    }

    @Test
    public void cos() {
        assertEquals(BigDecimal.valueOf(-1), WolphaCalculator.calculate("cos(pi)"));
        assertEquals(BigDecimal.ZERO, WolphaCalculator.calculate("cos(pi/2)"));
        assertEquals(BigDecimal.ONE, WolphaCalculator.calculate("cos(2*pi)"));
    }

    @Test
    public void tan() {
        assertEquals(BigDecimal.ONE, WolphaCalculator.calculate("tan(pi/4)"));
        assertEquals(BigDecimal.ZERO, WolphaCalculator.calculate("tan(2*pi)"));
        assertEquals(BigDecimalMath.sqrt(BigDecimal.valueOf(3), U), WolphaCalculator.calculate("tan(pi/6)"));
    }
}
