package com.msa.repos;

import com.msa.models.RunningResource;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface RunningResourcesRepo extends MongoRepository<RunningResource, String> {
    @Query(value = "{ 'resourceName': ?0 }")
    RunningResource findByResourceName(String resourceName);
}
