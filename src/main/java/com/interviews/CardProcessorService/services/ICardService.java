package com.interviews.CardProcessorService.services;

import com.interviews.CardProcessorService.domain.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICardService {

    Card saveCard(Card card);

    //List<Card> saveCardsFromCsv(MultipartFile file);

    ResponseEntity<String> saveCardsFromCsv2(MultipartFile file);

    List<Card> findAllCards();

}
