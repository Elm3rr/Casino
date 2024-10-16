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
            if (combate == null || !"pendiente".equalsIgnoreCase(combate.getEstado())) {
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
}
