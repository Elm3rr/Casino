package com.Proyecto.Final.Controlladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Proyecto.Final.Entity.Moderador;
import com.Proyecto.Final.Service.ModService;


@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    ModService modService; 

    @GetMapping("/staff")
    public String staff(Model model,
    @RequestParam(value="estado", defaultValue = "Habilitado") String estado,
    @RequestParam(value="busqueda", required=false) String busqueda){
        List<Moderador> moderadores;
        if( busqueda !=null && !busqueda.isEmpty()){
            moderadores = modService.buscar_mods(estado, busqueda);
        }else{
            moderadores = modService.lista_mod(estado);
        }
        model.addAttribute("estado", estado);
        model.addAttribute("moderadores", moderadores);

        return "moderadores";
    }

    @GetMapping("/guardar")
    public String nuevoMod(){
        return null;
    }   
}
