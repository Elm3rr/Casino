package com.Proyecto.Final.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Robot;
import java.util.List;


public interface RobotRepository extends JpaRepository <Robot, Long>{
    List<Robot> findByDescalificadoFalseOrderByVictoriasDesc(Pageable pageable);
    List<Robot> findByNombreContainingIgnoreCase(String nombre);
    List<Robot> findByOcupadoFalse();
}
