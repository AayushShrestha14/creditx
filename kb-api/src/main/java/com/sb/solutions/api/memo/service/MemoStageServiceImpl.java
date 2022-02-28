package com.sb.solutions.api.memo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.memo.entity.MemoStage;
import com.sb.solutions.api.memo.repository.MemoStageRepository;

@Service
public class MemoStageServiceImpl implements MemoStageService {

    private final MemoStageRepository repository;

    public MemoStageServiceImpl(@Autowired MemoStageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MemoStage> findAll() {
        return null;
    }

    @Override
    public MemoStage findOne(Long id) {
        return null;
    }

    @Override
    public MemoStage save(MemoStage memoStage) {
        return repository.save(memoStage);
    }

    @Override
    public Page<MemoStage> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<MemoStage> saveAll(List<MemoStage> list) {
        return repository.saveAll(list);
    }
}
