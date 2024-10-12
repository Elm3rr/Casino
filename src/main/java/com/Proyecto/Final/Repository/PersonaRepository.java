package com.Proyecto.Final.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Proyecto.Final.Entity.Persona;

public interface PersonaRepository extends JpaRepository <Persona, Long> {

    Optional<Persona> findByUsername(String username);
    Optional<Persona> findByEmail(String email);
    Optional<Persona> findByCui(String cui);

    List<Persona> findByRoleAndEstadoOrderByFechaCreacion(String role, String estado, Pageable pageable);

    @Query("SELECT u FROM Usuario u WHERE " +
    "(u.role = :role1 AND u.estado = :estado1 AND u.username LIKE %:username%) OR " +
    "(u.role = :role2 AND u.estado = :estado2 AND u.vetadoPor.username LIKE %:vetadoPorUsername%)")
    List<Persona> filtrarPorUsernameVetador(@Param("role1") String role1,
                                     @Param("estado1") String estado1,
                                     @Param("username") String username,
                                     @Param("role2") String role2,
                                     @Param("estado2") String estado2,
                                     @Param("vetadoPorUsername") String vetadoPorUsername);

    List<Persona> findByRoleAndEstadoOrderByFechaActualizacion(String rol, String estado);

    List<Persona> findByRoleAndEstadoAndUsernameContainingOrRoleAndEstadoAndCuiContaining(String role, String estado, String busqueda, String role2, String estado2, String buqueda2);

}
