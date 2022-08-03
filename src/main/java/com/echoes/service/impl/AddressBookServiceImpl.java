package com.echoes.service.impl;

import com.echoes.entity.AddressBook;
import com.echoes.mapper.AddressBookMapper;
import com.echoes.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author Xiao ZhiPeng
 * @since 2022-07-30
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
