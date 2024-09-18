package com.Proyecto.Final.Entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Apostador extends Persona {
    private double saldo;
    private boolean activo;   
     
}
