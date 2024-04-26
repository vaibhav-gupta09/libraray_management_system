package com.olikproject.olik.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olikproject.olik.model.Book;
import com.olikproject.olik.service.BookService;


@WebMvcTest(BookController.class)
public class bookControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookService bookService;

  private Book book1;
  private Book book2;

  @BeforeEach
  public void setUp() {
    book1 = new Book("bookId1", "Title One", "AuthorId1", "0-306-40615-5", true, 2022, new Date(), new Date());
    book2 = new Book("bookId2", "Title Two", "AuthorId2", "0-306-40615-6", true, 2023, new Date(), new Date());
  }


  @Test
  public void testGetAllBooks() throws Exception {
    List<Book> mockBooks = Arrays.asList(book1, book2);
    when(bookService.getAllBooks()).thenReturn(mockBooks);

    mockMvc.perform(MockMvcRequestBuilders.get("/book/all"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Title One"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Title Two"));
  }

  @Test
  public void testGetBookById_Success() throws Exception {
    when(bookService.getBookById("bookId1")).thenReturn(book1);

    mockMvc.perform(MockMvcRequestBuilders.get("/book/bookId1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title One"));
  }

  @Test
  public void testGetBookById_NotFound() throws Exception {
    when(bookService.getBookById("bookId3")).thenThrow(new NoSuchElementException("Book not found"));

    mockMvc.perform(MockMvcRequestBuilders.get("/book/bookId3"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testCreateBook_Success() throws Exception {
    when(bookService.createBook(any(Book.class))).thenReturn(book1);
    String jsonPayload = new ObjectMapper().writeValueAsString(book1);
    mockMvc.perform(MockMvcRequestBuilders.post("/book")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonPayload))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title One"));
  }

}
