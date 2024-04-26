package com.olikproject.olik.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olikproject.olik.exception.BookAlreadyRentedException;
import com.olikproject.olik.model.Book;
import com.olikproject.olik.model.Rental;
import com.olikproject.olik.repository.RentalRepository;
import com.olikproject.olik.utils.NullUtil;

@Service
public class RentalService {
  @Autowired
  private RentalRepository rentalRepo;

  @Autowired
  private BookService bookService;

  public Rental getRentalById(String id) {
    Optional<Rental> optionalRental = rentalRepo.findById(id);
    if (optionalRental.isPresent()) {
      return optionalRental.get();
    } else {
      throw new NoSuchElementException("Rental record not found with id: " + id);
    }
  }

  public Rental createRentalRecord(Rental rental) {
    Book book = bookService.getBookById(rental.getBookId());
    if (!book.getIsAvailable()) {
      throw new BookAlreadyRentedException("This book is already rented");
    }
    rental.setRentalDate(new Date());
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DATE, 14);
    rental.setReturnDate(c.getTime());
    Rental createdRental = rentalRepo.save(rental);
    book.setIsAvailable(false);
    bookService.saveBook(book);
    return createdRental;
  }

  public void deleteRentalRecordById(String id) {
    Rental rental = getRentalById(id);
    Book book = bookService.getBookById(rental.getBookId());
    rentalRepo.deleteById(id);
    book.setIsAvailable(true);
    bookService.saveBook(book);
  }

  public Rental updateRentalRecordById(String id, Rental updatedRental) {
    Rental currentRental = getRentalById(id);
    if (updatedRental.getBookId() != null && !updatedRental.getBookId().isEmpty()
        && !bookService.isBookExist(updatedRental.getBookId())) {
      throw new NoSuchElementException("No book found by given id");
    }
    if (currentRental.getBookId() != updatedRental.getBookId()) {
      Book newBook = bookService.getBookById(updatedRental.getBookId());
      if (!newBook.getIsAvailable()) {
        throw new BookAlreadyRentedException("This book is already rented");
      }
      Book currentBook = bookService.getBookById(currentRental.getBookId());
      currentBook.setIsAvailable(true);
      newBook.setIsAvailable(false);
      bookService.saveBook(currentBook);
      bookService.saveBook(newBook);
      NullUtil.updateIfPresent(currentRental::setBookId, updatedRental.getBookId());
    }
    NullUtil.updateIfPresent(currentRental::setRenterName, updatedRental.getRenterName());
    return rentalRepo.save(currentRental);
  }

  public List<Rental> getOverdueRentalRecords() {
    Date currentDate = new Date();
    return rentalRepo.findByReturnDateBefore(currentDate);
  }
}
