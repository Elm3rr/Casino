package com.Proyecto.Final.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.TransaccionRequest;
import com.Proyecto.Final.Entity.Administrador;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Entity.Transaccion;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.PersonaRepository;
import com.Proyecto.Final.Repository.TransactionRepository;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UserRepository userRepository;


    //Logica para aprobar Transacciones
    public Transaccion aprobar_transaccion(Long id, String nombreImagen, Principal principal){
        Transaccion transaccion = transactionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));

        if("Aprobada".equals("transaccion")){
            throw new IllegalStateException("La transacción ya está aprobada");
        }

        Persona persona = personaRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado"));

        Usuario usuario = transaccion.getSolicitadoPor();

        if("Recarga".equals(transaccion.getTipo())){
            usuario.setSaldo(usuario.getSaldo()+transaccion.getMonto());
        }else if("Retiro".equals(transaccion.getTipo())){
            usuario.setSaldo(usuario.getSaldo()-transaccion.getMonto());
            transaccion.setImagenBoleta(nombreImagen);
        }

        transaccion.setEstado("Aprobada");
        transaccion.setAutorizadoPor((Administrador) persona);

        userRepository.save(usuario);
        return transactionRepository.save(transaccion);
    }

    //Logica para rechazar Transacciones
    public Transaccion rechazar_transaccion(Long id, String motivoRechazo){
        Transaccion transaccion = transactionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));
            
            if("Rechazada".equals("transaccion")){
                throw new IllegalStateException("La transacción ya está rechazada");
            }

            transaccion.setEstado("Rechazada");
            transaccion.setMotivoRechazo(motivoRechazo);

            return transactionRepository.save(transaccion);
    }

    //Logica de Historiales
    public List<Transaccion> getTransaccionesUser(Principal principal, String tipo, String estado, Date fechaInicio, Date fechaFin){
        
        Usuario usuario = userRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if(fechaInicio==null||fechaFin==null){
            return transactionRepository.findByUsuarioAndTipoAndEstadoOrderByFechaActualizacion(usuario, tipo, estado, PageRequest.of(0, 10));
        }else{
            return transactionRepository.findByUsuarioAndTipoAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(usuario, tipo, estado, fechaInicio, fechaFin);
        }
    }

    public List<Transaccion> getTransaccionesAdmin(String tipo, String estado, String username, Date fechaInicio, Date fechaFin){
        if(username !=null && fechaInicio != null && fechaFin != null){
            return transactionRepository.findByUsuarioUsernameAndTipoAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(username, tipo, estado, fechaInicio, fechaFin);
        }

        if (fechaInicio != null && fechaFin != null){
            return transactionRepository.findByTipoAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(tipo, estado, fechaInicio, fechaFin);
        }

        if(username!=null){
            return transactionRepository.findByUsuarioUsernameAndTipoAndEstadoOrderByFechaActualizacion(username, tipo, estado,PageRequest.of(0, 10));
        }
        
        return transactionRepository.findByTipoAndEstadoOrderByFechaActualizacion(tipo, estado, PageRequest.of(0, 10));
    }


    //Logica para guardar nuevas transacciones
    public Transaccion saveTransaction(TransaccionRequest transaccionRequest, String nombreImagen, Principal principal ){

        Persona persona = personaRepository.findByUsername(principal.getName())            
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Transaccion transaccion = new Transaccion();
        if("Recarga".equals(transaccionRequest.getTipo())){

            transaccion.setFechaBoleta(transaccionRequest.getFechaBoleta());
            transaccion.setImagenBoleta(nombreImagen);
        }else{
            transaccion.setBanco(transaccionRequest.getBanco());
            transaccion.setCtaBanco(transaccionRequest.getCtaBanco());
        }

        transaccion.setMonto(transaccionRequest.getMonto());
        transaccion.setEstado("Pendiente");
        transaccion.setTipo(transaccionRequest.getTipo());
        transaccion.setSolicitadoPor((Usuario) persona);

        return transactionRepository.save(transaccion);
    }

}
