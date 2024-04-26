package com.olikproject.olik.exception;

public class BookAlreadyRentedException extends RuntimeException {

  public BookAlreadyRentedException (String message) {
    super(message);
  }
}
