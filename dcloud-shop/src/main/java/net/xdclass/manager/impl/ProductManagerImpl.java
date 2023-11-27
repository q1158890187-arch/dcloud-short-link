package net.xdclass.manager.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.ProductManager;
import net.xdclass.mapper.ProductMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/28 00:33
 */
@Component
@Slf4j
public class ProductManagerImpl implements ProductManager {

    @Resource
    private ProductMapper productMapper;
}