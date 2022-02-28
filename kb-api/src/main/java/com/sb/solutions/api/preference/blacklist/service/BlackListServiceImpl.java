package com.sb.solutions.api.preference.blacklist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.preference.blacklist.entity.BlackList;
import com.sb.solutions.api.preference.blacklist.repository.BlackListRepository;
import com.sb.solutions.core.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;

    public BlackListServiceImpl(
            BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    @Override
    public List<BlackList> findAll() {
        return blackListRepository.findAll();
    }

    @Override
    public BlackList findOne(Long id) {
        return blackListRepository.getOne(id);
    }

    @Override
    public BlackList save(BlackList blackList) {
        return blackListRepository.save(blackList);
    }

    @Override
    public Page<BlackList> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return blackListRepository
                .blackListFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<BlackList> saveAll(List<BlackList> list) {
        return blackListRepository.saveAll(list);
    }

    @Override
    public void saveList(List<BlackList> newBlackList) {
        /* Fetch existing blacklist */
        List<BlackList> existingBlackList = blackListRepository.findAll();

        List<String> newRefList = newBlackList.stream()
                .map(BlackList::getRef).collect(Collectors.toList());

        /* Remove existing blacklist if it is in new black-list */
        existingBlackList = existingBlackList.stream()
                .filter(blackList -> newRefList.contains(blackList.getRef()))
                .collect(Collectors.toList());

        blackListRepository.deleteAll(existingBlackList);
        blackListRepository.saveAll(newBlackList);
    }

    @Override
    public Page<BlackList> findAllBlackList(Pageable pageable) {
        return blackListRepository.findAll(pageable);
    }

    @Override
    public void removeById(Long id) {
        blackListRepository.deleteById(id);
    }

    @Override
    public boolean checkBlackListByRef(String ref) {
        return (!(blackListRepository.findBlackListByRef(ref)).isEmpty());
    }

}
