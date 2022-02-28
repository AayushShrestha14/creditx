package com.sb.solutions.api.memo.service;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.api.memo.entity.MemoStage;
import com.sb.solutions.api.memo.enums.Stage;
import com.sb.solutions.api.memo.repository.MemoRepository;
import com.sb.solutions.api.memo.repository.specification.MemoSpecBuilder;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

@Service
public class MemoServiceImpl extends BaseServiceImpl<Memo, Long> implements MemoService {

    private final MemoRepository repository;

    public MemoServiceImpl(@Autowired MemoRepository repository) {
        super(repository);

        this.repository = repository;
    }

    @Override
    public Memo save(Memo memo) {

        if (memo.getStage() == Stage.DRAFT && CollectionUtils.isEmpty(memo.getStages())) {
            final MemoStage memoStage = new MemoStage();
            memoStage.setSentBy(memo.getSentBy());
            memoStage.setSentTo(memo.getSentTo());
            memoStage.setStage(Stage.DRAFT);
            memoStage.setNote("Saved as Draft");

            memo.addMemoStage(memoStage);
        } else {
            for (MemoStage stage : memo.getStages()) {
                memo.addMemoStage(stage);
            }
        }

        return repository.save(memo);
    }

    @Override
    protected BaseSpecBuilder<Memo> getSpec(Map<String, String> filterParams) {
        return new MemoSpecBuilder(filterParams);
    }

    @Override
    public void delete(Memo memo) {
        memo.setStatus(Status.DELETED);
        repository.save(memo);
    }

    @Override
    public void deleteById(Long id) {
        final Memo memo = repository.getOne(id);
        memo.setStatus(Status.DELETED);

        repository.save(memo);
    }
}
