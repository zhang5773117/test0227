package com.zb.feign;

import com.zb.entity.XcUser;
import com.zb.entity.XcUserExt;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "USERSERVICE")
public interface XcUserFeginClient {
    @PostMapping(value = "/getUserLogin/{username}")
    public XcUserExt getUserLogin(@PathVariable("username") String username);

    @GetMapping(value = "/currentUser/{token}")
    public XcUser getCurrentUser(@PathVariable("token") String token);
}
