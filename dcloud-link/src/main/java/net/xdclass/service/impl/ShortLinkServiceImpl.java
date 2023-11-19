package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.model.ShortLinkDO;
import net.xdclass.service.ShortLinkService;
import net.xdclass.vo.ShortLinkVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/19 15:09
 */
@Service
@Slf4j
public class ShortLinkServiceImpl implements ShortLinkService {

    @Autowired
    private ShortLinkManager shortLinkManager;


    @Override
    public ShortLinkVO parseShortLinkCode(String shortLinkCode) {

        ShortLinkDO shortLinkDO = shortLinkManager.findByShortLinCode(shortLinkCode);
        if(shortLinkDO == null){
            return null;
        }
        ShortLinkVO shortLinkVO = new ShortLinkVO();
        BeanUtils.copyProperties(shortLinkDO,shortLinkVO);
        return shortLinkVO;
    }
}
