package com.interviews.CardProcessorService.utils;

import com.interviews.CardProcessorService.domain.Card;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class CardValidator {

    private static final String customDatePattern = "MMM-yyyy";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(customDatePattern);
    private static final List<String> VALID_MONTHS = Arrays.asList(
            "JAN", "FEB", "MER", "APR", "MAY", "JUN",
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    );

    public static boolean validateCreditCardNumber(String cardNumber) {
        // Luhn's Algorithm.
        if (cardNumber == null || cardNumber.isEmpty())
            return false;
        cardNumber = cardNumber.replace("-", "");
        int sum = 0;
        boolean isSecond = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';
            if (isSecond)
                digit = digit * 2;
            sum += digit / 10;
            sum += digit % 10;
            isSecond = !isSecond;
        }

        return (sum % 10 == 0);
    }

    /**
     * Validate the provided Expiry Date.
     *
     * @param expiryDate expiryDate
     * @return isValid
     */
    public static boolean validateExpiryDate(String expiryDate) {
        if (expiryDate == null || expiryDate.length() != customDatePattern.length()) {
            return false;
        }

        String month = expiryDate.substring(0, 3).toUpperCase();
        String yearString = expiryDate.substring(4, 8);

        if (!VALID_MONTHS.contains(month)) {
            return false;
        }

        try {
            int year = Integer.parseInt(yearString);
            if (year < 1950 || year > 3000) {
                return false;
            }

            // Check if the day is valid for the given month. (DEC-1999)
            String testDate = month + "-" + year;
            DATE_FORMAT.setLenient(false);
            Date parsedDate = DATE_FORMAT.parse(testDate);

            return parsedDate != null;
        } catch (NumberFormatException | ParseException e) {
            return false;
        }
    }

    /**
     * Validates a Card based on all fields that need to be validated.
     *
     * @param card
     * @return
     */
    public static boolean isValid(Card card) {
        if(card == null){
            return false;
        }

        // Validate Expiry Date.
        if (!validateExpiryDate(card.getExpiryDate())) {
            log.error("validateExpiryDate validation failed!");
            return false;
        }

        // Validate Credit Card Number.
        if (!validateCreditCardNumber(card.getCardNumber())) {
            log.error("validateCreditCardNumber validation failed!");
            return false;
        }

        return true;
    }
}
