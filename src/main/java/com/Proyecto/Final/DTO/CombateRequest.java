package com.Proyecto.Final.DTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private Long robot1_id;
    private Long robot2_id;
    private String estado;

    @NotNull(message = "La fecha del combate es obligatoria")
    @Future(message = "La fecha del combate debe ser en el futuro")
    private LocalDateTime fechacombate;

    private String auxiliar_fecha;

        public String getFechaUltimaModificacion() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return fechacombate.format(formatter);
        }
}
