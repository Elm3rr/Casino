package com.Proyecto.Final.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApuestaRequest {

    @NotNull(message = "Debes seleccionar un robot.")
    private Long robotApostadoId;

    private Long combateId;
    
    @Min(value = 1, message = "El monto debe ser mayor a 0.")
    private double monto;
}
