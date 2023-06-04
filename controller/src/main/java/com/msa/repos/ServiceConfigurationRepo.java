package com.msa.repos;


import com.msa.models.ServiceConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceConfigurationRepo extends MongoRepository<ServiceConfiguration, String> {
    @Query("{ 'serviceName' : ?0 }")
    ServiceConfiguration findByServiceName(String serviceName);
}
