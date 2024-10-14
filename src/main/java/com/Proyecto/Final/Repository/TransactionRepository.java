package com.Proyecto.Final.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Transaccion;
import com.Proyecto.Final.Entity.Usuario;

public interface TransactionRepository extends JpaRepository <Transaccion, Long> {
    //Listas para los historiales de usuario
    List<Transaccion> findBySolicitadoPorAndTipoAndEstadoOrderByFechaActualizacion(Usuario usuario, String tipo, String estado, Pageable pageable);

    List<Transaccion> findBySolicitadoPorAndTipoAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(Usuario usuario, String tipo, String estado, Date fechaInicio, Date fechaFin);

    //Listas para los historiales de administrador
    List<Transaccion>findByTipoAndEstadoOrderByFechaActualizacion(String tipo, String estado, Pageable pageable);

    List<Transaccion> findBySolicitadoPorUsernameAndTipoAndEstadoOrderByFechaActualizacion(String username, String tipo, String estado, Pageable pageable);
    
    List<Transaccion> findByTipoAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(String tipo, String estado, Date fechaInicio, Date fechaFin);

    List<Transaccion> findBySolicitadoPorUsernameAndTipoAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(String username, String tipo, String estado, Date fechaInicio, Date fechaFin);
}
