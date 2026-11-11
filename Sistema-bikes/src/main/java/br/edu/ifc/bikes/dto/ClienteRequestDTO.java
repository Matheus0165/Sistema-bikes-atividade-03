package br.edu.ifc.bikes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100)
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Size(min = 11, max = 11)
        @CPF(message = "O CPF deve ser válido")
        String cpf
) {}
