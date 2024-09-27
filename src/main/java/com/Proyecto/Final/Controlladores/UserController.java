package com.Proyecto.Final.Controlladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Service.CombateService;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CombateService combateService;

    @GetMapping("/combates")
    public String combates(Model model){
        List<Combate> combates = combateService.Lista_Combates_Pendientes();
        model.addAttribute("combates", combates);
        return "combate";
    }
}
