package com.sb.solutions.core.revision;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RevisionRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(RevisionRepository.class);

    @PersistenceContext
    private EntityManager manager;

    public List<BaseRevisionEntity<T>> getRevisions(Long id) {

        final AuditReader reader = AuditReaderFactory.get(manager);

        final AuditQuery query = reader.createQuery().forRevisionsOfEntity(getClazz(), false, true)
            .add(AuditEntity.id().eq(id));

        final List<?> results = query.getResultList();

        results.forEach(r -> log.info("{}", r));

        return results.stream()
            .filter(r -> r instanceof Object[])
            .map(r -> (Object[]) r)
            .map(r -> {

                T entity = r[0] != null ? (T) r[0] : null;
                DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) r[1];
                RevisionType revisionType = (RevisionType) r[2];

                return new BaseRevisionEntity<>(
                    entity,
                    revisionEntity.getRevisionDate(),
                    revisionType);
            }).collect(Collectors.toList());
    }

    protected abstract Class<T> getClazz();
}
