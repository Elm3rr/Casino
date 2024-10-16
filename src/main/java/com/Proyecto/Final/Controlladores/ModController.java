package com.Proyecto.Final.Controlladores;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Service.CombateService;

@Controller
@RequestMapping("/mod")
public class ModController {

    @Autowired
    private CombateService combateService;

    //Lista de combates para que los usuarios apuesten
    @GetMapping("/combates")
    public String combates(Model model,
    @RequestParam(value="estado", defaultValue="Programado") String estado,
    @RequestParam(value="busqueda", required=false) String busqueda,
    Principal principal){
        List<Combate> combates = combateService.getCombatesForModerators(principal,estado, busqueda);
        model.addAttribute("combates", combates);
        return "combates";
    }

    
}
