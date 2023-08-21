package com.sojrel.saccoapi.repository;

import com.sojrel.saccoapi.model.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {



}

