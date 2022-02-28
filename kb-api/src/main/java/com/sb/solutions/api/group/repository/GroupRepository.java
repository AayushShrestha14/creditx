package com.sb.solutions.api.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.group.entity.Group;

public interface GroupRepository extends JpaRepository<Group,Long> {

}
