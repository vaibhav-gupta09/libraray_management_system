package com.olikproject.olik.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.olikproject.olik.model.Author;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String>{

}
