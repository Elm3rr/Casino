package com.Proyecto.Final.Controlladores;

import java.util.List;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Proyecto.Final.DTO.CombateRequest;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Service.ApuestaService;
import com.Proyecto.Final.Service.CombateService;
import com.Proyecto.Final.Service.RobotService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/combate")
public class CombateController {

    @Autowired
    private CombateService combateService;

    @Autowired
    private RobotService robotService;

    @Autowired
    private ApuestaService apuestaService;

    @GetMapping("/eliminar")
    public String deleteProduct(@RequestParam long id, Principal principal){
        try{
            combateService.ocultar_combate(id, principal);
            return "redirect:/user/combates";

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage()); 
        }
        return "redirect:/robots";
    }

    @GetMapping("/crear")
    public String crear_robot(@RequestParam(value="id", required = false) Long id, Model model,
    @RequestParam(value="estado", defaultValue="Disponible") String estado){
        CombateRequest combateRequest;
        if(id != null){
            Combate combate = combateService.findById(id);
            combateRequest = combateService.ActualData(combate);
        }else{
            combateRequest = new CombateRequest();
        }
        List<Robot> robots = robotService.robots_list_habilitados(estado);
        List<String> estados = List.of("Pendiente", "Finalizado", "Pospuesto", "Cancelado");
        model.addAttribute("robots", robots);
        model.addAttribute("estados", estados);
        model.addAttribute("combateRequest", combateRequest);

        return "r_combate";
    }

    @PostMapping("/crear_editar")
    public String crear_editar(@RequestParam("robotsSeleccionados") List<Long> robotsSeleccionados,
    @Valid @ModelAttribute("combateRequest") CombateRequest combateRequest, BindingResult result, Model model,
    @RequestParam(value="estado", defaultValue="Desocupado") String estado, Principal principal){
        
        if(robotsSeleccionados.size() != 2){
            model.addAttribute("error", "Debes seleccionar exactamente dos robots");
            List<Robot> robots = robotService.robots_list_habilitados(estado);
            List<String> estados = List.of("Pendiente", "Finalizado", "Pospuesto", "Cancelado");
            model.addAttribute("robots", robots);
            model.addAttribute("estados", estados);
            return "r_combate";
        }

        if(result.hasErrors()){
            List<Robot> robots = robotService.robots_list_habilitados(estado);
            List<String> estados = List.of("Pendiente", "Finalizado", "Pospuesto", "Cancelado");
            model.addAttribute("robots", robots);
            model.addAttribute("estados", estados);
            return "r_combate";
        }

        try{
            combateService.saveCombate(robotsSeleccionados, combateRequest, principal);
        }catch(Exception e){
            model.addAttribute("error", "Ocurrió un error al guardar el combate");
            List<Robot> robots = robotService.robots_list_habilitados(estado);
            List<String> estados = List.of("Pendiente", "Finalizado", "Pospuesto", "Cancelado");
            model.addAttribute("robots", robots);
            model.addAttribute("estados", estados);
            return "r_combate";
        }
        return "redirect:/home";
    }

    @GetMapping("/iniciar")
        public String iniciarCombate(@RequestParam("id") Long id, Model model) {
        Combate combate = combateService.findById(id);
        model.addAttribute("combate", combate);
        return "determinarGanador";
    }

    @PostMapping("/combate/guardarGanador")
    public String guardarGanador(@RequestParam("combateId") Long combateId,
                                  @RequestParam("ganador") Long ganadorId) {
        try {
            apuestaService.guardarGanador(combateId, ganadorId);
            return "redirect:/mod/combates";  // Redirigir a la lista de combates
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            return "error"; // Redirigir a una página de error si ocurre algún problema
        }
    }




    

}
