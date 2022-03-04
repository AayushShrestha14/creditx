package com.sb.solutions.web.proposal.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProposalController.URL)

public class ProposalController {

    static final String URL = "/v1/proposal";


}

