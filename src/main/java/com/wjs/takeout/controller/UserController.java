package com.wjs.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjs.takeout.common.Result;
import com.wjs.takeout.entity.User;
import com.wjs.takeout.service.UserService;
import com.wjs.takeout.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wjs
 * @createTime 2022-11-15 15:44
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * @param user 接收电话号码
     * @return 返回值
     */
    @RequestMapping("/code")
    public Result<String> codeView(User user){
        String code=null;
        if(StringUtils.isNotBlank(user.getPhone())){
            //发送短信验证码给用户
            code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("短信验证码是:{}",code);
            //SMSUtils.sendPhone(user.getPhone(),code);
            redisTemplate.opsForValue().set(user.getPhone(), code, 5, TimeUnit.MINUTES);
        }
        return Result.success(code);
    }
    /**
     * app登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpServletRequest request){
        //            获取用户信息
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        if(codeInSession!=null&&codeInSession.equals(code)){
        //    登陆成功,判断是否为新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User one = userService.getOne(queryWrapper);
            if(StringUtils.isEmpty(one.getPhone())){
            //    新用户存入到数据库中
                User user = new User();
                user.setPhone(phone);
                userService.save(user);
                request.getSession().setAttribute("user",user.getId());
             return Result.success(one);
            }
            request.getSession().setAttribute("user",one.getId());
            return Result.success(one);
          }
        return Result.error("登陆失败");
    }
    /**
     * 退出登录
     */
    @PostMapping("/loginout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return Result.success("退出登陆成功");
    }
}
