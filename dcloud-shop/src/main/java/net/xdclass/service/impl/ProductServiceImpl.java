package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.manager.ProductManager;
import net.xdclass.model.ProductDO;
import net.xdclass.service.ProductService;
import net.xdclass.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/28 00:32
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductManager productManager;

    @Override
    public List<ProductVO> list() {

        List<ProductDO> list = productManager.list();

        List<ProductVO> collect = list.stream().map( obj -> beanProcess(obj) ).collect(Collectors.toList());


        return collect;
    }

    @Override
    public ProductVO findDetailById(long productId) {

        ProductDO productDO = productManager.findDetailById(productId);

        ProductVO productVO = beanProcess(productDO);

        return productVO;
    }


    private ProductVO beanProcess(ProductDO productDO) {

        ProductVO productVO = new ProductVO();

        BeanUtils.copyProperties(productDO, productVO);
        return productVO;
    }
}