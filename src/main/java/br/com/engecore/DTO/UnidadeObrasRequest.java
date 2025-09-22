package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeObrasRequest {
    Optional<Integer> totalUnidades;
    Optional<Integer> unidadesConcluidas;

}
