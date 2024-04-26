package com.olikproject.olik.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

// import javax.validation.constraints.NotNull;

import lombok.*;

import org.springframework.data.annotation.Id;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Document(collection = "books")
public class Book {
  @Id
  private String id;

  @NotNull(message = "Title must not be null")
  @NotBlank(message = "Title must not be blank")
  private String title;

  @NotNull(message = "AuthorId must not be null")
  @NotBlank(message = "AuthorId must not be blank")
  private String authorId;

  @NotNull(message = "Isbn must not be null")
  @NotBlank(message = "Isbn must not be blank")
  private String isbn;

  private Boolean isAvailable = true;

  private int publicationYear;
  private Date createdAt;
  private Date updatedAt;
}
