package br.com.engecore.Enum;

public enum FaixaRenda {
    FAIXA_1(0, 2000),      // Até R$ 2.000
    FAIXA_2(2001, 4000),   // De R$ 2.001 a R$ 4.000
    FAIXA_3(4001, 7000),   // De R$ 4.001 a R$ 7.000
    FAIXA_4(7001, 10000),  // De R$ 7.001 a R$ 10.000
    ACIMA_10000(10001, null); // Acima de R$ 10.000

    private final Integer salarioMin;
    private final Integer salarioMax;

    FaixaRenda(Integer salarioMin, Integer salarioMax) {
        this.salarioMin = salarioMin;
        this.salarioMax = salarioMax;
    }

    public Integer getSalarioMin() {
        return salarioMin;
    }

    public Integer getSalarioMax() {
        return salarioMax;
    }
}
