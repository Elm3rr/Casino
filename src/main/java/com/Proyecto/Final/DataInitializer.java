package com.Proyecto.Final;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Service.PersonaService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{
    
    private final PersonaService personaService;

    @Override
    public void run(String... args) throws Exception{
        if(personaService.lista_personas("ROLE_ADMIN", "Habilitado").isEmpty()){
            PersonaRequest masterAdmin = new PersonaRequest();
            masterAdmin.setNombre("Admin");
            masterAdmin.setApellido("Maestro");
            masterAdmin.setEmail("admin@master.com");
            masterAdmin.setUsername("admin");
            masterAdmin.setPassword("Admin.2024");
            masterAdmin.setCui("1234567890123");

            Persona administrador = personaService.newUser(masterAdmin, "ROLE_ADMIN", null);
            System.out.println("Administrador creado exitosamente: " + administrador.getUsername());
        }else{
            System.out.println("Ya existe al menos un administrador. No se ha creado");
        }
    }
}
