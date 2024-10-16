package com.Proyecto.Final.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.CombateRequest;
import com.Proyecto.Final.Entity.Apuesta;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.ApuestaRepository;
import com.Proyecto.Final.Repository.CombateRepository;
import com.Proyecto.Final.Repository.PersonaRepository;
import com.Proyecto.Final.Repository.RobotRepository;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class CombateService{
    @Autowired
    private CombateRepository combateRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ApuestaRepository apuestaRepository;

    //Logica de busquedas
    
    public List<Combate> getCombatesfoUsers(String estado, String busqueda){
        if(busqueda != null && !busqueda.isEmpty()){
            return combateRepository.findByRobot1NombreContainingAndEstadoContainingOrRobot2NombreContainingAndEstadoContaining(busqueda, estado, busqueda, estado);
        }else{
            return combateRepository.findByEstadoOrderByFechacombateAsc(estado, PageRequest.of(0,10));
        }
    }

    public List<Combate> getCombatesForModerators(Principal principal, String estado, String busqueda) {

        Persona moderador = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (busqueda != null && !busqueda.isEmpty()) {
            return combateRepository.findByOrganizadoPorAndEstadoAndRobot1NombreContainingOrOrganizadoPorAndEstadoAndRobot2NombreContaining(
                    moderador, estado, busqueda, moderador, estado, busqueda);
        } else {
            return combateRepository.findByOrganizadoPorAndEstadoOrderByFechacombateAsc(moderador, estado, PageRequest.of(0, 10));
        }
    }
    public List<Combate> getCombatesForAdmin(String estado, String busqueda, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (estado.equals("Eliminado")) {
            return combateRepository.findByEstadoAndFechacombateBetweenAndCombateEliminadoPorNombreContaining(
                    estado, fechaInicio, fechaFin, busqueda);
        }
    
        if (busqueda != null && !busqueda.isEmpty()) {
            if (fechaInicio != null && fechaFin != null) {
                return combateRepository.findByEstadoAndFechacombateBetweenAndRobot1NombreContainingOrRobot2NombreContainingOrOrganizadoPorNombreContaining(
                        estado, fechaInicio, fechaFin, busqueda, busqueda, busqueda);
            }
            // Si no hay fechas, solo buscar por nombres de robots u organizadores
            return combateRepository.findByEstadoAndRobot1NombreContainingOrRobot2NombreContainingOrOrganizadoPorNombreContaining(
                    estado, busqueda, busqueda, busqueda);
        }
    
        // Si no se proporciona búsqueda, filtrar solo por estado y fechas (si están presentes)
        if (fechaInicio != null && fechaFin != null) {
            return combateRepository.findByEstadoAndFechacombateBetween(
                    estado, fechaInicio, fechaFin);
        }
    
        // Si no hay búsqueda ni fechas, solo buscar por estado
        return combateRepository.findByEstadoOrderByFechacombateAsc(estado, PageRequest.of(0,10));
    }
    

    public Combate findById(Long id){
        return combateRepository.findById(id).get();
    }

    public Combate sizeCombate(Long id) {
        Combate combate = combateRepository.findById(id).orElse(null);
        if (combate != null) {
            // Inicializa las apuestas para el combate
            combate.getApuestas().size(); // Para forzar la carga de apuestas
        }
        return combate;
    }

    public String convertirDateTime(CombateRequest combateRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String auxiliar = combateRequest.getFechacombate().format(formatter);
        return auxiliar;
    }

    public void ocultar_combate(long id, Principal principal){
        Persona usuario = personaRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Obtener el combate que se va a cancelar
        Combate combate = combateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Combate no encontrado"));

        // Cambiar el estado del combate a eliminado
        combate.setEstado("Eliminado");
        combate.setEliminadoVetado(true);
        combate.setCombateEliminadoPor(usuario);

        // Cambiar el estado de los robots a disponible
        Robot robot1 = combate.getRobot1();
        Robot robot2 = combate.getRobot2();
        if (robot1 != null) {
            robot1.setEstado("Disponible");
            robotRepository.save(robot1);
        }
        if (robot2 != null) {
            robot2.setEstado("Disponible");
            robotRepository.save(robot2);
        }

        // Procesar las apuestas
        List<Apuesta> apuestas = apuestaRepository.findByCombate(combate);
        for (Apuesta apuesta : apuestas) {
            // Devolver el dinero de la apuesta
            Usuario apostador = apuesta.getUsuario();
            double cantidadApostada = apuesta.getMonto(); // Asumiendo que tienes un campo cantidad

            // Actualizar saldo del usuario
            apostador.setSaldo(apostador.getSaldo() + cantidadApostada);
            userRepository.save(apostador);

            // Cambiar el estado de la apuesta
            apuesta.setEstado("Devuelta"); // Asumiendo que tienes un estado "Devuelta"
            apuestaRepository.save(apuesta);
        }

        // Guardar el combate con su nuevo estado
        combateRepository.save(combate);
    }

    //Actualización y registro de nuevos combates
    public CombateRequest ActualData(Combate combate){
        CombateRequest combateRequest = new CombateRequest();
        combateRequest.setId(combate.getId());
        combateRequest.setEstado(combate.getEstado());
        combateRequest.setFechacombate(combate.getFechacombate());
        combateRequest.setAuxiliar_fecha(convertirDateTime(combateRequest));
        combateRequest.setRobot1_id(combate.getRobot1().getId());
        combateRequest.setRobot2_id(combate.getRobot2().getId());
        return combateRequest;
    }

    public Combate saveCombate(List<Long> robotsSeleccionados, CombateRequest combateRequest, Principal principal){

        Persona usuario = personaRepository.findByUsername(principal.getName())            
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Combate combate;

        if(combateRequest.getId()== null){
            combate = new Combate();
            combateRequest.setAuxiliar_fecha(null);
        }else{
            combate = combateRepository.findById(combateRequest.getId())
            .orElseThrow(() -> new IllegalArgumentException("Combate no encontrado para ID: " + combateRequest.getId()));
        }

        combateRequest.setRobot1_id(robotsSeleccionados.get(0));
        combateRequest.setRobot2_id(robotsSeleccionados.get(1));

        Robot robot1 = robotRepository.findById(combateRequest.getRobot1_id())
        .orElseThrow(() -> new IllegalArgumentException("Robot 1 no encontrado"));

        Robot robot2 = robotRepository.findById(combateRequest.getRobot2_id())
        .orElseThrow(() -> new IllegalArgumentException("Robot 2 no encontrado"));

        combate.setOrganizadoPor(usuario);
        
        combate.setRobot1(robot1);
        combate.setRobot2(robot2);

        combate.setFechacombate(combateRequest.getFechacombate());
        
        combate.setEstado(combateRequest.getId() == null ? "Programado" : combateRequest.getEstado());

        try {
            robot1.setEstado("Ocupado");
            robot2.setEstado("Ocupado");
            robotRepository.save(robot1);
            robotRepository.save(robot2);
            return combateRepository.save(combate);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el combate: " + e.getMessage());
        }
    }
}
