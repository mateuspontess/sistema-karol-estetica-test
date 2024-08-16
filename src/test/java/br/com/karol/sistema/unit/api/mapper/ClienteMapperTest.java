package br.com.karol.sistema.unit.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.karol.sistema.api.dto.CriarUsuarioClienteDTO;
import br.com.karol.sistema.api.dto.EnderecoDTO;
import br.com.karol.sistema.api.dto.cliente.CriarClienteDTO;
import br.com.karol.sistema.api.mapper.ClienteMapper;
import br.com.karol.sistema.api.mapper.CpfMapper;
import br.com.karol.sistema.api.mapper.EmailMapper;
import br.com.karol.sistema.api.mapper.EnderecoMapper;
import br.com.karol.sistema.api.mapper.TelefoneMapper;
import br.com.karol.sistema.builder.UsuarioFactory;
import br.com.karol.sistema.domain.Cliente;
import br.com.karol.sistema.domain.Endereco;
import br.com.karol.sistema.domain.Usuario;
import br.com.karol.sistema.domain.valueobjects.Cpf;
import br.com.karol.sistema.domain.valueobjects.Email;
import br.com.karol.sistema.domain.valueobjects.Telefone;
import br.com.karol.sistema.utils.ClienteUtils;

@ExtendWith(MockitoExtension.class)
public class ClienteMapperTest {

    private static final Cliente DEFAULT = ClienteUtils.getCliente();
    private static final Usuario USUARIO_DEFAULT = UsuarioFactory.getUsuario();
    private static Cpf cpfDefault;
    private static Telefone telefoneDefault;
    private static Email emailDefault;
    private static Endereco enderecoDefault;

    @Mock
    private CpfMapper cpfMapper;
    @Mock
    private TelefoneMapper telefoneMapper;
    @Mock
    private EmailMapper emailMapper;
    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private ClienteMapper mapper;

    @BeforeAll
    static void setup() {
        cpfDefault = (Cpf) ReflectionTestUtils.getField(DEFAULT, "cpf");
        telefoneDefault = (Telefone) ReflectionTestUtils.getField(DEFAULT, "telefone");
        emailDefault = (Email) ReflectionTestUtils.getField(DEFAULT, "email");
        enderecoDefault = (Endereco) ReflectionTestUtils.getField(DEFAULT, "endereco");

        ReflectionTestUtils.setField(DEFAULT, "usuario", USUARIO_DEFAULT);
    }

    @Test
    void testToCliente() {
        // arrange
        EnderecoDTO enderecoDTO = new EnderecoDTO(DEFAULT.getEndereco());
        CriarClienteDTO dto = new CriarClienteDTO(
            DEFAULT.getCpf(), 
            DEFAULT.getNome(), 
            DEFAULT.getTelefone(), 
            DEFAULT.getEmail(), 
            enderecoDTO
        );

        when(cpfMapper.toCpf(anyString())).thenReturn(cpfDefault);
        when(telefoneMapper.toTelefone(anyString())).thenReturn(telefoneDefault);
        when(emailMapper.toEmail(anyString())).thenReturn(emailDefault);
        when(enderecoMapper.toEndereco(any())).thenReturn(enderecoDefault);

        // act
        Cliente result = mapper.toCliente(dto);

        // assert
        assertEquals(dto.getCpf(), result.getCpf());
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getTelefone(), result.getTelefone());
        assertEquals(dto.getEmail(), result.getEmail());
    }

    @Test
    void testToClienteComUsuario() {
        // arrange
        EnderecoDTO enderecoDTO = new EnderecoDTO(DEFAULT.getEndereco());
        CriarUsuarioClienteDTO dto = new CriarUsuarioClienteDTO(
            DEFAULT.getNome(), 
            USUARIO_DEFAULT.getLogin(),
            USUARIO_DEFAULT.getSenha(),
            DEFAULT.getCpf(), 
            DEFAULT.getTelefone(), 
            enderecoDTO,
            DEFAULT.getEmail()
        );

        when(cpfMapper.toCpf(anyString())).thenReturn(cpfDefault);
        when(telefoneMapper.toTelefone(anyString())).thenReturn(telefoneDefault);
        when(emailMapper.toEmail(anyString())).thenReturn(emailDefault);
        when(enderecoMapper.toEndereco(any())).thenReturn(enderecoDefault);

        // act
        Cliente result = mapper.toClienteComUsuario(dto, USUARIO_DEFAULT);

        // assert
        assertEquals(dto.getCpf(), result.getCpf());
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getTelefone(), result.getTelefone());
        assertEquals(dto.getEmailConfirmationToken(), result.getEmail());
        assertEquals(USUARIO_DEFAULT.getLogin(), result.getUsuario().getLogin());
        assertEquals(USUARIO_DEFAULT.getSenha(), result.getUsuario().getSenha());
    }

    @Test
    void testToDadosCompletosClienteDTO() {
        // arrange
        Cliente entry = DEFAULT;

        // act
        var result = mapper.toDadosCompletosClienteDTO(entry);

        // assert
        assertEquals(entry.getId(), result.getId());
        assertEquals(entry.getCpf(), result.getCpf());
        assertEquals(entry.getNome(), result.getNome());
        assertEquals(entry.getTelefone(), result.getTelefone());
        assertEquals(entry.getEmail(), result.getEmail());
        assertNotNull(result.getEndereco());
    }

    @Test
    void testToDadosClienteDTO() {
        // arrange
        Cliente entry = DEFAULT;

        // act
        var result = mapper.toDadosClienteDTO(entry);

        // assert
        assertEquals(entry.getId(), result.getId());
        assertEquals(entry.getCpf(), result.getCpf());
        assertEquals(entry.getNome(), result.getNome());
    }

    @Test
    void testToPageDadosClienteDTO() {
        // arrange
        Page<Cliente> entry = new PageImpl<>(List.of(DEFAULT));

        // act
        var result = mapper.toPageDadosClienteDTO(entry);

        // assert
        assertEquals(entry.getContent().get(0).getId(), result.getContent().get(0).getId());
        assertEquals(entry.getContent().get(0).getCpf(), result.getContent().get(0).getCpf());
        assertEquals(entry.getContent().get(0).getNome(), result.getContent().get(0).getNome());
    }

    @Test
    void testToIdNomeEmailClienteDTO() {
        // arrange
        Cliente entry = DEFAULT;

        // act
        var result = mapper.toDadosClienteDTO(entry);

        // assert
        assertEquals(entry.getId(), result.getId());
        assertEquals(entry.getCpf(), result.getCpf());
        assertEquals(entry.getNome(), result.getNome());
    }
}