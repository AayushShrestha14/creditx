package com.sb.solutions.web.forms.v1;

import com.sb.solutions.api.forms.entity.Forms;
import com.sb.solutions.api.forms.service.FormsService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author : Rujan Maharjan on  11/7/2020
 **/


@RestController
@RequestMapping(FormsController.URL)
public class FormsController {

    static final String URL = "/v1/forms";

    private static final Logger logger = LoggerFactory.getLogger(FormsController.class);

    private final FormsService formsService;

    public FormsController(@Autowired FormsService formsService) {
        this.formsService = formsService;
    }

    @PostMapping
    public ResponseEntity<?> saveForms(@Valid @RequestBody Forms forms) {
        logger.info("saving forms....");
        return new RestResponseDto().successModel(formsService.save(forms));
    }

    @PostMapping("/list")
    public ResponseEntity<?> getFormsByFilter(@RequestBody Map<String, String> search,
                                              @RequestParam("page") int page,
                                              @RequestParam("size") int size) {
        return new RestResponseDto().successModel(formsService.findPageableBySpec(search, PaginationUtils
                .pageable(page, size)));
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<?> saveForms(@PathVariable Long id) {
        logger.info("getting forms....");
        return new RestResponseDto().successModel(formsService.findOne(id));
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        logger.info("deleting forms....{}",id);
//        todo check whether form is tag or not
        formsService.deleteById(id);
        return new RestResponseDto().successModel("Deleted Successfully");
    }
}
