package com.sb.solutions.web.memo.v1;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.api.memo.repository.MemoTypeRevisionRepository;
import com.sb.solutions.core.revision.RevisionRepository;

@RestController
@RequestMapping("/v1/memos/types")
public class MemoTypeRevisionController {

    private final RevisionRepository<MemoType> revisionRepository;

    public MemoTypeRevisionController(
        MemoTypeRevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    @GetMapping("/{memoTypeId}/revisions")
    public List getRevisions(@PathVariable("memoTypeId") Long memoTypeId) {
        return this.revisionRepository.getRevisions(memoTypeId);
    }

}
