package com.sb.solutions.web.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.user.repository.UserRevisionRepository;

/**
 * @author Elvin Shrestha on 11/12/2019
 */
@RestController
@RequestMapping(UserRevisionController.URL)
public class UserRevisionController {

    static final String URL = "/v1/user";

    private final UserRevisionRepository revisionRepository;

    public UserRevisionController(
        UserRevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    @GetMapping("/{userId}/revisions")
    public List<?> getRevisions(@PathVariable Long userId) {
        return this.revisionRepository.getRevisions(userId);
    }


}
