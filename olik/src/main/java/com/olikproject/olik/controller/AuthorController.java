package com.olikproject.olik.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.olikproject.olik.model.Author;
import com.olikproject.olik.service.AuthorService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/author")
@Validated
public class AuthorController {

  @Autowired
  private AuthorService authorService;

  @GetMapping("/all")
  public ResponseEntity<?> getAllAuthors() {
    try {
      List<Author> authors = authorService.getAllAuthors();
      return new ResponseEntity<List<Author>>(authors, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getAuthorById(@PathVariable String id) {
    try {
      Author author = authorService.getAuthorById(id);
      return new ResponseEntity<Author>(author, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<?> createAuthor(@RequestBody @Valid Author author) {
    try {
      Author createdAuthor = authorService.createAuthor(author);
      return new ResponseEntity<Author>(createdAuthor, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateAuthorById(@PathVariable String id, @RequestBody Author updatedAuthor) {
    try {
      Author updatedAuthorEntity = authorService.updateAuthorById(id, updatedAuthor);
      return new ResponseEntity<Author>(updatedAuthorEntity, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteAurhorById(@PathVariable String id) {
    try {
      authorService.deleteAuthorById(id);
      return new ResponseEntity<>("Successfully deleted Author with id " + id, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/all-books/{id}")
  public ResponseEntity<?> getAllBooksByAuthor(@PathVariable String id) {
    try {
      List<String> bookNames = authorService.getAllBooksByAuthor(id);
      return new ResponseEntity<List<String>>(bookNames, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
