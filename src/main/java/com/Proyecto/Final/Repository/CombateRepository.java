package com.Proyecto.Final.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Combate;

public interface CombateRepository extends JpaRepository<Combate, Long>{
    List<Combate> findByEstadoAndEliminadoVetadoFalseOrderByFechacombateAsc(String estado, Pageable pageable);
    List<Combate> findByRobot1NombreContainingAndEstadoContainingOrRobot2NombreContainingAndEstadoContaining(String nombre1, String estado1, String nombre2, String estado2);
}
