package com.interviews.CardProcessorService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviews.CardProcessorService.domain.CardDTO;
import com.interviews.CardProcessorService.repository.ICardRepository;
import com.interviews.CardProcessorService.services.ICardService;
import com.interviews.CardProcessorService.utils.CardTestData;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.interviews.CardProcessorService.utils.CardTestData.validObfuscatedCardNumber;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class CardControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICardService cardService;

    @Autowired
    private ICardRepository cardRepository;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @Test
    public void addCard_WithGoodData_ReturnsHttpStatus201() throws Exception {
        CardDTO cardDTO = CardTestData.testCardDtoGood1();
        String jsonBookDto = objectMapper.writeValueAsString(cardDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/card/").contentType(MediaType.APPLICATION_JSON).content(jsonBookDto))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bank").value(cardDTO.getBank()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber").value(validObfuscatedCardNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiryDate").value(cardDTO.getExpiryDate()));
    }

    @Test
    public void addCard_WithBadData_ReturnsHttpStatus400() throws Exception {
        CardDTO badBookDTO = CardTestData.testCardDTOBadMissingData1();
        String jsonBookDto = objectMapper.writeValueAsString(badBookDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/card/").contentType(MediaType.APPLICATION_JSON).content(jsonBookDto))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
