package jean.wolpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WolphaService {
    @Autowired
    private WolphaRepository repo;

    public BigDecimal process(String expr) {
        repo.addEntity(expr, new java.sql.Date(System.currentTimeMillis()));
        return WolphaCalculator.calculate(expr);
    }
}
