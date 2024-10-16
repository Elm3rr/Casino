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
    public String realizarApuesta(Long combateId, Long robotId, Double monto, Principal principal){
        try{
            Combate combate = combateService.sizeCombate(combateId);
            if (combate == null || !"Programado".equalsIgnoreCase(combate.getEstado())) {
                return "No se puede realizar la apuesta, el combate no está pendiente.";
            }
            Usuario usuario = userRepository.findByUsername(principal.getName()).get();
        
            if(usuario.getSaldo()<=monto){
                return "No tienes suficiente saldo para realizar esta apuesta.";
            }

            Apuesta apuesta = new Apuesta();
            apuesta.setRobotApostado(robotRepository.findById(robotId).get());
            apuesta.setCombate(combate);
            apuesta.setMonto(monto);
            apuesta.setUsuario(usuario);

            usuario.setSaldo(usuario.getSaldo()-monto);
            userRepository.save(usuario);
            return "Apuesta realizada con éxito";
        }catch (Exception e) {
            e.printStackTrace(); // Para depuración
            return "Ocurrió un error al realizar la apuesta: " + e.getMessage();
        }
    }

    public void guardarGanador(Long combateId, Long ganadorId) {
        try {
            // Guardar el ganador
            Combate combate = combateService.sizeCombate(combateId);
            combate.setIdGanador(ganadorId); // Suponiendo que tienes un método para establecer el ganador
            combateRepository.save(combate);

            // Calcular el monto total apostado
            double montoTotalApostado = calcularMontoTotalApostado(combateId);

            // Calcular el monto a repartir y el porcentaje de la casa
            double porcentajeCasa = montoTotalApostado * 0.10;
            double montoAPagar = montoTotalApostado - porcentajeCasa;

            // Obtener las apuestas realizadas en este combate
            List<Apuesta> apuestas = apuestaRepository.findByCombateId(combateId);

            // Pagar a los ganadores
            for (Apuesta apuesta : apuestas) {
                if (apuesta.getRobotApostado().getId().equals(ganadorId)) {
                    // Calcular la ganancia para este usuario
                    double ganancia = (apuesta.getMonto() / montoTotalApostado) * montoAPagar;
                    Usuario usuario = apuesta.getUsuario();
                    usuario.setSaldo(usuario.getSaldo() + ganancia);
                    userRepository.save(usuario);
                }
            }
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
