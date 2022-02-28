package com.sb.solutions.api.userNotification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.userNotification.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>,
    JpaSpecificationExecutor<Message> {

}
