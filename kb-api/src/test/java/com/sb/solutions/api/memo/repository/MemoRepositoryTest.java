package com.sb.solutions.api.memo.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.sb.solutions.BaseJpaTest;
import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.api.memo.entity.MemoStage;
import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.repository.UserRepository;

public class MemoRepositoryTest extends BaseJpaTest {

    @Autowired
    private MemoTypeRepository memoTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Test
    @DatabaseSetup("/dataset/memo/memo-config.xml")
    public void testSaveShouldSaveMemo() {

        final User sender = userRepository.getOne(1L);
        final User receiver = userRepository.getOne(2L);
        final MemoType memoType = memoTypeRepository.getOne(1L);

        final Memo memo = new Memo();
        memo.setSubject("Test Memo");
        memo.setContent("Test Content for Memo");
        memo.setSentTo(receiver);
        memo.setSentBy(sender);
        memo.setType(memoType);

        final MemoStage stage = new MemoStage();
        stage.setMemo(memo);
        stage.setNote(sender.getName() + " created Draft");
        stage.setSentBy(sender);

        final Memo savedMemo = memoRepository.save(memo);

        assertThat(savedMemo.getId(), equalTo(1L));
    }
}
