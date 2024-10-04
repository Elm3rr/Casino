package com.Proyecto.Final.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Combate;

public interface CombateRepository extends JpaRepository<Combate, Long>{
    List<Combate> findByEstadoAndEliminadoFalseOrderByFechacombateAsc(String estado, Pageable pageable);
    List<Combate> findByRobot1NombreContainingOrRobot2NombreContaining(String robot1nombre, String robot2nombre);

}
