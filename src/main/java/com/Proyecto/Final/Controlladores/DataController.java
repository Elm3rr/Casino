package com.Proyecto.Final.Controlladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Service.PersonaService;

import java.util.List;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/usuarios")
    public String usuarios(Model model,
    @RequestParam(value="estado", defaultValue="Habilitado") String estado,
    @RequestParam(value="rol", defaultValue="ROLE_USER") String rol,
    @RequestParam(value="busqueda", required =false) String busqueda){
        List<Persona> usuarios;
        if(busqueda !=null && !busqueda.isEmpty()){
            usuarios = personaService.lista_busqueda_personas(rol, estado, busqueda);
        }else{
            usuarios =  personaService.lista_personas(estado, busqueda);
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("estado", estado);

        return "usuarios";
    }

    
}
