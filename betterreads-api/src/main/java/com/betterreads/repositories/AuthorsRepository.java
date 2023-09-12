package com.betterreads.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.betterreads.models.Author;

/**
 * <p>
 * MongoDB repository for Authors collection
 * </p>
 */
@Repository
public interface AuthorsRepository extends MongoRepository<Author, String> {

}
