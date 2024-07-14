package com.interviews.CardProcessorService.services;

import com.interviews.CardProcessorService.domain.Card;
import com.interviews.CardProcessorService.domain.CardEntity;
import com.interviews.CardProcessorService.repository.ICardRepository;
import com.interviews.CardProcessorService.services.Impl.CardServiceImpl;
import com.interviews.CardProcessorService.utils.DomainUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    final static String validCardNumber = "5355-2214-3598-2253";
    final static String validObfuscatedCardNumber = "xxxx-xxxx-xxxx-2253";
    final static String invalidCardNumber = "1234-5678-9012-3419";
    final static String validExpiryDate = "Nov-2017";

    private CardServiceImpl cardService;
    @Mock
    private ICardRepository cardRepository;

    @Mock
    private MultipartFile file;

    @Mock
    private BufferedReader reader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardService = new CardServiceImpl(cardRepository);
    }

    @Test
    public void saveCard_withValidCard_returnsSavedCard() {
        Card validCard = new Card();
        validCard.setCardNumber(validCardNumber);
        validCard.setExpiryDate(validExpiryDate);
        CardEntity savedCardEntity = new CardEntity();
        when(cardRepository.save(any(CardEntity.class))).thenReturn(savedCardEntity);
        Card savedCard = cardService.saveCard(validCard);
        assertEquals(DomainUtils.cardEntityToCard(savedCardEntity), savedCard);
        verify(cardRepository, times(1)).save(any(CardEntity.class));
    }

    @Test
    public void saveCard_withInvalidCard_returnsNull() {
        Card card = new Card();
        card.setCardNumber(invalidCardNumber); // Invalid card number
        card.setExpiryDate(validExpiryDate);
        Card savedCard = cardService.saveCard(card);
        assertNull(savedCard);
        verify(cardRepository, times(0)).save(any(CardEntity.class));
    }

    @Test
    public void saveCard_withValidCard_obfuscatesCardNumber() {
        Card validCard = new Card();
        validCard.setCardNumber(validCardNumber);
        validCard.setExpiryDate(validExpiryDate);
        CardEntity savedCardEntity = CardEntity.builder().bank("Test Bank").cardNumber(validObfuscatedCardNumber).expiryDate(validExpiryDate).build();
        when(cardRepository.save(any(CardEntity.class))).thenReturn(savedCardEntity);
        Card savedCard = cardService.saveCard(validCard);
        assertEquals(validObfuscatedCardNumber, savedCard.getCardNumber());
        verify(cardRepository, times(1)).save(any(CardEntity.class));
    }

    @Test
    public void testSaveCardsFromCsv2_ValidData_Success() {
        String csvData = "BankName,5355-2214-3598-2253,Nov-2023";
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvData.getBytes());
        CardEntity bookEntityReturn = CardEntity.builder().id(1).bank("Test Bank").cardNumber("xxxx-xxxx-xxxx-3456").expiryDate(validExpiryDate).build();
        when(cardRepository.save(any())).thenReturn(bookEntityReturn);
        ResponseEntity<String> response = cardService.saveCardsFromCsv2(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveCardsFromCsv2_InvalidData_Failure() {
        String invalidCsvData = "BankName,5355-2214-3598-2253";
        MockMultipartFile invalidFile = new MockMultipartFile("file", "invalid.csv", "text/csv", invalidCsvData.getBytes());
        ResponseEntity<String> response = cardService.saveCardsFromCsv2(invalidFile);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testFindAllCards_WithValidData_ShouldReturn2Cards() {
        List<CardEntity> cardEntities = new ArrayList<>();
        CardEntity testCardEntity = CardEntity.builder().bank("Test Bank").cardNumber("xxxx-xxxx-xxxx-3456").expiryDate(validExpiryDate).build();
        cardEntities.add(testCardEntity);
        cardEntities.add(testCardEntity);
        when(cardRepository.findAll()).thenReturn(cardEntities);
        List<Card> cards = cardService.findAllCards();
        assertEquals(2, cards.size());
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllCards_WithEmptyListData_ShouldReturnEmptyList() {
        when(cardRepository.findAll()).thenReturn(Collections.emptyList());
        List<Card> result = cardService.findAllCards();
        assertEquals(0, result.size());
    }

    @Test
    public void testFindAllCards_WithValidData_ShouldSortCorrectly() {
        List<CardEntity> cardEntities = new ArrayList<>();
        CardEntity testCardEntity1 = CardEntity.builder().bank("Test Bank").cardNumber("xxxx-xxxx-xxxx-3456").expiryDate(validExpiryDate).build();
        CardEntity testCardEntity2 = CardEntity.builder().bank("Test Bank").cardNumber("xxxx-xxxx-xxxx-1234").expiryDate("Dec-2099").build();
        cardEntities.add(testCardEntity1);
        cardEntities.add(testCardEntity2);
        when(cardRepository.findAll()).thenReturn(cardEntities);
        List<Card> cards = cardService.findAllCards();
        assertEquals(2, cards.size());
        assertEquals("xxxx-xxxx-xxxx-1234", cards.get(0).getCardNumber());
        assertEquals("xxxx-xxxx-xxxx-3456", cards.get(1).getCardNumber());
    }

    @Test
    public void testFindAllCards_WithInvalidExpiryDate_ShouldThrowsRuntimeException() {
        List<CardEntity> cardEntities = new ArrayList<>();
        CardEntity testCardEntity1 = CardEntity.builder().bank("Test Bank").cardNumber("xxxx-xxxx-xxxx-3456").expiryDate("Invalid Date").build();
        CardEntity testCardEntity2 = CardEntity.builder().bank("Test Bank").cardNumber("xxxx-xxxx-xxxx-1234").expiryDate(validExpiryDate).build();
        cardEntities.add(testCardEntity1);
        cardEntities.add(testCardEntity2);
        when(cardRepository.findAll()).thenReturn(cardEntities);
        assertThrows(RuntimeException.class, () -> cardService.findAllCards());
    }

}
