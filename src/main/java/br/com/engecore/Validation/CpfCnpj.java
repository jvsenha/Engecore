package br.com.engecore.Validation;

import jakarta.validation.Payload;

public  @interface CpfCnpj {
    String message() default "CPF ou CNPJ inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}