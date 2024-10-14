package com.Proyecto.Final.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Data
@Entity
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Administrador autorizadoPor;

    @ManyToOne
    private Usuario solicitadoPor;

    private String tipo;
    private String numBoleta;
    private Double monto;
    private Date fechaBoleta;
    private String imagenBoleta;
    private String estado;
    private String motivoRechazo;

    private String banco;
    private String ctaBanco;

    @CreationTimestamp
    private Date fechaCreacion;

    @UpdateTimestamp
    private Date fechaActualizacion;

}
