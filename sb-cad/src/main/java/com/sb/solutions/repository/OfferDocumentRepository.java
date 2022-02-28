package com.sb.solutions.repository;

import com.sb.solutions.core.repository.BaseRepository;
import com.sb.solutions.entity.OfferDocument;

public interface OfferDocumentRepository extends
        BaseRepository<OfferDocument, Long> {

    OfferDocument findByDocName(String docName);
}
