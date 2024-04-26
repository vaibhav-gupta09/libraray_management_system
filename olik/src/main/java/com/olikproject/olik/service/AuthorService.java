package com.olikproject.olik.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olikproject.olik.model.Author;
import com.olikproject.olik.model.Book;
import com.olikproject.olik.repository.AuthorRepository;
import com.olikproject.olik.repository.BookRepository;
import com.olikproject.olik.utils.NullUtil;

@Service
public class AuthorService {
  @Autowired
  private AuthorRepository authorRepo;

  @Autowired
  private BookRepository bookRepo;

  public List<Author> getAllAuthors() {
    return authorRepo.findAll();
  }

  public Author getAuthorById(String id) {
    Optional<Author> optionalAuthor = authorRepo.findById(id);
    if (optionalAuthor.isPresent()) {
      return optionalAuthor.get();
    } else {
      throw new NoSuchElementException("Author not found with id: " + id);
    }
  }

  public Author createAuthor(Author author) {
    author.setCreatedAt(new Date());
    author.setBookIds(new ArrayList<>());
    return authorRepo.save(author);
  }

  public Author updateAuthorById(String id, Author updatedAuthor) {
    Author existingAuthor = getAuthorById(id);
    NullUtil.updateIfPresent(existingAuthor::setName, updatedAuthor.getName());
    NullUtil.updateIfPresent(existingAuthor::setBiography, updatedAuthor.getBiography());
    existingAuthor.setUpdatedAt(new Date());
    return authorRepo.save(existingAuthor);
  }

  public void deleteAuthorById(String id) {
    Author author = getAuthorById(id);
    if (!author.getBookIds().isEmpty()) {
      throw new RuntimeException("Delete books associated with author first");
    }
    authorRepo.deleteById(id);
  }

  public List<String> getAllBooksByAuthor(String id) {
    Author author = getAuthorById(id);
    List<String> bookIds = author.getBookIds();
    List<String> bookNames = new ArrayList<>();
    for (String bookId : bookIds) {
      Book book = bookRepo.findById(bookId).orElse(null);
      bookNames.add(book.getTitle());
    }
    return bookNames;
  }

  public Author saveAuthor(Author author){
    return authorRepo.save(author);
  }

  public boolean isAuthorExist(String id){
    return authorRepo.existsById(id);
  }
}
