package com.Proyecto.Final.Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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

    private String motivoeliminacion;

    private boolean eliminadoVetado;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechacombate;

    @CreationTimestamp
    private Date fechaCreacion;

    @UpdateTimestamp
    private Date fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "creado_usuario_id")
    private Persona organizadoPor;

    @ManyToOne
    @JoinColumn(name = "eliminado_usuario_id")
    private Persona CombateEliminadoPor;

    @OneToMany(mappedBy="combate")
    private List<Apuesta> apuestas;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return fechacombate != null ? fechacombate.format(formatter) : "Fecha no disponible";
    }
}
