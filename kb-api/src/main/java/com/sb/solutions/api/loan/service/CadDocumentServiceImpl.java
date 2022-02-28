package com.sb.solutions.api.loan.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.loan.entity.CadDocument;
import com.sb.solutions.api.loan.repository.CadDocumentRepository;

@Service
public class CadDocumentServiceImpl implements CadDocumentService{

    private final CadDocumentRepository cadDocumentRepository;

    public CadDocumentServiceImpl(
        CadDocumentRepository cadDocumentRepository) {
        this.cadDocumentRepository = cadDocumentRepository;
    }

    @Override
    public List<CadDocument> findAll() {
        return cadDocumentRepository.findAll();
    }

    @Override
    public CadDocument findOne(Long id) {
        return cadDocumentRepository.getOne(id);
    }

    @Override
    public CadDocument save(CadDocument cadDocument) {
        return cadDocumentRepository.save(cadDocument);
    }

    @Override
    public Page<CadDocument> findAllPageable(Object t, Pageable pageable) {
        return cadDocumentRepository.findAll(pageable);
    }

    @Override
    public List<CadDocument> saveAll(List<CadDocument> list) {
        return cadDocumentRepository.saveAll(list);
    }

    @Override
    public int deleteByLoanIdAndDocument(Long longId, Long docId) {
        return cadDocumentRepository.deleteByLoanIdAndDocument(longId, docId);
    }

    @Override
    public void deleteById(Long customerDocId) {
        cadDocumentRepository.deleteById(customerDocId);
    }
}
