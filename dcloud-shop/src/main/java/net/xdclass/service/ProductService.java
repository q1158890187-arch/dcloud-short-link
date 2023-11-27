package net.xdclass.service;

import net.xdclass.vo.ProductVO;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/28 00:31
 */
public interface ProductService {
    List<ProductVO> list();

    ProductVO findDetailById(long productId);
}