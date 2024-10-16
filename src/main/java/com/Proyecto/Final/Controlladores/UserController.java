package com.Proyecto.Final.Controlladores;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Proyecto.Final.DTO.ApuestaRequest;
import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.DTO.TransaccionRequest;
import com.Proyecto.Final.Entity.Apuesta;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Transaccion;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Service.ApuestaService;
import com.Proyecto.Final.Service.CombateService;
import com.Proyecto.Final.Service.ImagenService;
import com.Proyecto.Final.Service.PersonaService;
import com.Proyecto.Final.Service.TransactionService;
import com.Proyecto.Final.Service.UserService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CombateService combateService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private ApuestaService apuestaService;

    @Autowired
    private UserService userService;

    //Logica para eliminar
    @GetMapping("/eliminacion")
    public String eliminar_cuenta(Principal principal, RedirectAttributes redirectAttributes){
        try {
            personaService.eliminar_cuenta(principal);
            redirectAttributes.addFlashAttribute("mensaje", "Cuenta eliminada exitosamente.");
            return "redirect:/login"; 
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la cuenta: " + ex.getMessage());
             return "redirect:/home";
        }
    }

    
    //Get para conseguir la vista del perfil, dando todos los atributos falses ya que así se activaran los readonly
    @GetMapping("/perfil")
    public String show(Model model, Principal principal){
        PersonaRequest user = personaService.ActualData(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("editable", false); // Solo visualización
        model.addAttribute("esEdicion", false);
        model.addAttribute("esAdmin", false);
        return "register"; 
    }


    //La logica de esEdicion true y editable true, dentro de la verificación del metodo del html enruta hacia esta lógica que nos permite la actualización
    //luego de que le de editar, básicamente se desactiva el readonly
    @GetMapping("/edit")
    public String editarDatos(Model model, Principal principal){
        PersonaRequest user = personaService.ActualData(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("editable", true);
        model.addAttribute("esEdicion", true);
        model.addAttribute("exito", false);
        model.addAttribute("esAdmin", false);
        return "register";
    }

    //Post para editar
    @PostMapping("/edit")
    public String confirm(@Valid @ModelAttribute("user") PersonaRequest personaRequest, BindingResult result, Model model) {
        
        if (!personaRequest.getPassword().equals(personaRequest.getConfirmPassword())) {
            result.addError(
                new FieldError("user", "confirmPassword", "Las contraseñas no coinciden")
            );
        }
    
        if (personaService.email_ocupado(personaRequest.getEmail()) && !personaService.DueñoUsername(personaRequest.getUsername(), personaRequest.getId())) {
            result.addError(
                new FieldError("user", "email", "Correo ya utilizado")
            );
        }
    
        if (personaService.CUI_ocupado(personaRequest.getCui()) && !personaService.DueñoCUI(personaRequest.getCui(), personaRequest.getId())) {
            result.addError(
                new FieldError("user", "cui", "CUI ya utilizado")
            );
        }
    
        if (result.hasErrors()) {
            model.addAttribute("editable", true);
            model.addAttribute("esEdicion", true);
            return "register";
        }
    
        try {
            personaService.newUser(personaRequest, personaRequest.getRol(), null);
            model.addAttribute("user", new PersonaRequest());
        } catch (Exception ex) {
            result.addError(
                new FieldError("user", "nombre", ex.getMessage())
            );
        }
    
        return "redirect:/user/perfil";
    }

    //Metodo de enrutamiento hacia el get para descargar saldo
    @GetMapping("/retirar")
    public String descarga(Model model){
        TransaccionRequest transaccionRequest = new TransaccionRequest();
        transaccionRequest.setTipo("Retiro");
        model.addAttribute("request",transaccionRequest);
        return "r_transaccion";
    }

    @GetMapping("/recargar")
    public String recargar(Model model){
        TransaccionRequest transaccionRequest = new TransaccionRequest();
        transaccionRequest.setTipo("Recarga");
        model.addAttribute("request",transaccionRequest);
        return "r_transaccion";
    }

    //Metodo de enrutamiento desde el get y post para retirar saldo
    @PostMapping("/processTransaction")
    public String process(@Valid @ModelAttribute("request") TransaccionRequest transaccionRequest,
    BindingResult result, Model model, Principal principal){

        if(result.hasErrors()){
            model.addAttribute("request", transaccionRequest);
            return "r_transaccion";
        }try {
            String nombreImagen = null;
            if(transaccionRequest.getTipo().equals("Recarga")){
                nombreImagen = imagenService.buildImage(transaccionRequest.getImage());
            }
            transactionService.saveTransaction(transaccionRequest,nombreImagen,principal);
            model.addAttribute("exito", true);
            model.addAttribute("request", new TransaccionRequest());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            model.addAttribute("request", transaccionRequest);
            return "r_transaccion";
        }
        return "r_transaccion";
    }

    //Metodo de enrutamiento para ver las transacciones(recarga o retiro de saldo) realizadas por el usuario
    @GetMapping("/myTransactions")
    public String HistorialTransacciones(Model model,
    @RequestParam(value="tipo", defaultValue="Recarga") String tipo,
    @RequestParam(value="estado", defaultValue="Pendiente") String estado,
    @RequestParam(value="busqueda", required =false) String busqueda, 
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
    Principal principal){

        List<Transaccion> transacciones = transactionService.getTransaccionesUser(principal, tipo, estado, fechaInicio, fechaFin);

        model.addAttribute("transacciones", transacciones);
        model.addAttribute("tipo", tipo);
        model.addAttribute("estado", estado);
        return "Transacciones";
    }

    //Lista de combates para que los usuarios apuesten
    @GetMapping("/combates")
    public String combates(Model model,
                           @RequestParam(value = "estado", defaultValue = "Programado") String estado,
                           @RequestParam(value = "busqueda", required = false) String busqueda,
                           Principal principal) {

        // Obtener el usuario autenticado
        Usuario user = userService.findByUsername(principal.getName());

        // Obtener la lista de combates
        List<Combate> combates = combateService.getCombatesfoUsers(estado, busqueda);

        // Verificar si el usuario ha apostado en cada combate
        combates.forEach(combate -> {
            boolean haApostado = apuestaService.haApostado(user.getId(), combate.getId());
            combate.setHaApostado(haApostado);  // Se debe agregar un campo booleano "haApostado" en la entidad Combate
        });

        // Pasar la lista de combates al modelo
        model.addAttribute("combates", combates);
        model.addAttribute("user", user);  // Pasar también el usuario autenticado

        return "combates";  // Retornar la vista combates.html
    }
    

    @PostMapping("/apostar")
    public String realizarApuesta(
        @Valid @ModelAttribute("apuestaRequest") ApuestaRequest apuestaRequest,
        @RequestParam("combateId") Long combateId,  // Agregar esto para probar
        BindingResult bindingResult, Model model, Principal principal, RedirectAttributes redirectAttributes) {
    
        System.out.println("ID del combate recibido por parámetro: " + combateId);
        System.out.println("ID del combate recibido en ApuestaRequest: " + apuestaRequest.getCombateId());
        
        if (bindingResult.hasErrors()) {
            System.out.println("Errores en la validación del formulario: " + bindingResult.getAllErrors());
            return "r_apuesta"; // Devuelve el formulario con errores
        }
    
        String resultado = apuestaService.realizarApuesta(apuestaRequest.getCombateId(),
                apuestaRequest.getRobotApostadoId(), apuestaRequest.getMonto(), principal);
    
        redirectAttributes.addFlashAttribute("mensaje", resultado);
        return "redirect:/user/combates";
    }
    
    @GetMapping("/hacerapuesta")
    public String ludopatia(@RequestParam("id") Long combateId, Model model) {
        System.out.println("Entrando en ludopatia...");
        System.out.println("ID del combate recibido: " + combateId);
    
        // Supongamos que obtienes el objeto 'Combate' a partir del ID proporcionado.
        Combate combate = combateService.findById(combateId);
        System.out.println("Combate obtenido: " + (combate != null ? combate.toString() : "Combate no encontrado"));
    
        model.addAttribute("combate", combate);
    
        model.addAttribute("apuestaRequest", new ApuestaRequest());
    
        return "r_apuesta";
    }
    

    @GetMapping("/apuestas")
    public String mostrarApuestasUsuario(
            Principal principal,
            @RequestParam(name = "estado", required = false, defaultValue = "Pendiente") String estado,
            @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            Model model) {
        
        List<Apuesta> apuestas = apuestaService.getApuestasUser(principal, estado, fechaInicio, fechaFin);
        
        model.addAttribute("apuestas", apuestas);
        
        return "apuestas"; // Nombre de la vista Thymeleaf (apuestas.html)
    }
    
    
}
