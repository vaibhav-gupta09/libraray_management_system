package com.olikproject.olik.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.olikproject.olik.model.Author;
import com.olikproject.olik.model.Book;
import com.olikproject.olik.repository.AuthorRepository;
import com.olikproject.olik.repository.BookRepository;

@ExtendWith(MockitoExtension.class)

public class bookServiceTest {
  @Mock
  private BookRepository bookRepository;

  @Mock
  private AuthorRepository authorRepository;

  @InjectMocks
  private BookService bookService;

  @Mock
  private AuthorService authorService;

  private Book book1;
  private Book book2;
  private Author existingAuthor;

  @BeforeEach
  public void setUp() {
    book1 = new Book("bookId1", "Title One", "AuthorId1", "0-306-40615-5", true, 2022, new Date(), new Date());
    book2 = new Book("bookId2", "Title Two", "AuthorId2", "0-306-40615-6", true, 2023, new Date(), new Date());
    List<String> bookIds = new ArrayList<>();
    existingAuthor = new Author("AuthorId1", "Test Author", "this is test author", bookIds, new Date(), new Date());
  }

  @Test
  public void testGetAllBooks() {

    List<Book> mockBooks = Arrays.asList(book1, book2);

    when(bookRepository.findAll()).thenReturn(mockBooks);

    List<Book> returnedBooks = bookService.getAllBooks();

    verify(bookRepository, times(1)).findAll();

    assertEquals(2, returnedBooks.size());
    assertEquals(book1, returnedBooks.get(0));
    assertEquals(book2, returnedBooks.get(1));
  }

  @Test
  public void testGetBookById_ExistingId() {
    when(bookRepository.findById("1")).thenReturn(Optional.of(book1));
    Book returnedBook = bookService.getBookById("1");
    verify(bookRepository, times(1)).findById("1");
    assertEquals(book1, returnedBook);
  }

  @Test
  public void testGetBookById_NonExistingId() {
    when(bookRepository.findById("3")).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> {
      bookService.getBookById("3");
    });
    verify(bookRepository, times(1)).findById("3");
  }

  @Test
  public void testCreateBook_Success() {
    when(authorService.getAuthorById("AuthorId1")).thenReturn(existingAuthor);
    when(bookRepository.save(book1)).thenReturn(book1);
    Book createdBook = bookService.createBook(book1);
    verify(authorService, times(1)).getAuthorById("AuthorId1");
    verify(bookRepository, times(1)).save(book1);
    assertEquals(true, createdBook.getCreatedAt() instanceof Date);
    assertEquals(1, existingAuthor.getBookIds().size());
    assertEquals("bookId1", existingAuthor.getBookIds().get(0));
    verify(authorService, times(1)).saveAuthor(existingAuthor);
    assertEquals(book1, createdBook);
  }

  @Test
  public void testCreateBook_AuthorNotFound() {
    when(authorService.getAuthorById("AuthorId2")).thenThrow(new NoSuchElementException("Author not found"));
    assertThrows(NoSuchElementException.class, () -> {
      bookService.createBook(book2);
    });
    verify(authorService, times(1)).getAuthorById("AuthorId2");
  }

  @Test
  public void testDeleteBookById_Success() {
    when(bookRepository.findById("bookId1")).thenReturn(Optional.of(book1));
    when(authorService.getAuthorById("AuthorId1")).thenReturn(existingAuthor);
    doNothing().when(bookRepository).deleteById("bookId1");
    bookService.deleteBookById("bookId1");
    verify(bookRepository, times(1)).findById("bookId1");
    verify(authorService, times(1)).getAuthorById("AuthorId1");
    verify(bookRepository, times(1)).deleteById("bookId1");
    verify(authorService, times(1)).saveAuthor(existingAuthor);
  }

  @Test
  public void testDeleteBookById_BookNotFound() {
    when(bookRepository.findById("bookId3")).thenThrow(new NoSuchElementException("Book not found"));
    assertThrows(NoSuchElementException.class, () -> {
      bookService.deleteBookById("bookId3");
    });
    verify(bookRepository, times(1)).findById("bookId3");
  }

  @Test
  public void testUpdateBookById_Success() {
    when(bookRepository.findById("bookId1")).thenReturn(Optional.of(book1));
    List<String> bookIds = new ArrayList<>();
    Author updatedAuthor = new Author("AuthorId2", "Test Author 2", "this is test author", bookIds, new Date(),
        new Date());
    when(authorService.getAuthorById("AuthorId1")).thenReturn(existingAuthor);
    when(authorService.getAuthorById("AuthorId2")).thenReturn(updatedAuthor);
    when(authorService.isAuthorExist("AuthorId2")).thenReturn(true);
    Book updatedBook = new Book("bookId1", "Updated Title", "AuthorId2", "0-306-40615-7", true, 2023, new Date(),
        new Date());
    bookService.updateBookById("bookId1", updatedBook);
    verify(bookRepository, times(1)).findById("bookId1");
    verify(authorService, times(1)).isAuthorExist("AuthorId2");
    verify(authorService, times(1)).getAuthorById("AuthorId1");
    verify(authorService, times(1)).getAuthorById("AuthorId2");
    verify(authorService, times(1)).saveAuthor(existingAuthor);
    verify(authorService, times(1)).saveAuthor(updatedAuthor);
    verify(bookRepository, times(1)).save(book1);
  }

  @Test
  public void testUpdateBookById_AuthorNotFound() {
    when(bookRepository.findById("bookId1")).thenReturn(Optional.of(book1));
    when(authorService.isAuthorExist("AuthorId2")).thenReturn(false);
    Book updatedBook = new Book("bookId1", "Updated Title", "AuthorId2", "ISBN0987654321", true, 2023, new Date(),
        new Date());
    assertThrows(NoSuchElementException.class, () -> {
      bookService.updateBookById("bookId1", updatedBook);
    });

  }

}
