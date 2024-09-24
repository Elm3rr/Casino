package com.Proyecto.Final.DTO;

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

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Email no válido")
    private String email;

    @NotBlank(message = "El email no puede estar vacío")
    private String confirmEmail;
        
    @Size(min = 13, max = 13, message = "El CUI debe de tener exactamente 13 dígitos")
    @NotBlank(message = "El CUI no puede estar vacío")
    private String cui;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 5, max=15, message = "El nombre de usuario debe tener una longitud entre 5 y 15 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe de tener minimo 8 caracteres")
    private String password;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String confirmPassword;
}
