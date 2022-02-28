package com.sb.solutions.api.forms.service;

import com.sb.solutions.api.forms.entity.Forms;
import com.sb.solutions.api.forms.repository.FormsRepository;
import com.sb.solutions.api.forms.repository.specification.FormsSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author : Rujan Maharjan on  11/18/2020
 **/
@Service
public class FormsServiceImpl extends BaseServiceImpl<Forms, Long> implements FormsService {

    private final FormsRepository formsRepository;

    protected FormsServiceImpl(FormsRepository formsRepository) {
        super(formsRepository);
        this.formsRepository = formsRepository;
    }

    @Override
    protected BaseSpecBuilder<Forms> getSpec(Map filterParams) {
        filterParams.values().removeIf(Objects::isNull);
        filterParams.values().removeIf(value -> value.equals("null") || value.equals("undefined"));
        return new FormsSpecBuilder(filterParams);
    }

    @Override
    public Forms save(Forms forms) {
        return formsRepository.save(forms);
    }

    @Override
    public Optional<Forms> findOne(Long aLong) {
        return formsRepository.findById(aLong);
    }

    @Override
    public void delete(Forms entity) {

    }

    @Override
    public void deleteById(Long aLong) {
        formsRepository.deleteById(aLong);
    }
}
