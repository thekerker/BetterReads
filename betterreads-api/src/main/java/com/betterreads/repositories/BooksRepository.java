package com.betterreads.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.betterreads.models.Book;

/**
 * <p>
 * MongoDB repository for Books collections
 * </p>
 */
@Repository
public interface BooksRepository extends MongoRepository<Book, String> {

}
