package com.msa.repos;

import com.msa.models.RunningInstance;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RunningInstanceRepo extends JpaRepository<RunningInstance, Long>{ }
