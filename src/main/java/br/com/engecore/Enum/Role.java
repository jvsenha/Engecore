package br.com.engecore.Enum;

public enum Role {
    //ROLE ADM E CLIENTE
    ROLE_ADMIN(null),
    ROLE_CLIENTE(null),

    //ROLE DE FUNCIONARIOS
    ROLE_FUNC("FUNC"),
    ROLE_FUNC_GERENTE("FUNC"),
    ROLE_FUNC_FINANCEIRO("FUNC"),
    ROLE_FUNC_GESTOR("FUNC"),
    ROLE_FUNC_RH("FUNC"),
    ROLE_FUNC_ADM("FUNC");

    // STRING para guarda a CATEGORIA
    private final String categoria;

    //Construtor para a enum
    Role(String categoria) {
        this.categoria = categoria;
    }

    //verificar se a ROLE é FUNC
    public boolean isFuncionario() {
        return "FUNC".equals(categoria);
    }

}
