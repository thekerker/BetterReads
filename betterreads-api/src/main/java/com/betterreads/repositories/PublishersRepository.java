package com.betterreads.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.betterreads.models.Publisher;

/**
 * <p>
 * MongoDB repository for Publishers collections
 * </p>
 */
@Repository
public interface PublishersRepository extends MongoRepository<Publisher, String> {

}
