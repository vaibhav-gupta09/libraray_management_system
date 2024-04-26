package com.olikproject.olik.controller;

import java.util.List;
import java.util.NoSuchElementException;

// import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.olikproject.olik.exception.InvalidIsbnException;
import com.olikproject.olik.model.Author;
import com.olikproject.olik.model.Book;
import com.olikproject.olik.service.BookService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/book")
@Validated
public class BookController {
  @Autowired
  private BookService bookService;

  @GetMapping("/all")
  public ResponseEntity<?> getAllBooks() {
    try {
      List<Book> books = bookService.getAllBooks();
      return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getBookById(@PathVariable String id) {
    try {
      Book book = bookService.getBookById(id);
      return new ResponseEntity<Book>(book, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<?> createBook(@RequestBody @Valid Book book) {
    try {
      Book createdBook = bookService.createBook(book);
      return new ResponseEntity<Book>(createdBook, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (InvalidIsbnException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateBookById(@PathVariable String id, @RequestBody Book updatedBook) {
    try {
      Book saveBook = bookService.updateBookById(id, updatedBook);
      return new ResponseEntity<Book>(saveBook, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (InvalidIsbnException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteBookById(@PathVariable String id) {
    try {
      bookService.deleteBookById(id);
      return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
    }catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/author/{id}")
  public ResponseEntity<?> getAuthorByBookId(@PathVariable String id) {
    try{
      Author author = bookService.getAuthorByBookId(id);
      return new ResponseEntity<Author>(author, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/available")
  public ResponseEntity<?> getAllAvailableBooks() {
    try {
      List<Book> books = bookService.getBooksListByStatus(true);
      return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/rented")
  public ResponseEntity<?> getAllRentedBooks() {
    try {
      List<Book> books = bookService.getBooksListByStatus(false);
      return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

}
