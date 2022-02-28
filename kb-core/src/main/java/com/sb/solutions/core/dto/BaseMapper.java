package com.sb.solutions.core.dto;

import java.util.List;

public abstract class BaseMapper<S, D> {

    public static final String SPRING_MODEL = "spring";

    public abstract D mapEntityToDto(S s);

    public abstract S mapDtoToEntity(D d);

    public abstract List<D> mapEntitiesToDtos(List<S> s);

    public abstract List<S> mapDtosToEntities(List<D> d);
}
