package com.olikproject.olik.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rentals")
public class Rental {
  @Id
  private String id;
  
  @NotNull(message = "BookId must not be null")
  @NotBlank(message = "BookId must not be blank")
  private String bookId;

  @NotNull(message = "Renter Name must not be null")
  @NotBlank(message = "Renter Name not be blank")
  private String renterName;

  private Date rentalDate;

  private Date returnDate;
}
