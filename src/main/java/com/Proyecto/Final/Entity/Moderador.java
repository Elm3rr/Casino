package com.Proyecto.Final.Entity;

import java.util.List;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Moderador extends Persona{
    private List<Usuario> usuariosSuspendidos;
    private List<Robot> robotscreados;
    private List<Combate> combatesCreados;
    
}
