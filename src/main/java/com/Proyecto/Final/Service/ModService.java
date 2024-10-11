package com.Proyecto.Final.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.Entity.Moderador;
import com.Proyecto.Final.Repository.ModeradorRepository;

@Service
public class ModService {
    
    @Autowired
    ModeradorRepository moderadorRepository;

    //Logica de busquedas
    public List<Moderador> lista_mod(String estado){
        return moderadorRepository.findByEstadoOrderByFechaCreacion(estado, PageRequest.of(0,10));
    }

    public List<Moderador> buscar_mods(String estado, String busqueda){    
        return moderadorRepository.findByEstadoAndUsernameContainingOrEstadoAndApellidoContaining(estado, busqueda, estado, busqueda);
    }

    //Ocultar y restaurar moderadores
    public void ocultar_moderador(long id, String motivo){
        Moderador moderador = moderadorRepository.findById(id).get();
        moderador.setMotivoEliminacion(motivo);
        moderador.setEstado("Vetado");
        moderador.setEliminadoVetado(true);
    }

    public void restaurar_moderador(long id){
        Moderador moderador = moderadorRepository.findById(id).get();
        moderador.setEliminadoVetado(false);
        moderador.setEstado("Habilitado");
        moderadorRepository.save(moderador);
    }

}
