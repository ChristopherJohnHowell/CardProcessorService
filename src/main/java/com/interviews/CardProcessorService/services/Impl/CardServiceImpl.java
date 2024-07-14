package com.interviews.CardProcessorService.services.Impl;

import com.interviews.CardProcessorService.domain.Card;
import com.interviews.CardProcessorService.domain.CardDTO;
import com.interviews.CardProcessorService.domain.CardEntity;
import com.interviews.CardProcessorService.repository.ICardRepository;
import com.interviews.CardProcessorService.services.ICardService;
import com.interviews.CardProcessorService.utils.CardUtils;
import com.interviews.CardProcessorService.utils.CardValidator;
import com.interviews.CardProcessorService.utils.DomainUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CardServiceImpl implements ICardService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM-yyyy");
    private final ICardRepository cardRepository;

    public CardServiceImpl(ICardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Save a Card. Invalid Cards will not be saved and return null.
     *
     * @param card credit card
     * @return savedCard
     */
    @Override
    public Card saveCard(Card card) {
        try {
            // Validate
            if (CardValidator.isValid(card)) {
                // Obfuscate Data as required.
                card.setCardNumber(CardUtils.obfuscateCardNumber(card.getCardNumber()));
                // Save Card Data in DB.
                CardEntity cardEntity = DomainUtils.cardToCardEntity(card);
                CardEntity resultCardEntity = cardRepository.save(cardEntity);
                if (resultCardEntity == null) {
                    log.error("Save Error! Cannot save to DB!");
                    return null;
                }
                return DomainUtils.cardEntityToCard(resultCardEntity);
            } else {
                log.error("Invalid Card data! Cannot save to DB!");
                return null;
            }
        } catch (Exception e) {
            log.error("Service saveCard Error: ", e);
            return null;
        }
    }

    /**
     * Read Cards from CSV and save valid cards.
     *
     * @param file
     * @return
     */
    @Override
    public ResponseEntity<String> saveCardsFromCsv2(MultipartFile file) {
        // Initial Validation
        if (file.isEmpty()) {
            return new ResponseEntity<>("File provided is empty!", HttpStatus.BAD_REQUEST);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Card> cards = new ArrayList<>(); //
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    CardDTO cardDto = new CardDTO();
                    cardDto.setBank(data[0].trim());
                    cardDto.setCardNumber(data[1].trim());
                    cardDto.setExpiryDate(data[2].trim());
                    if (cardDto.getBank().isEmpty() || cardDto.getCardNumber().isEmpty() || cardDto.getExpiryDate().isEmpty()) {
                        log.error("Data may not be formatted/entered correctly for line: " + line);
                        return new ResponseEntity<>("Data may not be formatted/entered correctly for line: " + line, HttpStatus.BAD_REQUEST);
                    }
                    // Save Card.
                    Card resultCard = saveCard(DomainUtils.cardDtoToCard(cardDto));
                    if (resultCard == null) {
                        return new ResponseEntity<>("Data provided may be INVALID for line: " + line, HttpStatus.BAD_REQUEST);
                    }
                    cards.add(resultCard);
                } else {
                    log.error("Data may not be segmented/formatted correctly for line: " + line);
                    return new ResponseEntity<>("Data may not be segmented/formatted correctly for line: " + line, HttpStatus.BAD_REQUEST);
                }
            }

            return new ResponseEntity<>("Successful! ", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Could not get information properly from CSV file provided!");
        }

        return new ResponseEntity<>("Could not get information properly from CSV file provided!", HttpStatus.BAD_REQUEST);
    }

    /**
     * Find and return all the cards.
     *
     * @return list of cards
     */
    @Override
    public List<Card> findAllCards() {
        // Find cards
        List<CardEntity> allCardEntities = cardRepository.findAll();
        // Sort the values
        allCardEntities.sort((card1, card2) -> {
            try {
                Date date1 = CardUtils.parseExpiryDate(card1.getExpiryDate());
                Date date2 = CardUtils.parseExpiryDate(card2.getExpiryDate());
                return date2.compareTo(date1); // Expiry Date in Descending order.
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        return allCardEntities.stream().map(DomainUtils::cardEntityToCard).toList();
    }

}
