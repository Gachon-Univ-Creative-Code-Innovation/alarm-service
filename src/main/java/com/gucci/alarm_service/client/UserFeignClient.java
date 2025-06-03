package com.gucci.alarm_service.client;

import com.gucci.alarm_service.config.FeignConfig;
import com.gucci.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "user-service",
        url = "${user-service.url}",
        configuration = FeignConfig.class) // 추후 포트 번호 변경 필요
public interface UserFeignClient {

    @GetMapping("/follow/followers/{userId}")
    ApiResponse<List<Long>> getFollowers(@PathVariable Long userId);

}
