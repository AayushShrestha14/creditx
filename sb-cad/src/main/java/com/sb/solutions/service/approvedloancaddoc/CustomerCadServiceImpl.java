package com.sb.solutions.service.approvedloancaddoc;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.loan.entity.CustomerOfferLetter;
import com.sb.solutions.api.loan.entity.OfferLetterDocType;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.entity.CadFile;
import com.sb.solutions.entity.CustomerApprovedLoanCadDocumentation;
import com.sb.solutions.entity.OfferDocument;
import com.sb.solutions.repository.CustomerCadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("customerCadService")
@Transactional
public class CustomerCadServiceImpl implements CustomerCadService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerCadServiceImpl.class);

    private final CustomerCadRepository customerCadRepository;

    public CustomerCadServiceImpl(CustomerCadRepository customerCadRepository) {
        this.customerCadRepository = customerCadRepository;
    }

    @Override
    public List<CustomerApprovedLoanCadDocumentation> findAll() {
        return customerCadRepository.findAll();
    }

    @Override
    public CustomerApprovedLoanCadDocumentation findOne(Long id) {
        return customerCadRepository.getOne(id);
    }

    @Override
    public CustomerApprovedLoanCadDocumentation save(CustomerApprovedLoanCadDocumentation c) {
        return customerCadRepository.save(c);
    }

    @Override
    public Page<CustomerApprovedLoanCadDocumentation> findAllPageable(Object t, Pageable pageable) {
        return customerCadRepository.findAll(pageable);
    }

    @Override
    public List<CustomerApprovedLoanCadDocumentation> saveAll(List<CustomerApprovedLoanCadDocumentation> list) {
        return customerCadRepository.saveAll(list);
    }

    @Override
    public CustomerOfferLetter saveWithMultipartFile(MultipartFile multipartFile,
                                                     Long customerApprovedDocId, Long offerLetterId, String type) {
      final CustomerApprovedLoanCadDocumentation customerCad = customerCadRepository.getOne(customerApprovedDocId);
      final OfferDocument offerDocument = customerCad.getOfferDocumentList().stream().filter(od -> od.getId().equals(offerLetterId)).findFirst().get();
        if (offerDocument == null) {
            logger.info("Please select a offer letter and save before uploading file {}",
                    customerCad);
            throw new ServiceValidationException(
                    "Please select a offer letter and save before uploading file");
        }
        String uploadPath = new PathBuilder(UploadDir.initialDocument)
                .buildCustomerCadDocumentPath(
                        customerCad.getLoanHolder().getBranch().getId(),
                        customerCad.getLoanHolder().getId(),
                        offerLetterId,
                        OfferLetterDocType.valueOf(type).toString());

        final StringBuilder nameBuilder = new StringBuilder().append(offerDocument.getDocName().trim().replaceAll("\\s", "-"))
                .append("-")
                .append(customerCad.getLoanHolder().getIdNumber().replace("/","-"));
        logger.info("namebuilder{}:::",nameBuilder);
        logger.info("File Upload Path offer letter {} {}", uploadPath, nameBuilder);
        ResponseEntity responseEntity = FileUploadUtils
                .uploadFile(multipartFile, uploadPath, nameBuilder.toString());
        RestResponseDto restResponseDto = (RestResponseDto) responseEntity.getBody();
            for (OfferDocument c : customerCad.getOfferDocumentList()) {
                if (c.getId().equals(offerLetterId)) {
                    if (OfferLetterDocType.valueOf(type).equals(OfferLetterDocType.DRAFT)) {
                        c.setDraftPath(restResponseDto.getDetail().toString());
                    } else {
                        c.setPathSigned(restResponseDto.getDetail().toString());
                    }
                    break;
                }
            }
            customerCadRepository.save(customerCad);

        return null;


    }

    @Override
    public CustomerApprovedLoanCadDocumentation saveCadCheckListDoc(MultipartFile multipartFile, Long customerInfoId, Long loanId,Long customerApprovedDocId,String documentName,Long documentId){
        CustomerApprovedLoanCadDocumentation customerCad = customerCadRepository.getOne(customerApprovedDocId);
        List<CadFile> cadExistingFile =  customerCad.getCadFileList();


        String path = new PathBuilder(UploadDir.initialDocument).buildCustomerCadDocumentCheckListPath
            (customerCad.getLoanHolder().getBranch().getId(),
            customerInfoId,loanId,customerApprovedDocId);
        ResponseEntity responseEntity = FileUploadUtils
            .uploadFile(multipartFile, path, StringUtils.trimAllWhitespace(documentName));
        RestResponseDto restResponseDto = (RestResponseDto) responseEntity.getBody();
        logger.info("Upload Doc Cad Check List path {}",restResponseDto.getDetail().toString());

        if (!customerCad.getCadFileList().isEmpty()) {
            List<CadFile> cadExistFile = customerCad.getCadFileList().stream().filter(existingFile -> existingFile.getCadDocument().getId().equals(documentId) && existingFile.getCustomerLoanId().equals(loanId)).collect(
                Collectors.toList());

            for (CadFile file : customerCad.getCadFileList()) {
                if(file.getCadDocument().getId().equals(documentId) && file.getCustomerLoanId().equals(loanId)){
                    file.setPath(restResponseDto.getDetail().toString());
                    file.setUploadedDate(new Date());
                }
            }
            if (ObjectUtils.isEmpty(cadExistFile)){
                CadFile cadFile = new CadFile();
                List<CadFile> cadFileList = new ArrayList<>();
                Document document= new Document();
                document.setId(documentId);
                cadFile.setCadDocument(document);
                cadFile.setCustomerLoanId(loanId);
                cadFile.setPath(restResponseDto.getDetail().toString());
                cadFile.setUploadedDate(new Date());
                cadFileList.add(cadFile);
                cadFileList.addAll(cadExistingFile);
                customerCad.setCadFileList(cadFileList);
            }
        } else {
            CadFile cadFile = new CadFile();
            List<CadFile> cadFileList = new ArrayList<>();
            Document document = new Document();
            document.setId(documentId);
            cadFile.setCadDocument(document);
            cadFile.setCustomerLoanId(loanId);
            cadFile.setPath(restResponseDto.getDetail().toString());
            cadFile.setUploadedDate(new Date());
            cadFileList.add(cadFile);
            customerCad.setCadFileList(cadFileList);
        }
       return customerCadRepository.save(customerCad);
    }

    @Override
    public String uploadAdditionalDocument(MultipartFile multipartFile, Long cadId,
        String docName,Long branchId,Long customerInfoId) {
        String path = new PathBuilder(UploadDir.initialDocument).buildCustomerAdditionalDocPath
            (branchId,
                customerInfoId,cadId);
        ResponseEntity responseEntity = FileUploadUtils
            .uploadFile(multipartFile, path, StringUtils.trimAllWhitespace(docName));
        RestResponseDto restResponseDto = (RestResponseDto) responseEntity.getBody();
        return String.valueOf(restResponseDto.getDetail());
    }

    @Override
    public String sccFilePath(MultipartFile multipartFile, Long cadId,Long branchId,Long customerInfoId){
        String path = new PathBuilder(UploadDir.initialDocument).buildCustomerCadSccDocPath
            (branchId, customerInfoId,cadId);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        ResponseEntity responseEntity = FileUploadUtils
            .uploadFile(multipartFile, path, StringUtils.trimAllWhitespace(timeStamp));
        RestResponseDto restResponseDto = (RestResponseDto) responseEntity.getBody();
        logger.info("Upload Cad SCC doc path {}",restResponseDto.getDetail().toString());
        return String.valueOf(restResponseDto.getDetail());

    }

}
