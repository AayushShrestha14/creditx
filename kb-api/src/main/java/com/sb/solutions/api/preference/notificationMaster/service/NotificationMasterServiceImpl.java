package com.sb.solutions.api.preference.notificationMaster.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.preference.notificationMaster.entity.NotificationMaster;
import com.sb.solutions.api.preference.notificationMaster.repository.NotificationMasterRepository;
import com.sb.solutions.api.preference.notificationMaster.repository.spec.NotificationMasterSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

@Service
public class NotificationMasterServiceImpl extends
    BaseServiceImpl<NotificationMaster, Long> implements NotificationMasterService {

    private final NotificationMasterRepository repository;

    public NotificationMasterServiceImpl(
        NotificationMasterRepository repository) {
        super(repository);

        this.repository = repository;
    }

    @Override
    protected BaseSpecBuilder<NotificationMaster> getSpec(Map<String, String> filterParams) {
        return new NotificationMasterSpecBuilder(filterParams);
    }
}
