package com.br.contas.apagar.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class DateUtilsTest {

    @Test
    public void testFormatDate_ValidDate() {
        LocalDate date = LocalDate.of(2024, 11, 17);
        String formattedDate = DateUtils.formatDate(date);
        Assertions.assertEquals("17/11/2024", formattedDate);
    }

    @Test
    public void testFormatDate_NullDate() {
        String formattedDate = DateUtils.formatDate(null);
        Assertions.assertEquals("", formattedDate);
    }

    @Test
    public void testIsDateInRange_ValidRange() {
        LocalDate date = LocalDate.of(2024, 11, 17);
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 30);

        boolean result = DateUtils.isDateInRange(date, startDate, endDate);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsDateInRange_DateBeforeStart() {
        LocalDate date = LocalDate.of(2024, 10, 31);
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 30);

        boolean result = DateUtils.isDateInRange(date, startDate, endDate);
        Assertions.assertFalse(result);
    }

    @Test
    public void testIsDateInRange_DateAfterEnd() {
        LocalDate date = LocalDate.of(2024, 12, 1);
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 30);

        boolean result = DateUtils.isDateInRange(date, startDate, endDate);
        Assertions.assertFalse(result);
    }
}