package com.interviews.CardProcessorService.utils;

import com.interviews.CardProcessorService.domain.CardDTO;

public class CardTestData {

    public final static String validCardNumber = "5355-2214-3598-2253";
    public final static String validObfuscatedCardNumber = "xxxx-xxxx-xxxx-2253";
    public final static String invalidCardNumber = "1234-5678-9012-3419";
    public final static String validExpiryDate = "Nov-2017";

    public static CardDTO testCardDtoGood1() {
        CardDTO bookDTO = new CardDTO();
        bookDTO.setBank("Test Bank");
        bookDTO.setCardNumber(validCardNumber);
        bookDTO.setExpiryDate(validExpiryDate);
        return bookDTO;
    }

    public static CardDTO testCardDTOBadMissingData1() {
        CardDTO bookDTO = new CardDTO();
        bookDTO.setBank("Test Bank");
        //bookDTO.setCardNumber("1234-5678-9012-3419");
        bookDTO.setExpiryDate(validExpiryDate);
        return bookDTO;
    }


}
