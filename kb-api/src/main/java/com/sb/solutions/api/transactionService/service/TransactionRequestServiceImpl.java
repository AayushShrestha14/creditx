package com.sb.solutions.api.transactionService.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.loan.dto.CustomerDto;
import com.sb.solutions.core.config.CbsProperties;
import com.sb.solutions.core.dto.ApimsDto;
import com.sb.solutions.core.dto.SignatureDto;
import com.sb.solutions.core.enums.TransactionFunction;
import com.sb.solutions.core.exception.InvalidSignatureException;
import com.sb.solutions.core.exception.InvalidTransactionRequest;
import com.sb.solutions.core.utils.apims.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import static com.sb.solutions.core.constant.APIMSConstants.TIMESTAMP_FORMAT;

@Service
@Slf4j
public class TransactionRequestServiceImpl implements TransactionRequestService {

    private final CbsProperties cbsProperties;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public TransactionRequestServiceImpl(
            CbsProperties cbsProperties) {
        this.cbsProperties = cbsProperties;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(cbsProperties.getUsername(), cbsProperties.getPassword());

        final MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonHttpMessageConverter.getObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).
                setSerializationInclusion(JsonInclude.Include.NON_EMPTY).disable(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);
    }

    @Override
    public CustomerInfo customerInfo(CustomerDto request)
            throws InvalidTransactionRequest {
        final ApimsDto payload = getApiRequest(TransactionFunction.CustomerInfo, request);

        final HttpEntity<ApimsDto> apiRequest = new HttpEntity<>(payload, headers);

        try {
            log.trace("Customer info request: " + request);

//            final Map<String, String> response = new HashMap<>();
            final Map<String, String> response =
                    restTemplate.postForEntity(cbsProperties.getApi(), apiRequest, Map.class).getBody();

            log.trace("CustomerInfo response: " + response);

//            CustomerInfo balanceResponse = CustomerInfo.builder()
//                    .accountNumber(response.getOrDefault("AccountNumber", ""))
//                    .availableBalance(BigDecimal.valueOf(
//                            Double.parseDouble(response.getOrDefault("AvailableBalance", "0"))))
//                    .ledgerBalance(BigDecimal.valueOf(
//                            Double.parseDouble(response.getOrDefault("LedgerBalance", "0"))))
//                    .code(Integer.parseInt(response.getOrDefault("Code", "0")))
//                    .mesage(response.getOrDefault("Message", "")).build();

            return new CustomerInfo();

        } catch (Exception ex) {
            log.error("Customer Info Exception: " + ex.getLocalizedMessage());
            throw new InvalidTransactionRequest(ex.getLocalizedMessage(), ex);
        }
    }

//    @Override
//    public FundTransferResponse fundTransfer(AccountTransferDto request) throws InvalidTransactionRequest {
//        final ApimsDto payload = getApiRequest(TransactionFunction.AccountTransfer, request);
//
//        final HttpEntity<ApimsDto> apiRequest = new HttpEntity<>(payload, headers);
//
//        try {
//            log.trace("Fund Transfer request: " + request);
//
////            final Map<String, String> response = new HashMap<>();
//
//            final Map<String, String> response =
//                    restTemplate.postForEntity(apims.getApi(), apiRequest, Map.class).getBody();
//
//            log.trace("Fund Transfer response: " + response);
//
//            FundTransferResponse fundTransferResponse = FundTransferResponse.builder()
//                    .transactionId(response.getOrDefault("TransactionId", "0"))
//                    .merchantTransactionId(response.getOrDefault("MerchantTransactionId", "0"))
//                    .availableBalance(
//                            BigDecimal.valueOf(Double.parseDouble(response.getOrDefault("AvailableBalance", "0"))))
//                    .ledgerBalance(BigDecimal.valueOf(Double.parseDouble(response.getOrDefault("LedgerBalance", "0"))))
//                    .code(Integer.parseInt(response.getOrDefault("Code", "0")))
//                    .message(response.getOrDefault("Message", ""))
//                    .build();
//
//            return fundTransferResponse;
//
//        } catch (Exception ex) {
//            log.error("Fund Transfer exception: " + ex.getLocalizedMessage());
//            throw new InvalidTransactionRequest(ex.getLocalizedMessage(), ex);
//        }
//
//    }

    private ApimsDto getApiRequest(TransactionFunction function, Object request)
            throws InvalidTransactionRequest {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT,
                Locale.ENGLISH);
        final String currentDateTime = LocalDateTime.now().format(formatter);

        final Gson gson = new Gson();
        final String base64String = Base64.getEncoder()
                .encodeToString(gson.toJson(request).getBytes());

        final SignatureDto signatureDto = new SignatureDto();
        signatureDto.setTimeStamp(currentDateTime);
        signatureDto.setModel(request);

        try {
            final ApimsDto apimsDto = new ApimsDto();
            apimsDto.setFunctionName(function.name());
            apimsDto.setData(base64String);
            apimsDto.setSignature(
                    SignatureUtil.signSHA256RSA(gson.toJson(signatureDto), cbsProperties.getCertFilePath()));
            apimsDto.setTimeStamp(currentDateTime);

            return apimsDto;
        } catch (InvalidSignatureException ex) {
            log.error("Invalid Signature Exception: " + ex.getLocalizedMessage());
            throw new InvalidTransactionRequest(ex.getLocalizedMessage(), ex);
        }
    }

}
