package com.sb.solutions.api.document.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.document.entity.LoanCycle;
import com.sb.solutions.api.document.repository.DocumentRepository;
import com.sb.solutions.api.document.repository.LoanCycleRepository;
import com.sb.solutions.core.constant.FilePath;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.utils.ProductUtils;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final LoanCycleRepository loanCycleRepository;

    public DocumentServiceImpl(@Autowired DocumentRepository documentRepository,
                               @Autowired LoanCycleRepository loanCycleRepository) {
        this.documentRepository = documentRepository;
        this.loanCycleRepository = loanCycleRepository;
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document findOne(Long id) {
        try {
            return documentRepository.findById(id).get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Document save(Document document) {
        document.setName(document.getDisplayName().trim().replaceAll("\\s", "-"));
        document.setLastModifiedAt(new Date());
        if (document.getId() == null) {
            document.setStatus(Status.ACTIVE);
        }
        return documentRepository.save(document);
    }

    @Override
    public Page<Document> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(t, SearchDto.class);
        return documentRepository.documentFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<Document> saveAll(List<Document> list) {
        return documentRepository.saveAll(list);
    }


    @Override
    public List<Document> getByCycleContainingAndStatus(Long loanCycleId, String statusName) {
        LoanCycle loanCycle = loanCycleRepository.getOne(loanCycleId);
        Status status = Status.valueOf(statusName);
        return documentRepository.findByLoanCycleContainingAndStatus(loanCycle, status);
    }

    @Override
    public Map<Object, Object> documentStatusCount() {
        return documentRepository.documentStatusCount();
    }

    @Override
    public String saveList(List<Long> ids, LoanCycle loanCycle) {
        Status status = Status.valueOf("ACTIVE");
        if (ids.size() == 0) {
            for (Document document : documentRepository
                    .findByLoanCycleContainingAndStatus(loanCycle, status)) {
                document.getLoanCycle().remove(loanCycle);
                documentRepository.save(document);
            }
            return "Success";
        }
        for (Document document : documentRepository
                .findByLoanCycleContainingAndStatus(loanCycle, status)) {
            document.getLoanCycle().remove(loanCycle);
        }
        for (Long id : ids) {
            Document doc = documentRepository.getOne(id);
            doc.getLoanCycle().add(loanCycle);
            documentRepository.save(doc);
        }
        return "Success";
    }

    @Override
    public List<Document> getByStatus(String statusName) {
        Status status = Status.valueOf(statusName);
        return documentRepository.findByStatus(status);
    }

    @Override
    public String downloadAllDoc(String sourcepath, String SourcePathCustomer, String SourceCadDocPath) {
        String rootth = String.join("/",
                (Arrays.asList(sourcepath.split("/"))
                        .subList(0, Arrays.asList(sourcepath
                                .split("/")).size() - 1)));

        String destinationPath = rootth
                + "/zipFolder/customerDocument.zip";

        String destinationCadDocumentPath = null;

        if (ProductUtils.CAD_LITE_VERSION) {
            destinationCadDocumentPath = rootth
                    + "/zipFolder/cadDocument.zip";
        }

        String destinationCustomerDocumentPath = rootth
                + "/zipFolder/loanDocument.zip";

        String parentDocumentPath = rootth
                + "/allDocument.zip";

        String sourcePathParent = rootth
                + "/zipFolder/";

        Path path = Paths.get(FilePath.getOSPath() + sourcePathParent);
        if (!Files.exists(path)) {
            new File(FilePath.getOSPath() + sourcePathParent).mkdirs();
        }

        try {

            FileUploadUtils.createZip(UploadDir.WINDOWS_PATH + sourcepath,
                    UploadDir.WINDOWS_PATH + destinationPath);
            FileUploadUtils.createZip(UploadDir.WINDOWS_PATH + SourcePathCustomer,
                    UploadDir.WINDOWS_PATH + destinationCustomerDocumentPath);
            if (!ObjectUtils.isEmpty(SourceCadDocPath)) {
                FileUploadUtils.createZip(UploadDir.WINDOWS_PATH + SourceCadDocPath,
                        UploadDir.WINDOWS_PATH + destinationCadDocumentPath);
            }
            FileUploadUtils.createZip(UploadDir.WINDOWS_PATH + sourcePathParent,
                    UploadDir.WINDOWS_PATH + parentDocumentPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parentDocumentPath;
    }
}
