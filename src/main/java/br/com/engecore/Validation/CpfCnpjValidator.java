package br.com.engecore.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<CpfCnpj, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return false;

        String sanitized = value.replaceAll("[^a-zA-Z0-9]", ""); // remove pontos, traços, barras

        if (sanitized.length() == 11) {
            return validarCPF(sanitized);
        } else if (sanitized.length() == 14) {
            return validarCNPJ(sanitized);
        } else if (sanitized.length() >= 8) {
            return validarCnpjAlfanumerico(sanitized);
        }

        return false;
    }

    private boolean validarCPF(String cpf) {
        if (!cpf.matches("\\d{11}")) return false;

        // Checa se todos os números são iguais
        if (cpf.chars().distinct().count() == 1) return false;

        try {
            int d1 = 0;
            int d2 = 0;
            for (int i = 0; i < 9; i++) {
                int num = Character.getNumericValue(cpf.charAt(i));
                d1 += num * (10 - i);
                d2 += num * (11 - i);
            }

            int verificador1 = (d1 * 10 % 11) % 10;
            int verificador2 = (d2 + verificador1 * 2) * 10 % 11 % 10;

            return verificador1 == Character.getNumericValue(cpf.charAt(9)) &&
                    verificador2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarCNPJ(String cnpj) {
        if (!cnpj.matches("\\d{14}")) return false;

        int[] peso1 = {5,4,3,2,9,8,7,6,5,4,3,2};
        int[] peso2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};

        try {
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso1[i];
            }
            int resto = soma % 11;
            int dig1 = (resto < 2) ? 0 : 11 - resto;

            soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso2[i];
            }
            soma += dig1 * peso2[12];
            resto = soma % 11;
            int dig2 = (resto < 2) ? 0 : 11 - resto;

            return dig1 == Character.getNumericValue(cnpj.charAt(12)) &&
                    dig2 == Character.getNumericValue(cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarCnpjAlfanumerico(String cnpj) {
        // aceita letras e números, pelo menos 8 caracteres
        return cnpj.matches("[a-zA-Z0-9]{8,}");
    }
}