package com.interviews.CardProcessorService.utils;

import com.interviews.CardProcessorService.domain.Card;
import com.interviews.CardProcessorService.domain.CardDTO;
import com.interviews.CardProcessorService.domain.CardEntity;

/*
 * Used to assist with Domain Object Layer conversion
 *
 * */
public class DomainUtils {

    public static CardDTO cardToCardDto(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .bank(card.getBank())
                .cardNumber(card.getCardNumber())
                .expiryDate(card.getExpiryDate())
                .build();
    }

    public static Card cardDtoToCard(CardDTO cardDTO) {
        return Card.builder()
                .id(cardDTO.getId())
                .bank(cardDTO.getBank())
                .cardNumber(cardDTO.getCardNumber())
                .expiryDate(cardDTO.getExpiryDate())
                .build();
    }

    public static CardEntity cardToCardEntity(Card card) {
        return CardEntity.builder()
                .id(card.getId())
                .bank(card.getBank())
                .cardNumber(card.getCardNumber())
                .expiryDate(card.getExpiryDate())
                .build();
    }

    public static Card cardEntityToCard(CardEntity cardEntity) {
        return Card.builder()
                .id(cardEntity.getId())
                .bank(cardEntity.getBank())
                .cardNumber(cardEntity.getCardNumber())
                .expiryDate(cardEntity.getExpiryDate())
                .build();
    }
}
