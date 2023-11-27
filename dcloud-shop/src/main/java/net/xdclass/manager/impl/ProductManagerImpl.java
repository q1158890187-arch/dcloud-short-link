package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.ProductManager;
import net.xdclass.mapper.ProductMapper;
import net.xdclass.model.ProductDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<ProductDO> list() {
        return productMapper.selectList(null);
    }

    @Override
    public ProductDO findDetailById(long productId) {
        return productMapper.selectOne(new LambdaQueryWrapper<ProductDO>().eq(ProductDO::getId, productId));
    }
}