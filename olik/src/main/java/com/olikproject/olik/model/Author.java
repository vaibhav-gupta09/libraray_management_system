package com.olikproject.olik.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "authors")
public class Author {
  @Id
  private String id;

  @NotNull(message = "Author Name must not be null")
  @NotBlank(message = "Author Name not be blank")
  private String name;

  @NotNull(message = "Biography must not be null")
  @NotBlank(message = "Biographynot be blank")
  private String biography;

  private List<String> bookIds;

  private Date createdAt;
  private Date updatedAt;
}
