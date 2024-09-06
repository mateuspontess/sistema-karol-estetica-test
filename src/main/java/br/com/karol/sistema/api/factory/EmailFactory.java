package br.com.karol.sistema.api.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.karol.sistema.domain.validator.EmailValidator;
import br.com.karol.sistema.domain.valueobjects.Email;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmailFactory {

    private final List<EmailValidator> validators;


    public Email createEmail(String value) {
        return new Email(value, validators);
    }
    
    public Email toEmailOrNull(String value) {
        if (value != null && !value.isBlank())
            return new Email(value, validators);
        else return null;
    }
}