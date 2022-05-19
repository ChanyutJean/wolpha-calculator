package jean.wolpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class WolphaController {
    @Autowired
    private WolphaService serv;

    @GetMapping("/")
    public String home() {
        return "Welcome to Wolpha! To use this API, please perform a POST request on this endpoint.";
    }

    @PostMapping("/")
    public ResponseEntity<String> api(@RequestBody String expr) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(serv.process(expr));
        } catch (ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}