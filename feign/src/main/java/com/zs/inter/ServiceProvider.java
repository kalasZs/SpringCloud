package com.zs.inter;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("serviceProvider")
public interface ServiceProvider {

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    Integer add(@RequestParam(value = "a")Integer a,
                @RequestParam(value = "b")Integer b);
}
