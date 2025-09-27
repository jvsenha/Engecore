package br.com.engecore.Enum;

public enum Role {
    ROLE_ADMIN(null),
    ROLE_CLIENTE(null),

    ROLE_FUNC_ADM("FUNC"),
    ROLE_FUNC_FINANCEIRO("FUNC"),
    ROLE_FUNC_GERENTE("FUNC"),
    ROLE_FUNC_GESTOR("FUNC"),
    ROLE_FUNC_RH("FUNC"),
    ROLE_FUNC_CONST("FUNC"),
    ROLE_FORNECEDOR(null);

    private final String categoria;

    Role(String categoria) {
        this.categoria = categoria;
    }

    public boolean isFuncionario() {
        return "FUNC".equals(categoria);
    }
}
