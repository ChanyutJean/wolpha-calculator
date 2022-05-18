package jean.wolpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

@Service
public class WolphaService {
    @Autowired
    private WolphaRepository repo;

    public String process(String expr) {
        String result = String.valueOf(WolphaCalculator.calculate(expr).doubleValue());
        repo.addEntity(expr, result, new java.sql.Timestamp(System.currentTimeMillis()));
        return result;
    }
}
