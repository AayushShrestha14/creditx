package com.sb.solutions.api.proposal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.proposal.entity.Proposal;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {

}
