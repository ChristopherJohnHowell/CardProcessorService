package com.interviews.CardProcessorService.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class CardUtils {

    private static final String customDatePattern = "MMM-yyyy";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(customDatePattern);

    /**
     * Converts an Expiry Date into Date object.
     *
     * @param expiryDate
     * @return
     * @throws ParseException
     */
    public static Date parseExpiryDate(String expiryDate) throws ParseException {
        DATE_FORMAT.setLenient(false);
        return DATE_FORMAT.parse(expiryDate);
    }

    /**
     * Obfuscate credit card and returns result.
     *
     * @param cardNumber
     * @return obfuscated Credit Card
     */
    public static String obfuscateCardNumber(String cardNumber) {
        return "xxxx-xxxx-xxxx-" + cardNumber.substring(cardNumber.length() - 4);
    }

}
