package com.Proyecto.Final.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy =  InheritanceType.JOINED)
@NoArgsConstructor
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, unique=true)
    private String cui;

    @Column(nullable = false, unique=true)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private Date fecha_creacion;
}
