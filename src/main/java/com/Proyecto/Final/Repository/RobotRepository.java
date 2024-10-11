package com.Proyecto.Final.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Robot;
import java.util.List;


public interface RobotRepository extends JpaRepository <Robot, Long>{

    List<Robot> findByEstadoAndEliminadoDescalificadoFalseOrderByVictoriasDesc(String Estado, Pageable pageable);

    List<Robot> findByEstadoOrderByVictoriasDesc(String Estado, Pageable pageable);
    
    List<Robot> findByEstadoAndNombreContainingOrEstadoAndJugadorContaining(String estado, String busqueda, String estado2, String busqueda2);
    
    List<Robot> findByEstado(String estado);
}
