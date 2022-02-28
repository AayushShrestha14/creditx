package com.sb.solutions.api.proposal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.api.proposal.repository.ProposalRepository;

@Service
public class ProposalServiceImpl implements ProposalService {

    private final ProposalRepository proposalRepository;

    @Autowired
    public ProposalServiceImpl(
        ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @Override
    public List<Proposal> findAll() {
        return proposalRepository.findAll();
    }

    @Override
    public Proposal findOne(Long id) {
        return proposalRepository.getOne(id);
    }

    @Override
    public Proposal save(Proposal proposal) {
        return proposalRepository.save(proposal);
    }

    @Override
    public Page<Proposal> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<Proposal> saveAll(List<Proposal> list) {
        return proposalRepository.saveAll(list);
    }
}
