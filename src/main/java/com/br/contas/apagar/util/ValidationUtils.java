package com.br.contas.apagar.util;

import java.math.BigDecimal;

public class ValidationUtils {

    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("\\d{6,}");
    }

    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}