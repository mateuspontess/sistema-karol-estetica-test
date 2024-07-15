package br.com.karol.sistema.api.dto.cliente;

import br.com.karol.sistema.api.dto.EnderecoDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CriarClienteDTO {

    @NotBlank
    @Size(min = 11, max = 11)
    private String cpf;

    @NotBlank
    private String nome;

    @NotBlank
    @Size(min = 11, max = 13)
    private String telefone;

    @NotBlank
    private String email;

    @NotNull
    private EnderecoDTO endereco;
}