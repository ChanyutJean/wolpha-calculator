package jean.wolpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WolphaService {
    @Autowired
    private WolphaRepository repo;

    public String process(String expr) {
        String result = "";
        try {
            result = String.valueOf(WolphaCalculator.calculate(expr).doubleValue());
        } catch (ArithmeticException e) {
            repo.addEntity(expr, "", Integer.parseInt(e.getMessage()), new java.sql.Timestamp(System.currentTimeMillis()));
        }
        repo.addEntity(expr, result, -1, new java.sql.Timestamp(System.currentTimeMillis()));
        return result;
    }
}
