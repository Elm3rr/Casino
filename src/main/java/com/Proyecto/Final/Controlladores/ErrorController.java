package com.Proyecto.Final.Controlladores;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accesoDenago(){
        return "403";
    }
    
}
