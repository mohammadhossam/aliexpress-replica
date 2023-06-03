package com.msa.repos;

import com.msa.models.RunningResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RunningResourcesRepo extends MongoRepository<RunningResource, Long> {
    @Query(value = "{ 'resourceName': ?0 }")
    RunningResource findByResourceName(String resourceName);
}
