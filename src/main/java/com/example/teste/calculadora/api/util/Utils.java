package com.example.teste.calculadora.api.util;

import com.example.teste.calculadora.api.exceptions.CalculoException;
import com.example.teste.calculadora.api.exceptions.DataInvalidaException;
import com.example.teste.calculadora.api.exceptions.ValorInvalidoException;
import com.example.teste.calculadora.api.exceptions.TaxaJurosInvalidaException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Utils {

    public static void validarDatas(LocalDate dataInicial, LocalDate dataFinal, LocalDate primeiroPagamento) {
        if (dataInicial == null) {
            throw new DataInvalidaException("A data inicial não pode ser nula");
        }
        if (dataFinal == null) {
            throw new DataInvalidaException("A data final não pode ser nula");
        }
        if (primeiroPagamento == null) {
            throw new DataInvalidaException("A data do primeiro pagamento não pode ser nula");
        }
        if (dataFinal.isBefore(dataInicial)) {
            throw new DataInvalidaException("Data final deve ser maior que a data inicial");
        }
        if (primeiroPagamento.isBefore(dataInicial) ||
                primeiroPagamento.isAfter(dataFinal)) {
            throw new DataInvalidaException("Data do primeiro pagamento deve estar entre a data inicial e final");
        }
    }

    public static void validarValorEmprestimo(BigDecimal valorEmprestimo) {
        if (valorEmprestimo == null) {
            throw new ValorInvalidoException("O valor do empréstimo não pode ser nulo");
        }
        if (valorEmprestimo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor do empréstimo deve ser maior que zero");
        }
    }

    public static void validarTaxaJuros(double taxaJuros) {
        if (taxaJuros <= 0) {
            throw new TaxaJurosInvalidaException("A taxa de juros deve ser maior que zero");
        }
        if (taxaJuros > 100) {
            throw new TaxaJurosInvalidaException("A taxa de juros não pode ser maior que 100%");
        }
    }

    public static double arredondar(double value) {
        try {
            return BigDecimal.valueOf(value)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        } catch (Exception e) {
            throw new CalculoException("Erro ao arredondar o valor: " + value, e);
        }
    }
}
