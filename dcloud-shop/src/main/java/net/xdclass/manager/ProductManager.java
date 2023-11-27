package net.xdclass.manager;

import net.xdclass.model.ProductDO;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/28 00:32
 */
public interface ProductManager {
    List<ProductDO> list();

    ProductDO findDetailById(long productId);
}