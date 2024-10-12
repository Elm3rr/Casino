package com.Proyecto.Final.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Moderador extends Persona{
    //Registro del admin que veto al moderador
    @ManyToOne
    @JoinColumn(name = "administrador_id")
    private Administrador vetadoporAdministrador;
    //Usuarios que fueron suspendidos por el moderador
    @OneToMany(mappedBy = "vetadoPor", cascade = CascadeType.ALL)
    private List<Usuario> usuariosSuspendidos;

    //Registro de robots y combates creados por el moderador
    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL)
    private List<Robot> robotsCreados;

    @OneToMany(mappedBy = "organizadoPor", cascade = CascadeType.ALL)
    private List<Combate> combatesCreados;

    //Registro de robots y combates eliminados por el moderador
    @OneToMany(mappedBy = "RobotEliminadoPor", cascade = CascadeType.ALL)
    private List<Robot> robotsEliminados;

    @OneToMany(mappedBy = "CombateEliminadoPor", cascade = CascadeType.ALL)
    private List<Combate> combatesEliminados;

    //Administrador quien lo creo
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Persona ModeradorcreadoPor;

}
