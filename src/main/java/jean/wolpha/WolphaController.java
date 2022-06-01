package jean.wolpha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> api(@RequestBody String expr) {
        String result = serv.process(expr);
        if (result.length() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

    }

}