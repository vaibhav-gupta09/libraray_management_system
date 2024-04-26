package com.olikproject.olik.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.olikproject.olik.exception.BookAlreadyRentedException;
import com.olikproject.olik.model.Rental;
import com.olikproject.olik.service.RentalService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/rental")
@Validated
public class RentalController {

  @Autowired
  private RentalService rentalService;

  @GetMapping("/{id}")
  public ResponseEntity<?> getRentalRecordById(@PathVariable String id) {
    try {
      Rental rental = rentalService.getRentalById(id);
      return new ResponseEntity<Rental>(rental, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<?> createRentalRecord(@RequestBody @Valid Rental rental) {
    try {
      Rental createdRental = rentalService.createRentalRecord(rental);
      return new ResponseEntity<Rental>(createdRental, HttpStatus.OK);
    } catch (BookAlreadyRentedException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteRentalRecordById(@PathVariable String id) {
    try {
      rentalService.deleteRentalRecordById(id);
      return new ResponseEntity<>("Successfully deleted rental record with id " + id, HttpStatus.OK);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateRentalRecord(@PathVariable String id, @RequestBody Rental updatedRental) {
    try {
      Rental savedRental = rentalService.updateRentalRecordById(id, updatedRental);
      return new ResponseEntity<Rental>(savedRental, HttpStatus.OK);
    } catch (BookAlreadyRentedException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (NoSuchElementException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/overdue")
  public ResponseEntity<?> getOverdueRentalRecords() {
    try {
      List<Rental> overdueRentals = rentalService.getOverdueRentalRecords();
      return new ResponseEntity<List<Rental>>(overdueRentals, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

}
