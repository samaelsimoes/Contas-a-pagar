    package com.br.contas.apagar.util;

    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;

    public class DateUtils {

        public static String formatDate(LocalDate date) {
            if (date == null) {
                return "";
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(formatter);
        }

        public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
            return !date.isBefore(startDate) && !date.isAfter(endDate);
        }
    }