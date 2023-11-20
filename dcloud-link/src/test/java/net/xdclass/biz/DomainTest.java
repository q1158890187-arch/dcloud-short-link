package net.xdclass.biz;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.LinkApplication;
import net.xdclass.manage.DomainManager;
import net.xdclass.model.DomainDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/20 22:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LinkApplication.class)
@Slf4j
public class DomainTest {

    @Autowired
    private DomainManager domainManager;

    @Test
    public void testListDomain() {


        List<DomainDO> domainDOS = domainManager.listOfficialDomain();

        log.info(domainDOS.toString());

    }


}
