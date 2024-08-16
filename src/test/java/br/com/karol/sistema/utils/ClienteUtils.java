package br.com.karol.sistema.utils;

import org.springframework.test.util.ReflectionTestUtils;

import br.com.karol.sistema.domain.Endereco;
import br.com.karol.sistema.domain.valueobjects.Cpf;
import br.com.karol.sistema.domain.valueobjects.Email;
import br.com.karol.sistema.domain.valueobjects.Telefone;

public class ClienteUtils {
    
    public static Cpf getCpf() {
        Cpf cpf = new Cpf();
        ReflectionTestUtils.setField(cpf, "value", "12345678911"); // 11 caracteres
        return cpf;
    }
    public static Cpf getCpf(String value) {
        Cpf cpf = new Cpf();
        ReflectionTestUtils.setField(cpf, "value", value); // 11 caracteres
        return cpf;
    }
    
    public static Telefone getTelefone() {
        Telefone telefone = new Telefone();
        ReflectionTestUtils.setField(telefone, "value", "12345678911"); // 11 caracteres
        return telefone;
    }
    public static Telefone getTelefone(String value) {
        Telefone telefone = new Telefone();
        ReflectionTestUtils.setField(telefone, "value", value); // 11 caracteres
        return telefone;
    }

    public static Email getEmail() {
        Email email = new Email();
        ReflectionTestUtils.setField(email, "value", "cliente@email.com");
        return email;
    }
    public static Email getEmail(String value) {
        Email email = new Email();
        ReflectionTestUtils.setField(email, "value", value);
        return email;
    }

    public static Endereco getEndereco() {
        Endereco endereco = new Endereco();
        ReflectionTestUtils.setField(endereco, "rua", "umaRua");
        ReflectionTestUtils.setField(endereco, "numero", "umNumero");
        ReflectionTestUtils.setField(endereco, "cidade", "umaCidade");
        ReflectionTestUtils.setField(endereco, "bairro", "umBairro");
        ReflectionTestUtils.setField(endereco, "estado", "umEstado");
        return endereco;
    }
}