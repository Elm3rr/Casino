package com.Proyecto.Final.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(nullable = false)
    private String estado;

    @Column(nullable=false)
    private boolean eliminadoVetado;

    private String motivoEliminacion;

    @Column(nullable = false, unique=true)
    private String cui;

    @Column(nullable = false, unique=true)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    @CreationTimestamp
    private Date fechaCreacion;

    @UpdateTimestamp
    private Date fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona VetadoPor;

    public void setEliminadoVetado(boolean eliminadoVetado) {
        this.eliminadoVetado = eliminadoVetado;
    }
}
