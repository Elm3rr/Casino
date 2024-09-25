package com.Proyecto.Final.DTO;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotRequest {
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    private int Victorias;
    private int Derrotas;
    private boolean descalificado;
    @DecimalMin(value = "0.0", inclusive = false, message = "La altura debe ser mayor a 0")
    @DecimalMax(value = "40.0", message = "La altura no puede superar los 40 cm")
    private double altura;

    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    @DecimalMax(value = "22.68", message = "El peso no puede superar las 50 libras (22.68 kg)")
    private double peso;

    @DecimalMin(value = "0.0", inclusive = false, message = "La envergadura debe ser mayor a 0")
    @DecimalMax(value = "40.0", message = "La envergadura no puede superar los 40 cm")
    private double envergadura;

    @NotBlank(message = "El nombre del jugador no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre del jugador debe tener entre 2 y 50 caracteres")
    private String jugador;
    
    @NotEmpty(message = "Debe proporcionar una lista de armas")
    private List<String> armas;

    private MultipartFile image;
}
