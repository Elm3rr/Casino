package com.Proyecto.Final.Controlladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Entity.Transaccion;
import com.Proyecto.Final.Service.PersonaService;
import com.Proyecto.Final.Service.TransactionService;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private TransactionService transactionService;

    //Logica para vetar usuarios
    @PostMapping("/vetar_usuario")
    public String eliminate(@RequestParam Long id, @RequestParam("motivo") String motivo, Principal principal){
        try {
            personaService.vetar_cuenta(id, motivo,principal);
            return "redirect:/usuarios";
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/usuarios";
    }
    //Logica para restaurar usuarios (solo admins)
    //Mover a admincontroller
    @GetMapping("/restaurar_usuario")
    public String restaurar(@RequestParam Long id){
        try {
            personaService.restaurar_usuario(id);
            return "redirect:/usuarios";
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/usuarios";
    }

    //Logica para la busqueda de usuarios tanto(USER como MODERADORES)
    @GetMapping("/usuarios")
    public String usuarios(Model model,
    @RequestParam(value="estado", defaultValue="Habilitado") String estado,
    @RequestParam(value="rol", defaultValue="ROLE_USER") String rol,
    @RequestParam(value="busqueda", required =false) String busqueda){
        List<Persona> usuarios;

        if(estado.equals("Vetado")){
            if(busqueda !=null && !busqueda.isEmpty()){
                usuarios = personaService.lista_busqueda_vetados(rol, busqueda);
            }else{
                usuarios = personaService.lista_vetados(rol);
            }
        }else{
            if(busqueda !=null && !busqueda.isEmpty()){
                usuarios = personaService.lista_busqueda_personas(rol, estado, busqueda);
            }else{
                usuarios =  personaService.lista_personas(rol, estado);
            }
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("estado", estado);

        return "usuarios";
    }

    //Logica para la busqueda de transacciones
    @GetMapping("/transactions")
    public String HistorialTransacciones(Model model,
    @RequestParam(value="tipo", defaultValue="Recarga") String tipo,
    @RequestParam(value="estado", defaultValue="Pendiente") String estado,
    @RequestParam(value="busqueda", required =false) String busqueda, 
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin){

        List<Transaccion> transacciones = transactionService.getTransaccionesAdmin(busqueda, tipo, estado, fechaInicio, fechaFin);

        model.addAttribute("transacciones", transacciones);

        return "Transacciones";
    }
    
}
