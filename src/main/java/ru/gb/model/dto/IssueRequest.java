package ru.gb.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Запрос на выдачу
 */
public record IssueRequest(long readerId, long bookId) {

//  /**
//   * Идентификатор читателя
//   */
//  private long readerId;
//
//  /**
//   * Идентификатор книги
//   */
//  private long bookId;

}