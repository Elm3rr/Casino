package com.Proyecto.Final.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Persona;

public interface CombateRepository extends JpaRepository<Combate, Long> {
    
    // Búsqueda por estado y ordenado por fecha para usuarios
    List<Combate> findByEstadoOrderByFechacombateAsc(String estado, Pageable pageable);

    // Búsqueda por nombres de robots y estado (puede buscar por cualquiera de los dos robots)
    List<Combate> findByRobot1NombreContainingAndEstadoContainingOrRobot2NombreContainingAndEstadoContaining(
        String nombre1, String estado1, String nombre2, String estado2);
    
    // Búsqueda por organizador y estado para moderadores
    List<Combate> findByOrganizadoPorAndEstadoOrderByFechacombateAsc(Persona persona, String estado, Pageable pageable);
    
    // Búsqueda avanzada por organizador y robot para moderadores
    List<Combate> findByOrganizadoPorAndEstadoAndRobot1NombreContainingOrOrganizadoPorAndEstadoAndRobot2NombreContaining(
        Persona persona1, String estado1, String busqueda1, Persona persona2, String estado2, String busqueda2);
    
    // Lógica de búsqueda para administradores:
    
    // Buscar por estado y entre fechas sin búsqueda
    List<Combate> findByEstadoAndFechacombateBetween(String estado, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar por estado, fechas y nombres de robots u organizador
    List<Combate> findByEstadoAndFechacombateBetweenAndRobot1NombreContainingOrRobot2NombreContainingOrOrganizadoPorNombreContaining(
        String estado, LocalDateTime fechaInicio, LocalDateTime fechaFin, String nombreRobot1, String nombreRobot2, String nombreOrganizador);
    
    // Buscar combates eliminados filtrando por quién los eliminó
    List<Combate> findByEstadoAndFechacombateBetweenAndCombateEliminadoPorNombreContaining(
        String estado, LocalDateTime fechaInicio, LocalDateTime fechaFin, String eliminadoPor);

    List<Combate> findByEstadoAndRobot1NombreContainingOrRobot2NombreContainingOrOrganizadoPorNombreContaining(String estado, String busqueda, String busqueda2, String busqueda3);
}
