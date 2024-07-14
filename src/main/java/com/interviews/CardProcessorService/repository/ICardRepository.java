package com.interviews.CardProcessorService.repository;

import com.interviews.CardProcessorService.domain.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICardRepository extends JpaRepository<CardEntity, Integer> {
}
