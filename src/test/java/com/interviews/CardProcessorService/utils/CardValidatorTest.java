package com.interviews.CardProcessorService.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CardValidatorTest {

    @Test
    public void validateCreditCard_WithValidData_ShouldReturnTrue() {
        String cardNumber = "5355221435982253";
        boolean result = CardValidator.validateCreditCardNumber(cardNumber);
        assertTrue(result);
    }

    @Test
    public void validateCreditCard_WithInvalidData_ShouldReturnFalse() {
        String cardNumber = "5355221435982254";
        boolean result = CardValidator.validateCreditCardNumber(cardNumber);
        assertFalse(result);
    }

    @Test
    public void validateCreditCard_WithNullData_ShouldReturnFalse() {
        String cardNumber = null;
        boolean result = CardValidator.validateCreditCardNumber(cardNumber);
        assertFalse(result);
    }

    @Test
    public void validateCreditCard_WithEmptyData_ShouldReturnFalse() {
        String cardNumber = "";
        boolean result = CardValidator.validateCreditCardNumber(cardNumber);
        assertFalse(result);
    }

    @Test
    void validateExpiryDate_WithValidExpiryDate_ShouldReturnTrue() {
        assertTrue(CardValidator.validateExpiryDate("Nov-2022"));
        assertTrue(CardValidator.validateExpiryDate("Sep-2022"));
        assertTrue(CardValidator.validateExpiryDate("Aug-2022"));
    }

    @Test
    void validateExpiryDate_WithInvalidExpiryDate_ShouldReturnFalse() {
        assertFalse(CardValidator.validateExpiryDate(null));
        assertFalse(CardValidator.validateExpiryDate(""));
        assertFalse(CardValidator.validateExpiryDate("Feb-202"));
        assertFalse(CardValidator.validateExpiryDate("Oct-20223"));
        assertFalse(CardValidator.validateExpiryDate("May-1949"));
        assertFalse(CardValidator.validateExpiryDate("Jun-3001"));
    }


}
