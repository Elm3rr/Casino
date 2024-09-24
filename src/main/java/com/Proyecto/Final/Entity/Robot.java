package com.Proyecto.Final.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int victorias;
    private int derrotas;
    private double altura;
    private double peso;
    private double envergadura;
    private int n_armas;
    private boolean descalificado;
    private String jugador;
    private Date fecha_creacion;

    @OneToMany(mappedBy = "robot1")
    private List<Combate> player1;

    @OneToMany(mappedBy = "robot2")
    private List<Combate> player2;


    
}
