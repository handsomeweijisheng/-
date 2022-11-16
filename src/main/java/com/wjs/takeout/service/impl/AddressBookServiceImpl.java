package com.wjs.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjs.takeout.entity.AddressBook;
import com.wjs.takeout.mapper.AddressBookMapper;
import com.wjs.takeout.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author wjs
 * @createTime 2022-11-16 10:38
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
