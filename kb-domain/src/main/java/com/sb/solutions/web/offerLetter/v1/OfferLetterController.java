package com.sb.solutions.web.offerLetter.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.postApprovalDocument.service.OfferLetterService;
import com.sb.solutions.core.dto.RestResponseDto;

@RestController
@RequestMapping("/v1/offer-letter")
public class OfferLetterController {

    private final OfferLetterService offerLetterService;

    public OfferLetterController(@Autowired OfferLetterService offerLetterService) {
        this.offerLetterService = offerLetterService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(offerLetterService.findAll());
    }

}
