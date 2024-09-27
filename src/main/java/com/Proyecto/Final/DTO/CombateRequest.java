package com.Proyecto.Final.DTO;

import java.time.LocalDateTime;

import com.Proyecto.Final.Entity.Robot;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombateRequest{

    private Long id;
    @NotNull(message="El robot es obligatorio")
    private Long robot1_id;
    @NotNull(message = "El robot es obligatorio")
    private Robot robot2_id;
    @NotNull(message="El estado del combate es obligatorio")
    private String estado;

    @NotNull(message = "La fecha del combate es obligatoria")
    @Future(message = "La fecha del combate debe ser en el futuro")
    private LocalDateTime fechacombate;
}
