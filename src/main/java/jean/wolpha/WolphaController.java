package jean.wolpha;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class WolphaController {

    @PostMapping("/")
    public BigDecimal calculate(@RequestBody String expr) {
        return BigDecimal.ZERO;
    }

}