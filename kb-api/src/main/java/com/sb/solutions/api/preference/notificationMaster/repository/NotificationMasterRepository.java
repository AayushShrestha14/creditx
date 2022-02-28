package com.sb.solutions.api.preference.notificationMaster.repository;

import org.springframework.stereotype.Repository;

import com.sb.solutions.api.preference.notificationMaster.entity.NotificationMaster;
import com.sb.solutions.core.repository.BaseRepository;

@Repository
public interface NotificationMasterRepository extends BaseRepository<NotificationMaster, Long> {

}
