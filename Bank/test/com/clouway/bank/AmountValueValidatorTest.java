package com.clouway.bank;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class AmountValueValidatorTest {
    private AmountValueValidator amountValueValidator;
    private Double amountLimit = 99999999.0;


    @Before
    public void setUp() {
        amountValueValidator = new AmountValueValidator(amountLimit);
    }

    @Test
    public void validateAmountHappyPath() throws Exception {
        assertTrue(amountValueValidator.validate(new BigDecimal("20.00")));
        assertTrue(amountValueValidator.validate(new BigDecimal("20")));
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void validateNegativeAmount() {
        amountValueValidator.validate(new BigDecimal("-20"));
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void validateZeroAmount() throws Exception {
        amountValueValidator.validate(new BigDecimal("0"));
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void validateTooBigAmount() {
        amountValueValidator.validate(new BigDecimal(amountLimit + 100));
    }

    @Test
    public void checkAmountDelimiterHappyPath() {
        assertEquals("20.00", amountValueValidator.checkAmountDelimiter("20.00"));
    }

    @Test
    public void checkAmountWithOneDecimalPlaceAfterDot() {
        assertEquals("20.00", amountValueValidator.checkAmountDelimiter("20.0"));
    }

    @Test
    public void checkAmountWithCommaDelimiter() {
        assertEquals("20.00", amountValueValidator.checkAmountDelimiter("20,00"));
    }

    @Test
    public void checkAmountWithOneDecimalPlaceAfterComma() {
        assertEquals("20.00", amountValueValidator.checkAmountDelimiter("20,0"));
    }
}
