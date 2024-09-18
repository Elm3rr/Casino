package com.Proyecto.Final.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Email no válido")
    @Column(nullable = false, unique=true)
    private String email;
    

    @Column(nullable = false)
    private String role;
    
    @Column(nullable=false, unique=true)
    @Size(min = 13, max = 13, message = "El CUI debe de tener exactamente 13 dígitos")
    private String cui;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 5, max=15, message = "El nombre de usuario debe tener una longitud entre 5 y 15 caracteres")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe de tener minimo 8 caracteres")
    @Column(nullable = false)
    private String password;
}
