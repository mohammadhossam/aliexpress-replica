package com.msa.repos;

import com.msa.models.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MachinesRepo extends MongoRepository<Machine, String> { }
