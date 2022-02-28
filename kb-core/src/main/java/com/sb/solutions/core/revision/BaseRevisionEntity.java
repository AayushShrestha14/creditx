package com.sb.solutions.core.revision;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionType;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
class BaseRevisionEntity<T> {

    private T entity;
    private Date revisionDate;
    private RevisionType revisionType;
}
