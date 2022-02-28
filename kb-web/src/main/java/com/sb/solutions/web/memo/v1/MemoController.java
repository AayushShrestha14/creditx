package com.sb.solutions.web.memo.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.api.memo.service.MemoService;
import com.sb.solutions.core.controller.BaseController;
import com.sb.solutions.web.memo.v1.dto.MemoDto;
import com.sb.solutions.web.memo.v1.mapper.MemoMapper;

@RestController
@RequestMapping(MemoController.URL)
public class MemoController extends BaseController<Memo, MemoDto, Long> {

    static final String URL = "/v1/memos";

    private static final Logger logger = LoggerFactory.getLogger(MemoController.class);

    public MemoController(@Autowired MemoService service,
        @Autowired MemoMapper mapper) {
        super(service, mapper);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
