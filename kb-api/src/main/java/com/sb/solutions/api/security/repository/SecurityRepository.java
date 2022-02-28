package com.sb.solutions.api.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.security.entity.Security;

public interface SecurityRepository extends JpaRepository<Security,Long> {
}
