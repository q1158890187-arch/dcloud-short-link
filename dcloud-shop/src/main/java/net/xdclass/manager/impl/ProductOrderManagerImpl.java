package net.xdclass.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.mapper.ProductOrderMapper;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.vo.ProductOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/28 13:08
 */
@Component
public class ProductOrderManagerImpl implements ProductOrderManager {

    @Resource
    private ProductOrderMapper productOrderMapper;

    @Override
    public int add(ProductOrderDO productOrderDO) {
        return productOrderMapper.insert(productOrderDO);
    }

    @Override
    public ProductOrderDO findByOutTradeNoAndAccountNo(String outTradeNo, Long accountNo) {

        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new LambdaQueryWrapper<ProductOrderDO>()
                .eq(ProductOrderDO::getOutTradeNo, outTradeNo)
                .eq(ProductOrderDO::getAccountNo, accountNo)
                .eq(ProductOrderDO::getDel, 0));
        return productOrderDO;
    }

    @Override
    public int updateOrderPayState(String outTradeNo, Long accountNo, String newState, String oldState) {

        int rows = productOrderMapper.update(null, new LambdaUpdateWrapper<ProductOrderDO>()
                .eq(ProductOrderDO::getOutTradeNo, outTradeNo)
                .eq(ProductOrderDO::getAccountNo, accountNo)
                .eq(ProductOrderDO::getState, oldState)
                .set(ProductOrderDO::getState, newState));
        return rows;
    }

    @Override
    public Map<String, Object> page(int page, int size, Long accountNo, String state) {

        Page<ProductOrderDO> pageInfo = new Page<>(page, size);

        IPage<ProductOrderDO> orderDOIPage;

        if (StringUtils.isBlank(state)) {

            orderDOIPage = productOrderMapper.selectPage(pageInfo, new QueryWrapper<ProductOrderDO>().eq("account_no", accountNo));

        } else {

            orderDOIPage = productOrderMapper.selectPage(pageInfo, new QueryWrapper<ProductOrderDO>()
                    .eq("account_no", accountNo)
                    .eq("state", state)
                    .eq("del", 0));
        }

        List<ProductOrderDO> orderDOIPageRecords = orderDOIPage.getRecords();

        List<ProductOrderVO> productOrderVOList = orderDOIPageRecords.stream().map(obj -> {
            ProductOrderVO productOrderVO = new ProductOrderVO();
            BeanUtils.copyProperties(obj, productOrderVO);
            return productOrderVO;
        }).collect(Collectors.toList());

        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",orderDOIPage.getTotal());
        pageMap.put("total_page",orderDOIPage.getPages());
        pageMap.put("current_data",productOrderVOList);

        return pageMap;
    }

    @Override
    public int del(Long productOrderId, Long accountNo) {

        int rows = productOrderMapper.update(null, new LambdaUpdateWrapper<ProductOrderDO>()
                .eq(ProductOrderDO::getId, productOrderId)
                .eq(ProductOrderDO::getAccountNo, accountNo)
                .set(ProductOrderDO::getDel, 1));
        return rows;
    }
}