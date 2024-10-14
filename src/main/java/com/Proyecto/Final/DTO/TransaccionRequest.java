package com.Proyecto.Final.DTO;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionRequest {

    @NotBlank(message = "El número de boleta no puede estar vacío")
    private String numBoleta;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "100.00", inclusive = true, message = "El monto debe ser mayor a 100")
    private Double monto;

    @NotNull(message = "La fecha de la boleta es obligatoria")
    @PastOrPresent(message = "La fecha de la boleta debe ser de hoy o días anteriores")
    private Date fechaBoleta;

    @NotNull(message = "La imagen no debe estar en blanco")
    private MultipartFile image;

    @NotNull(message = "El numero de cuenta no puede ir vacio")
    private String ctaBanco;

    @NotNull(message = "El banco es obligatorio")
    private String Banco;
    
    private String tipo;

}
