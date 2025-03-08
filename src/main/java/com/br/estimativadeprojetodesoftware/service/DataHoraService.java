package com.br.estimativadeprojetodesoftware.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author tetzner
 */
public class DataHoraService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMATO_COMPLETO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static LocalDate obterData(LocalDateTime dataHora) {
        return dataHora.toLocalDate();
    }

    public static LocalTime obterHora(LocalDateTime dataHora) {
        return dataHora.toLocalTime();
    }

    public static String formatarData(LocalDate data) {
        return data.format(FORMATO_DATA);
    }

    public static String formatarHora(LocalTime hora) {
        return hora.format(FORMATO_HORA);
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora.format(FORMATO_COMPLETO);
    }

    public static long calcularDiferencaEmDias(LocalDate dataInicial, LocalDate dataFinal) {
        return ChronoUnit.DAYS.between(dataInicial, dataFinal);
    }

    public static long calcularDiferencaEmHoras(LocalTime horaInicial, LocalTime horaFinal) {
        return ChronoUnit.HOURS.between(horaInicial, horaFinal);
    }
}

