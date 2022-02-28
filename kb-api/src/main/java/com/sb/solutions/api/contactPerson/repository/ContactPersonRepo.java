package com.sb.solutions.api.contactPerson.repository;

import com.sb.solutions.api.contactPerson.entity.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPersonRepo extends JpaRepository<ContactPerson, Long> {
}
