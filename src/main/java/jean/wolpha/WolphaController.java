package jean.wolpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WolphaController {
    @Autowired
    private WolphaService serv;

    @GetMapping("/")
    public String home() {
        return "Welcome to Wolpha! To use this API, please perform a POST request on this endpoint.";
    }

    @PostMapping("/")
    public String api(@RequestBody String expr) {
        return serv.process(expr).toPlainString();
    }

}