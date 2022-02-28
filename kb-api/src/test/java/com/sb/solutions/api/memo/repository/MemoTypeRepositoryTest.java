package com.sb.solutions.api.memo.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.sb.solutions.BaseJpaTest;
import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.core.enums.Status;

@DatabaseSetup("/dataset/memo/memo-config.xml")
public class MemoTypeRepositoryTest extends BaseJpaTest {

    @Autowired
    private MemoTypeRepository repository;

    public MemoTypeRepositoryTest() {
    }

    @Test
    public void testFindAllShouldReturnTwoRecords() {
        List<MemoType> memoTypes = repository.findAll();

        assertThat(memoTypes, hasSize(3));
    }

    @Test
    public void testFindByIdShoudReturnSingleRecord() {
        MemoType memoType = repository.getOne(1L);

        assertThat(memoType, notNullValue());
        assertThat(memoType.getName(), equalTo("Demo Type"));
    }

    @Test
    public void testFindByStatusGivenActiveShouldReturnActiveMemoTypes() {
        List<MemoType> activeTypes = repository.findByStatus(Status.ACTIVE);

        assertThat(activeTypes, hasSize(2));
    }

    @Test
    public void testFindByStatusGivenInactiveShouldReturnInactiveMemoTypes() {
        List<MemoType> activeTypes = repository.findByStatus(Status.INACTIVE);

        assertThat(activeTypes, hasSize(1));
    }
}
