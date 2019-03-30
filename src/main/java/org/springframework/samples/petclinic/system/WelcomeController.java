package org.springframework.samples.petclinic.system;


import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
    @GetMapping("/manual")
    public String abrirManual(){
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+"Manual de usuario.pdf");
            System.out.println("Final");
        } catch (IOException e) {
        e.printStackTrace();
        }
        return "redirect:/";
    }
}
