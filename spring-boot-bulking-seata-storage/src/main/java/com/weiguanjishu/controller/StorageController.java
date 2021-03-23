package com.weiguanjishu.controller;

import com.weiguanjishu.entity.Storage;
import com.weiguanjishu.service.StorageService;
import io.seata.core.context.RootContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RequestMapping("/api/storage")
@RestController
public class StorageController {


    @Autowired
    private StorageService storageService;

    /**
     * http://127.0.0.1:8083/api/storage/deduct?commodityCode=6666&count=1
     */
    @RequestMapping("/deduct")
    public Object deduct(@RequestParam String commodityCode, @RequestParam Integer count) throws SQLException {
        System.out.println("storage XID： " + RootContext.getXID());
        storageService.deduct(commodityCode, count);
        return "Storage调用成功，count=" + count;
    }

}