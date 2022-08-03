package com.echoes.service.impl;

import com.echoes.entity.User;
import com.echoes.mapper.UserMapper;
import com.echoes.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
