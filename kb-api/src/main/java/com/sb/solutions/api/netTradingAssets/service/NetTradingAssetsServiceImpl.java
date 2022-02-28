package com.sb.solutions.api.netTradingAssets.service;

import com.sb.solutions.api.netTradingAssets.entity.NetTradingAssets;
import com.sb.solutions.api.netTradingAssets.repository.NetTradingAssetsRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Amulya Shrestha on 10/15/2020
 **/

@Service
public class NetTradingAssetsServiceImpl extends
        BaseServiceImpl<NetTradingAssets, Long> implements NetTradingAssetsService {

    private NetTradingAssetsRepository netTradingAssetsRepository;

    protected NetTradingAssetsServiceImpl(
            NetTradingAssetsRepository repo) {
        super(repo);
    }

    @Override
    protected BaseSpecBuilder<NetTradingAssets> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
