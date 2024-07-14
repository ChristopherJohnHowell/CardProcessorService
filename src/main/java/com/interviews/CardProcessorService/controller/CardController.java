package com.interviews.CardProcessorService.controller;

import com.interviews.CardProcessorService.domain.Card;
import com.interviews.CardProcessorService.domain.CardDTO;
import com.interviews.CardProcessorService.utils.DomainUtils;
import com.interviews.CardProcessorService.services.ICardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/card")
public class CardController {

    private final ICardService cardService;

    public CardController(ICardService cardService) {
        this.cardService = cardService;
    }

    /**
     * This can be used for testing.
     *
     * @return testMessage
     */
    @GetMapping("/test")
    public ResponseEntity<String> index() {
        return new ResponseEntity<>("API is working!", HttpStatus.OK);
    }

    /**
     * Returns all the Credit Cards that have been stored.
     *
     * @return creditCards
     */
    @GetMapping("/")
    public ResponseEntity<List<CardDTO>> getAllCards() {
        List<Card> allCards = cardService.findAllCards();
        if (allCards.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(allCards.stream().map(card -> DomainUtils.cardToCardDto(card)).toList(), HttpStatus.OK);
        }
    }

    /**
     * Stores the provided card.
     *
     * @param cardDTO
     * @param bindingResult
     * @return resultantResponse
     */
    @PostMapping("/")
    public ResponseEntity<?> addCard(@RequestBody @Valid CardDTO cardDTO, BindingResult bindingResult) {
        // Validation
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        // Save Card
        Card card = DomainUtils.cardDtoToCard(cardDTO);
        final Card resultCard = cardService.saveCard(card);

        if (resultCard == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            CardDTO resultCardDTO = DomainUtils.cardToCardDto(resultCard);
            return new ResponseEntity<>(resultCardDTO, HttpStatus.CREATED);
        }
    }

    /**
     * Uploads all valid Cards from the provided file. Will not save cards that are invalid.
     *
     * @param file .CSV containing valid card details (Bank,Card Number,Expiry Date)
     * @return responseEntity
     */
    @PostMapping("/uploadCsv")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        return cardService.saveCardsFromCsv2(file);
    }

}
