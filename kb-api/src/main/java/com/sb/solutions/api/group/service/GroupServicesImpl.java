package com.sb.solutions.api.group.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.group.entity.Group;
import com.sb.solutions.api.group.repository.GroupRepository;


@Service
public class GroupServicesImpl implements GroupServices {

    final GroupRepository groupRepository;

    @Autowired
    public GroupServicesImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group findOne(Long id) {
        return groupRepository.getOne(id);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }


    @Override
    public Page<Group> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<Group> saveAll(List<Group> list) {
        return groupRepository.saveAll(list);
    }

}
