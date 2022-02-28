package com.sb.solutions.api.loan.service;

import java.util.List;
import com.google.gson.Gson;
import com.sb.solutions.core.utils.file.DeleteFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.loan.entity.CustomerDocument;
import com.sb.solutions.api.loan.repository.CustomerDocumentRepository;

/**
 * @author yunish on 11/5/2019
 */
@Service
public class CustomerDocumentServiceImpl implements CustomerDocumentService {

    private final CustomerDocumentRepository customerDocumentRepository;
    private Gson gson;

    public CustomerDocumentServiceImpl(
        @Autowired CustomerDocumentRepository customerDocumentRepository) {
        this.customerDocumentRepository = customerDocumentRepository;
    }

    @Override
    public List<CustomerDocument> findAll() {
        return customerDocumentRepository.findAll();
    }

    @Override
    public CustomerDocument findOne(Long id) {
        return customerDocumentRepository.getOne(id);
    }

    @Override
    public CustomerDocument save(CustomerDocument customerDocument) {
        return customerDocumentRepository.save(customerDocument);
    }

    @Override
    public Page<CustomerDocument> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CustomerDocument> saveAll(List<CustomerDocument> list) {
        return null;
    }

    @Override
    public String deleteCustomerDocFromSystem(String path) {
        DeleteFileUtils.deleteFile(path);
        return "Successfully deleted";
    }

    @Override
    public void deleteById(Long customerDocId) {
        customerDocumentRepository.deleteById(customerDocId);
    }
}
