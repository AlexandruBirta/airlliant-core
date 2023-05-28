package ro.unibuc.fmi.airlliantcore.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeSwaggerRedirectController {

    @RequestMapping(value = "/airlliant/v3")
    public String index() {
        return "redirect:/api-docs/swagger-ui.html";
    }

}