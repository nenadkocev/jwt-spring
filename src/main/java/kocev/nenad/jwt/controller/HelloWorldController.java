package kocev.nenad.jwt.controller;

import kocev.nenad.jwt.config.JwtTokenUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController {

    private final JwtTokenUtil jwtTokenUtil;

    public HelloWorldController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam("username") String username,
                               @RequestParam("password") String password){
        return jwtTokenUtil.generateToken(null);
    }
}
