package com.sb.solutions.api.memo.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.api.memo.repository.MemoTypeRepository;
import com.sb.solutions.core.enums.Status;

public class MemoTypeServiceTest {

    @Mock
    private MemoTypeRepository repository;

    @InjectMocks
    private MemoTypeServiceImpl service;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatusGivenActiveReturnsActiveMemoType() {
        final MemoType demo = new MemoType();
        demo.setName("Demo Type");

        when(repository.findByStatus(Status.ACTIVE)).thenReturn(Arrays.asList(demo));

        final List<MemoType> types = service.findByStatus(Status.ACTIVE);

        assertThat(types, notNullValue());
        assertThat(types, hasSize(1));

    }

    @Test
    public void testFindByStatusGivenInactiveReturnsInactiveMemoType() {
        final MemoType demo = new MemoType();
        demo.setName("Demo Type");

        when(repository.findByStatus(Status.INACTIVE)).thenReturn(Arrays.asList(demo));

        final List<MemoType> types = service.findByStatus(Status.INACTIVE);

        assertThat(types, notNullValue());
        assertThat(types, hasSize(1));
    }

    @Test
    public void testSaveAllReturnsSavedMemoTypes() {
        final MemoType demo = new MemoType();
        demo.setName("Demo Type");

        final MemoType type = new MemoType();
        type.setName("Type 2");

        final List<MemoType> memoTypes = Arrays.asList(demo, type);

        final List<MemoType> expectedTypes = getMockSavedMemoTypes();

        when(repository.saveAll(memoTypes)).thenReturn(expectedTypes);

        final List<MemoType> savedMemoTypes = service.saveAll(memoTypes);

        assertThat(savedMemoTypes, hasSize(2));
    }

    private List<MemoType> getMockSavedMemoTypes() {
        final MemoType demo = new MemoType();
        demo.setName("Demo Type");
        demo.setStatus(Status.ACTIVE);
        demo.setVersion(1);

        final MemoType type = new MemoType();
        type.setName("Type 2");
        type.setStatus(Status.ACTIVE);
        type.setVersion(1);

        return Arrays.asList(demo, type);
    }
}