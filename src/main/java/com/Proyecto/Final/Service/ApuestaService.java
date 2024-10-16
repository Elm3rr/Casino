package com.Proyecto.Final.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.Entity.Apuesta;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.ApuestaRepository;
import com.Proyecto.Final.Repository.CombateRepository;
import com.Proyecto.Final.Repository.RobotRepository;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class ApuestaService {

    @Autowired
    private ApuestaRepository apuestaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CombateService combateService;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private CombateRepository combateRepository;


    public boolean haApostado(Long userId, Long combateId) {
        return apuestaRepository.existsByUsuarioIdAndCombateId(userId, combateId);
    }

    //Logica de Historiales
    public List<Apuesta> getApuestasUser(Principal principal, String estado, Date fechaInicio, Date fechaFin){
        
        Usuario usuario = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if(fechaInicio==null||fechaFin==null){
            return apuestaRepository.findByUsuarioAndEstadoOrderByFechaActualizacion(usuario, estado, PageRequest.of(0, 10));
        }else{
            return apuestaRepository.findByUsuarioAndEstadoAndFechaActualizacionBetweenOrderByFechaCreacion(usuario, estado, fechaInicio, fechaFin);
        }
    }

    public List<Apuesta> getApuestasAdmin(String estado, String username, Date fechaInicio, Date fechaFin){
        if(username !=null && fechaFin != null){
            if(fechaInicio !=null && fechaFin != null){
                return apuestaRepository.findByUsuarioUsernameAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(username, estado, fechaInicio, fechaFin);
            }
            return apuestaRepository.findByUsuarioUsernameAndEstadoOrderByFechaActualizacion(username, estado, PageRequest.of(0, 10));
        }
        return apuestaRepository.findByEstadoOrderByFechaActualizacion(estado, PageRequest.of(0, 10));
    }

/*    public Apuesta saveApuesta(ApuestaRequest apuestaRequest)
 */
public String realizarApuesta(Long combateId, Long robotId, Double monto, Principal principal) {
    try {
        // Verificar que el combate existe y está "Programado"
        Combate combate = combateService.sizeCombate(combateId);
        if (combate == null || !"Programado".equalsIgnoreCase(combate.getEstado())) {
            return "No se puede realizar la apuesta, el combate no está pendiente.";
        }

        // Obtener el usuario que realiza la apuesta
        Usuario usuario = userRepository.findByUsername(principal.getName()).orElseThrow(
            () -> new IllegalArgumentException("Usuario no encontrado")
        );

        // Verificar que el usuario tenga saldo suficiente
        if (usuario.getSaldo() < monto) {
            return "No tienes suficiente saldo para realizar esta apuesta.";
        }

        // Crear y configurar la apuesta
        Apuesta apuesta = new Apuesta();
        apuesta.setRobotApostado(robotRepository.findById(robotId).orElseThrow(
            () -> new IllegalArgumentException("Robot no encontrado")
        ));
        apuesta.setCombate(combate);
        apuesta.setMonto(monto);
        apuesta.setUsuario(usuario);
        apuesta.setEstado("Pendiente");

        // Reducir el saldo del usuario y guardar los cambios
        usuario.setSaldo(usuario.getSaldo() - monto);
        userRepository.save(usuario);

        // Guardar la apuesta en el repositorio
        apuestaRepository.save(apuesta);  // <--- Guardar la apuesta

        return "Apuesta realizada con éxito";
    } catch (Exception e) {
        e.printStackTrace(); // Para depuración
        return "Ocurrió un error al realizar la apuesta: " + e.getMessage();
    }
}



    public void guardarGanador(Long combateId, Long ganadorId, Principal principal) {
        try {
            // Guardar el ganador
            Combate combate = combateService.sizeCombate(combateId);
            combate.setIdGanador(ganadorId);
            combateRepository.save(combate);

            // Calcular el monto total apostado
            double montoTotalApostado = calcularMontoTotalApostado(combateId);

            // Calcular el monto a repartir y el porcentaje de la casa
            double porcentajeCasa = montoTotalApostado * 0.10;
            double montoAPagar = montoTotalApostado - porcentajeCasa;

            // Obtener el monto total apostado a los ganadores
            List<Apuesta> apuestas = apuestaRepository.findByCombateId(combateId);
            double montoTotalApostadoAGanador = apuestas.stream()
                .filter(apuesta -> apuesta.getRobotApostado().getId().equals(ganadorId))
                .mapToDouble(Apuesta::getMonto)
                .sum();

            // Pagar a los ganadores
            for (Apuesta apuesta : apuestas) {
                if (apuesta.getRobotApostado().getId().equals(ganadorId)) {
                    // Calcular la ganancia para este usuario
                    double ganancia = (apuesta.getMonto() / montoTotalApostadoAGanador) * montoAPagar;
                    Usuario usuario = apuesta.getUsuario();
                    // Agregar la apuesta original a la ganancia
                    usuario.setSaldo(usuario.getSaldo() + apuesta.getMonto() + ganancia);
                    userRepository.save(usuario);
                }
                // Cambiar el estado de la apuesta a "Finalizada"
                apuesta.setEstado("Finalizada");
                apuestaRepository.save(apuesta);
            }

            // Ocultar el combate después de pagar
            combateService.ocultarCombateDespuesDePago(combateId, principal);

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            throw new RuntimeException("Error al guardar el ganador: " + e.getMessage());
        }
    }

    public double calcularMontoTotalApostado(Long combateId) {
        // Implementa la lógica para calcular el monto total apostado
        List<Apuesta> apuestas = apuestaRepository.findByCombateId(combateId);
        return apuestas.stream().mapToDouble(Apuesta::getMonto).sum();
    }
}
