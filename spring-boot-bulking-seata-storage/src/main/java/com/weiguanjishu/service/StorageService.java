package com.weiguanjishu.service;

import com.weiguanjishu.entity.Storage;
import com.weiguanjishu.mapper.StorageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StorageService {

    @Resource
    private StorageMapper storageMapper;


    public void deduct(String commodityCode, int count) {
        Storage storage = storageMapper.findByCommodityCode(commodityCode);
        storage.setCount(storage.getCount() - count);
        storageMapper.updateById(storage);
    }


}
