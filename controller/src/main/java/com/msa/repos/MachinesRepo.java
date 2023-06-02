package com.msa.repos;

import com.msa.models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachinesRepo extends JpaRepository<Machine, Long> {
}
