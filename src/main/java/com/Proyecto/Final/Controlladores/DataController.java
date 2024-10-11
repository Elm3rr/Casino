package com.Proyecto.Final.Controlladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Service.UserService;

import java.util.List;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    private UserService userService;

    @GetMapping("/usuarios")
    public String usuarios(Model model,
    @RequestParam(value="estado", defaultValue="Habilitado") String estado,
    @RequestParam(value="busqueda", required =false) String busqueda){
        List<Usuario> usuarios;
        if(busqueda !=null && !busqueda.isEmpty()){
            usuarios =  userService.Lista_busqueda(estado, busqueda);
        }else{
            usuarios = userService.Lista_Usuarios(estado);
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("estado", estado);

        return "usuarios";
    }

    
}
