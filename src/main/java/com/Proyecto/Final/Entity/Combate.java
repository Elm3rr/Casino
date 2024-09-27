package com.Proyecto.Final.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Combate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "robot1_id")
    private Robot robot1;

    @ManyToOne
    @JoinColumn(name = "robot2_id")
    private Robot robot2;

    private String resultado;

    private String estado;

    private LocalDateTime fechacombate;

    private LocalDateTime fecha_creacion;

    @OneToMany(mappedBy="combate")
    private List<Apuesta> apuestas;
}
