package com.olikproject.olik.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olikproject.olik.exception.InvalidIsbnException;
import com.olikproject.olik.model.Author;
import com.olikproject.olik.model.Book;
import com.olikproject.olik.repository.BookRepository;
import com.olikproject.olik.utils.NullUtil;

@Service
public class BookService {
  @Autowired
  private BookRepository bookRepo;

  @Autowired
  private AuthorService authorService;

  public List<Book> getAllBooks() {
    return bookRepo.findAll();
  }

  public Book getBookById(String id) {
    Optional<Book> optionalBook = bookRepo.findById(id);
    if (optionalBook.isPresent()) {
      return optionalBook.get();
    } else {
      throw new NoSuchElementException("Book not found with id: " + id);
    }
  }

  public Book createBook(Book book){
    Author author = authorService.getAuthorById(book.getAuthorId());
    matchIsbn(book.getIsbn());
    book.setCreatedAt(new Date());
    Book savedBook = saveBook(book);
    author.getBookIds().add(book.getId());
    authorService.saveAuthor(author);
    return savedBook;
  }

  public Book updateBookById(String id, Book updatedBook){
    Book currentBook = getBookById(id);
    if (updatedBook.getAuthorId() != null && !updatedBook.getAuthorId().isEmpty() && !authorService
        .isAuthorExist(updatedBook.getAuthorId())) {
      throw new NoSuchElementException("No author found by given id");
    }
    matchIsbn(updatedBook.getIsbn());
    if (currentBook.getAuthorId() != updatedBook.getAuthorId()) {
      Author currAuthor = authorService.getAuthorById(currentBook.getAuthorId());
      currAuthor.getBookIds().remove(currentBook.getId());
      Author newAuthor = authorService.getAuthorById(updatedBook.getAuthorId());
      newAuthor.getBookIds().add(currentBook.getId());
      authorService.saveAuthor(currAuthor);
      authorService.saveAuthor(newAuthor);
      NullUtil.updateIfPresent(currentBook::setAuthorId, updatedBook.getAuthorId());
    }
    currentBook.setUpdatedAt(new Date());

    NullUtil.updateIfPresent(currentBook::setTitle, updatedBook.getTitle());
    NullUtil.updateIfPresent(currentBook::setIsbn, updatedBook.getIsbn());
    NullUtil.updateIfPresent(currentBook::setPublicationYear, updatedBook.getPublicationYear());
    Book savedBook = saveBook(currentBook);
    return savedBook;
  }

  public void deleteBookById(String id){
    Book book = getBookById(id);
    Author author = authorService.getAuthorById(book.getAuthorId());
    author.getBookIds().remove(book.getId());
    bookRepo.deleteById(id);
    authorService.saveAuthor(author);
  }

  public Author getAuthorByBookId(String id){
    Book book = getBookById(id);
    Author author = authorService.getAuthorById(book.getAuthorId());
    return author;
  }

  public List<Book> getBooksListByStatus(boolean status){
    List<Book> books = bookRepo.findByIsAvailable(status);
    return books;
  }

  public Book saveBook(Book book) {
    return bookRepo.save(book);
  }

  public boolean isBookExist(String id) {
    return bookRepo.existsById(id);
  }

  public void matchIsbn(String isbn){
      if(isbn == null || isbn.isEmpty())
        return;
      String regex = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(isbn);
      if(!matcher.matches())
        throw new InvalidIsbnException("Enter a valid ISBN number " + isbn);
  }
}
