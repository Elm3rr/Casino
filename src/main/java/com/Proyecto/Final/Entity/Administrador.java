package com.Proyecto.Final.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Administrador extends Persona {
    //Vetado y eliminado, de usuarios y moderadores
    @OneToMany(mappedBy = "vetadoporAdministrador", cascade = CascadeType.ALL)
    private List<Moderador> moderadoresSuspendidos;

    @OneToMany(mappedBy = "vetadoPor", cascade = CascadeType.ALL)
    private List<Usuario> usuariosSuspendidos;

    //Autorizaciones y creaciones
    @OneToMany(mappedBy="autorizadoPor", cascade=CascadeType.ALL)
    private List<Transaccion> transaccionesAutorizadas;
    
    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL)
    private List<Robot> robotsCreados;

    @OneToMany(mappedBy = "organizadoPor", cascade = CascadeType.ALL)
    private List<Combate> combatesCreados;

    @OneToMany(mappedBy = "ModeradorcreadoPor", cascade = CascadeType.ALL)
    private List<Moderador> ModeradoresCreados;

    //Eliminado del combate y robots
    @OneToMany(mappedBy = "RobotEliminadoPor", cascade = CascadeType.ALL)
    private List<Robot> robotsEliminados;

    @OneToMany(mappedBy = "combateEliminadoPor", cascade = CascadeType.ALL)
    private List<Combate> combatesEliminados;
    
}
