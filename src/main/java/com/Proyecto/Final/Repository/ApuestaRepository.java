package com.Proyecto.Final.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Apuesta;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Usuario;

public interface ApuestaRepository extends JpaRepository<Apuesta, Long> {
    //Listas para los hitoriales de usuario
    List<Apuesta> findByUsuarioAndEstadoOrderByFechaActualizacion(Usuario usuario, String estado, Pageable pageable);

    List<Apuesta> findByUsuarioAndEstadoAndFechaActualizacionBetweenOrderByFechaCreacion(Usuario usuario, String estado, Date fechaInicio, Date fechaFin);

    //Listas para los historiales de administrador
    List<Apuesta> findByEstadoOrderByFechaActualizacion(String estado, Pageable pageable);

    List<Apuesta> findByUsuarioUsernameAndEstadoOrderByFechaActualizacion(String username, String estado, Pageable pageable);

    List<Apuesta> findByUsuarioUsernameAndEstadoAndFechaActualizacionBetweenOrderByFechaActualizacion(String username, String estado, Date fechaInicio, Date fechaFin);
    
    List<Apuesta> findByCombate(Combate combate);

    List<Apuesta> findByCombateId(Long id);

    boolean existsByUsuarioIdAndCombateId(Long userId, Long combateId);

}
