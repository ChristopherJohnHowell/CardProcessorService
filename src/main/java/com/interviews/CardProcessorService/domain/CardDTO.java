package com.interviews.CardProcessorService.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO implements Comparator<CardEntity> {

    @Id
    private int id;

    @NotEmpty(message = "Bank name is required")
    private String bank;

    @CreditCardNumber(ignoreNonDigitCharacters = true, message = "Valid Credit Card Number is required")
    @NotEmpty(message = "Card Number is required")
    private String cardNumber;

    @NotNull(message = "Expiry Date is required")
    private String expiryDate;

    @Override
    public int compare(CardEntity card1, CardEntity card2) {
        return card1.getExpiryDate().compareTo(card2.getExpiryDate());
    }

}
