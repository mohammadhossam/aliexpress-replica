package com.msa.repos;

import com.msa.models.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepo extends MongoRepository<Image, String> {
}
