package com.interviews.CardProcessorService.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card implements Comparator<CardEntity> {

    @Id
    private int id;
    private String bank;
    private String cardNumber;
    private String expiryDate;

    @Override
    public int compare(CardEntity card1, CardEntity card2) {
        return card1.getExpiryDate().compareTo(card2.getExpiryDate());
    }
}
