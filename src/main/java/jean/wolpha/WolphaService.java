package jean.wolpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WolphaService {
    @Autowired
    private WolphaRepository repo;

    public String process(String expr) {
        String result = WolphaCalculator.calculate(expr).toPlainString()
        repo.addEntity(expr, result, new java.sql.Timestamp(System.currentTimeMillis()));
        return result;
    }
}
