package com.msa.repos;

import com.msa.models.RunningInstance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunningInstanceRepo extends MongoRepository<RunningInstance, String> { }
