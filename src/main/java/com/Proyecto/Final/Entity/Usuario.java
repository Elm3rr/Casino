package com.Proyecto.Final.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario extends Persona {
    
    private double saldo;

    @OneToMany(mappedBy="usuario", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Apuesta> historialApuestas;

    private Date fechaUltimaApuesta;

    private Double totalGanancias;

    private Double totalPerdidas;
}
