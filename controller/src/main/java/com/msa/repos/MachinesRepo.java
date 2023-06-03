package com.msa.repos;

import com.msa.models.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface MachinesRepo extends MongoRepository<Machine, Long> {

}
