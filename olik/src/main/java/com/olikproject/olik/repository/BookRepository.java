package com.olikproject.olik.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.olikproject.olik.model.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
   List<Book> findByIsAvailable(boolean isAvailable);
}
