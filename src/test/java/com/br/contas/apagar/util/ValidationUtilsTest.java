package com.br.contas.apagar.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ValidationUtilsTest {

    @Test
    public void testIsValidAccountNumber_ValidAccount() {
        boolean isValid = ValidationUtils.isValidAccountNumber("123456");

        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsValidAccountNumber_ShortAccount() {
        boolean isValid = ValidationUtils.isValidAccountNumber("12345");

        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsValidAccountNumber_InvalidAccount() {
        boolean isValid = ValidationUtils.isValidAccountNumber("123ABC");
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsValidAmount_ValidAmount() {
        boolean isValid = ValidationUtils.isValidAmount(BigDecimal.valueOf(100));

        Assertions.assertTrue(isValid);
    }

    @Test
    public void testIsValidAmount_ZeroAmount() {
        boolean isValid = ValidationUtils.isValidAmount(BigDecimal.ZERO);

        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsValidAmount_NegativeAmount() {
        boolean isValid = ValidationUtils.isValidAmount(BigDecimal.valueOf(-100));

        Assertions.assertFalse(isValid);
    }

    @Test
    public void testIsValidAmount_NullAmount() {
        boolean isValid = ValidationUtils.isValidAmount(null);

        Assertions.assertFalse(isValid);
    }
}