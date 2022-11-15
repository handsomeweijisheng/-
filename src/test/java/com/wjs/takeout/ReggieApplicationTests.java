package com.wjs.takeout;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjs.takeout.entity.User;
import com.wjs.takeout.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ReggieApplicationTests {
    @Autowired
private UserService userService;
    @Test
    void contextLoads() {
        //User user = new User();
        //user.setPhone("18836079860");
        List<User> list = userService.list();
        List<User> collect = list.stream()
                .map(user -> {
                    User user1 = new User();
                    user1.setPhone(user.getPhone());
                    return user1;
                })
                .collect(Collectors.toList());
        collect.stream().forEach(user -> {
            System.out.println("user = " + user);
        });
        System.out.println(StringUtils.isEmpty(collect));
    }

}
