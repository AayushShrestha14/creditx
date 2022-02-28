package com.sb.solutions.api.user.repository;

import org.springframework.stereotype.Component;

import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.revision.RevisionRepository;

/**
 * @author Elvin Shrestha on 11/12/2019
 */
@Component
public class UserRevisionRepository extends RevisionRepository<User> {

    @Override
    protected Class<User> getClazz() {
        return User.class;
    }
}
