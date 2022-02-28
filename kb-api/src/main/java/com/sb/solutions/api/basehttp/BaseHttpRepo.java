package com.sb.solutions.api.basehttp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by bishallama on 4/26/2018.
 */
public interface BaseHttpRepo extends JpaRepository<BaseHttp, Integer> {

    @Query(value = "select base_url from base_http where flag = 1", nativeQuery = true)
    String baseUrl();

    @Query(value = "select base_url from base_http where flag = 2", nativeQuery = true)
    String url();
}
