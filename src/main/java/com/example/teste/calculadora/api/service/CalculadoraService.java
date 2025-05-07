package com.example.teste.calculadora.api.service;

import com.example.teste.calculadora.api.dto.CalculadoraDto;
import com.example.teste.calculadora.api.exceptions.CalculoException;
import com.example.teste.calculadora.api.exceptions.DataInvalidaException;
import com.example.teste.calculadora.api.util.Utils;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculadoraService {

    private static final int NUMERO_PARCELAS = 120;
    private static final int DIAS_BASE = 360;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final int[] DIAS_PAGAMENTO_POR_MES = new int[13];

    static {
        for (int i = 1; i <= 12; i++) {
            DIAS_PAGAMENTO_POR_MES[i] = 15;
        }
        DIAS_PAGAMENTO_POR_MES[6] = 17;  // Junho
        DIAS_PAGAMENTO_POR_MES[9] = 16;  // Setembro
        DIAS_PAGAMENTO_POR_MES[11] = 18; // Novembro
        DIAS_PAGAMENTO_POR_MES[12] = 16; // Dezembro
    }

    public List<CalculadoraDto> calcularJuros(String dataInicialString, String dataFinalString, String primeiroPagamentoString,
                                             BigDecimal valorEmprestimo, double taxaJuros) {
        try {
            ParametrosCalculo parametros = prepararParametrosCalculo(
                    dataInicialString,
                    dataFinalString,
                    primeiroPagamentoString,
                    valorEmprestimo,
                    taxaJuros
            );

            List<CalculadoraDto> calculos = inicializarListaResultados(parametros);

            adicionarRegistroInicial(calculos, parametros);

            processarPeriodos(calculos, parametros);

            return calculos;
        } catch (DataInvalidaException | CalculoException e) {
            throw e;
        } catch (Exception e) {
            throw new CalculoException("Erro ao calcular juros: " + e.getMessage(), e);
        }
    }

    private ParametrosCalculo prepararParametrosCalculo(String dataInicialString, String dataFinalString,
                                                      String primeiroPagamentoString, BigDecimal valorEmprestimo,
                                                      double taxaJuros) {
        try {
            LocalDate dataInicial = parseData(dataInicialString);
            LocalDate dataFinal = parseData(dataFinalString);
            LocalDate primeiroPagamento = parseData(primeiroPagamentoString);

            Utils.validarDatas(dataInicial, dataFinal, primeiroPagamento);
            Utils.validarValorEmprestimo(valorEmprestimo);
            Utils.validarTaxaJuros(taxaJuros);

            double valorEmprestimoDouble = valorEmprestimo.doubleValue();
            double taxaAnual = taxaJuros / 100.0;
            double amortizacaoMensal = Utils.arredondar(valorEmprestimoDouble / NUMERO_PARCELAS);

            return new ParametrosCalculo(
                dataInicial,
                dataFinal,
                primeiroPagamento,
                valorEmprestimoDouble,
                taxaAnual,
                amortizacaoMensal
            );
        } catch (DataInvalidaException | CalculoException e) {
            throw e;
        } catch (Exception e) {
            throw new CalculoException("Erro ao preparar parâmetros para cálculo: " + e.getMessage(), e);
        }
    }

    private List<CalculadoraDto> inicializarListaResultados(ParametrosCalculo parametros) {
        int mesesEntreDatas = (int) ChronoUnit.MONTHS.between(
            parametros.dataInicial,
            parametros.dataFinal
        ) + 2; // +2 para margem de segurança

        return new ArrayList<>(mesesEntreDatas);
    }

    private void adicionarRegistroInicial(List<CalculadoraDto> calculos, ParametrosCalculo parametros) {
        calculos.add(CalculadoraDto.builder()
                .dataCompetencia(parametros.dataInicial)
                .valorEmprestimo(parametros.valorEmprestimo)
                .saldoDevedor(parametros.valorEmprestimo)
                .consolidada("")
                .parcela(0.0)
                .total(0.0)
                .amortizacao(0.0)
                .saldo(parametros.valorEmprestimo)
                .provisao(0.0)
                .acumulado(0.0)
                .pago(0.0)
                .build());
    }

    private void processarPeriodos(List<CalculadoraDto> calculos, ParametrosCalculo parametros) {
        LocalDate dataAtual = parametros.dataInicial;
        double saldoDevedorBase = parametros.valorEmprestimo;
        double jurosAcumulado = 0.0;
        int numeroParcela = 1;

        while (!dataAtual.isAfter(parametros.dataFinal)) {
            LocalDate proximaData = calcularProximaData(dataAtual, parametros.dataInicial, parametros.primeiroPagamento);

            ResultadoPeriodo resultado = calcularJurosPeriodo(dataAtual, proximaData, saldoDevedorBase, parametros.taxaAnual);

            boolean isPagamento = verificarDataPagamento(proximaData, parametros.primeiroPagamento);

            CalculadoraDto calculoPeriodo;
            if (isPagamento) {
                EstadoCalculo novoEstado = processarPagamento(
                    saldoDevedorBase,
                    jurosAcumulado + resultado.jurosPeriodo,
                    parametros.amortizacaoMensal,
                    numeroParcela
                );

                calculoPeriodo = criarCalculoPagamento(
                    proximaData,
                    novoEstado.saldoDevedor,
                    novoEstado.consolidada,
                    novoEstado.valorParcela,
                    parametros.amortizacaoMensal,
                    resultado.jurosPeriodo,
                    novoEstado.valorPago
                );

                saldoDevedorBase = novoEstado.saldoDevedor;
                jurosAcumulado = novoEstado.jurosAcumulado;
                numeroParcela = novoEstado.numeroParcela;
            } else {
                double saldoDevedorAtual = Utils.arredondar(saldoDevedorBase + resultado.jurosPeriodo);
                jurosAcumulado += resultado.jurosPeriodo;

                calculoPeriodo = criarCalculoNaoPagamento(
                    proximaData,
                    saldoDevedorAtual,
                    saldoDevedorBase,
                    resultado.jurosPeriodo,
                    jurosAcumulado
                );
            }

            calculos.add(calculoPeriodo);
            dataAtual = proximaData;
        }
    }

    private ResultadoPeriodo calcularJurosPeriodo(LocalDate dataAtual, LocalDate proximaData, double saldoDevedorBase, double taxaAnual) {
        long diasPeriodo = ChronoUnit.DAYS.between(dataAtual, proximaData);
        double fatorJuros = Math.pow(1 + taxaAnual, diasPeriodo / (double) DIAS_BASE) - 1;
        double jurosPeriodo = Utils.arredondar(saldoDevedorBase * fatorJuros);

        return new ResultadoPeriodo(jurosPeriodo, fatorJuros);
    }

    private boolean verificarDataPagamento(LocalDate data, LocalDate primeiroPagamento) {
        int diaData = data.getDayOfMonth();
        return diaData >= 15 && diaData <= 18 &&
               (data.isAfter(primeiroPagamento) || data.equals(primeiroPagamento));
    }

    private EstadoCalculo processarPagamento(double saldoDevedorBase, double jurosAcumulado,
                                           double amortizacaoMensal, int numeroParcela) {
        String consolidada = String.format("%d/%d", numeroParcela, NUMERO_PARCELAS);
        double novoSaldoDevedor = Utils.arredondar(saldoDevedorBase - amortizacaoMensal);
        double valorParcela = Utils.arredondar(amortizacaoMensal + jurosAcumulado);

        return new EstadoCalculo(
            novoSaldoDevedor,
            0.0,
            numeroParcela + 1,
            consolidada,
            valorParcela,
            jurosAcumulado
        );
    }

    private CalculadoraDto criarCalculoPagamento(LocalDate data, double saldoDevedor, String consolidada,
                                               double valorParcela, double amortizacao,
                                               double jurosPeriodo, double jurosAcumulado) {
        return CalculadoraDto.builder()
                .dataCompetencia(data)
                .valorEmprestimo(0.0)
                .saldoDevedor(saldoDevedor)
                .consolidada(consolidada)
                .parcela(valorParcela)
                .total(0.0)
                .amortizacao(amortizacao)
                .saldo(saldoDevedor)
                .provisao(jurosPeriodo)
                .acumulado(0.0)
                .pago(valorParcela - amortizacao) // O valor pago é a parcela menos a amortização (ou seja, os juros)
                .build();
    }

    private CalculadoraDto criarCalculoNaoPagamento(LocalDate data, double saldoDevedorAtual,
                                                  double saldoDevedorBase, double jurosPeriodo,
                                                  double jurosAcumulado) {
        return CalculadoraDto.builder()
                .dataCompetencia(data)
                .valorEmprestimo(0.0)
                .saldoDevedor(saldoDevedorAtual)
                .consolidada("")
                .parcela(0.0)
                .total(0.0)
                .amortizacao(0.0)
                .saldo(saldoDevedorBase)
                .provisao(jurosPeriodo)
                .acumulado(jurosAcumulado)
                .pago(0.0)
                .build();
    }

    private static class ParametrosCalculo {
        final LocalDate dataInicial;
        final LocalDate dataFinal;
        final LocalDate primeiroPagamento;
        final double valorEmprestimo;
        final double taxaAnual;
        final double amortizacaoMensal;

        ParametrosCalculo(LocalDate dataInicial, LocalDate dataFinal, LocalDate primeiroPagamento,
                         double valorEmprestimo, double taxaAnual, double amortizacaoMensal) {
            this.dataInicial = dataInicial;
            this.dataFinal = dataFinal;
            this.primeiroPagamento = primeiroPagamento;
            this.valorEmprestimo = valorEmprestimo;
            this.taxaAnual = taxaAnual;
            this.amortizacaoMensal = amortizacaoMensal;
        }
    }

    private static class ResultadoPeriodo {
        final double jurosPeriodo;
        final double fatorJuros;

        ResultadoPeriodo(double jurosPeriodo, double fatorJuros) {
            this.jurosPeriodo = jurosPeriodo;
            this.fatorJuros = fatorJuros;
        }
    }

    private static class EstadoCalculo {
        final double saldoDevedor;
        final double jurosAcumulado;
        final int numeroParcela;
        final String consolidada;
        final double valorParcela;
        final double valorPago; // Valor a ser pago (juros acumulados antes do pagamento)

        EstadoCalculo(double saldoDevedor, double jurosAcumulado, int numeroParcela,
                     String consolidada, double valorParcela) {
            this(saldoDevedor, jurosAcumulado, numeroParcela, consolidada, valorParcela, 0.0);
        }

        EstadoCalculo(double saldoDevedor, double jurosAcumulado, int numeroParcela,
                     String consolidada, double valorParcela, double valorPago) {
            this.saldoDevedor = saldoDevedor;
            this.jurosAcumulado = jurosAcumulado;
            this.numeroParcela = numeroParcela;
            this.consolidada = consolidada;
            this.valorParcela = valorParcela;
            this.valorPago = valorPago;
        }
    }

    private LocalDate parseData(String dataString) {
        if (StringUtils.isBlank(dataString)) {
            throw new DataInvalidaException("A data não pode ser vazia");
        }

        try {
            return LocalDate.parse(dataString, DATE_FORMATTER);
        } catch (Exception e) {
            throw new DataInvalidaException("Formato de data inválido. Use o formato dd/MM/yyyy: " + dataString, e);
        }
    }

    private LocalDate calcularProximaData(LocalDate dataAtual, LocalDate dataInicial, LocalDate primeiroPagamento) {
        if (dataAtual.equals(dataInicial)) {
            return dataAtual.withDayOfMonth(dataAtual.lengthOfMonth());
        }

        if (dataAtual.getDayOfMonth() == dataAtual.lengthOfMonth()) {
            LocalDate proximoPagamento = getProximaDataPagamento(dataAtual.plusMonths(1));
            if (proximoPagamento.isAfter(primeiroPagamento) || proximoPagamento.equals(primeiroPagamento)) {
                return proximoPagamento;
            } else {
                LocalDate proximoMes = dataAtual.plusMonths(1);
                return proximoMes.withDayOfMonth(proximoMes.lengthOfMonth());
            }
        }

        return dataAtual.withDayOfMonth(dataAtual.lengthOfMonth());
    }

    private LocalDate getProximaDataPagamento(LocalDate data) {
        int mes = data.getMonthValue();
        int dia = DIAS_PAGAMENTO_POR_MES[mes];
        return data.withDayOfMonth(dia);
    }
}