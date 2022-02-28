package com.sb.solutions.api.postApprovalDocument.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.postApprovalDocument.entity.OfferLetter;
import com.sb.solutions.api.postApprovalDocument.repository.OfferLetterRepository;

@Service
public class OfferLetterServiceImpl implements OfferLetterService {

    private final OfferLetterRepository offerLetterRepository;

    public OfferLetterServiceImpl(@Autowired OfferLetterRepository offerLetterRepository) {
        this.offerLetterRepository = offerLetterRepository;
    }

    @Override
    public List<OfferLetter> findAll() {
        return offerLetterRepository.findAll();
    }

    @Override
    public OfferLetter findOne(Long id) {
        return offerLetterRepository.getOne(id);
    }

    @Override
    public OfferLetter save(OfferLetter offerLetter) {
        return offerLetterRepository.save(offerLetter);
    }

    @Override
    public Page<OfferLetter> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<OfferLetter> saveAll(List<OfferLetter> list) {
        return offerLetterRepository.saveAll(list);
    }
}
