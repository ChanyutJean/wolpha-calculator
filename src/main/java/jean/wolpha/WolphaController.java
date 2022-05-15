package jean.wolpha;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class WolphaController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Wolpha! To use this API, please perform a POST request on this endpoint.";
    }

    @PostMapping("/")
    public BigDecimal calculate(@RequestBody String expr) {
        return BigDecimal.ZERO;
    }

}