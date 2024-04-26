package com.olikproject.olik.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.olikproject.olik.model.Rental;

@Repository
public interface RentalRepository extends MongoRepository<Rental, String> {
    List<Rental> findByReturnDateBefore(Date currentDate);
}
