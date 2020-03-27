package com.scio.cloud.shardingjdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.scio.cloud.shardingjdbc.domain.EAVEntityPo;
import com.scio.cloud.shardingjdbc.domain.EAVValuePo;
import com.scio.cloud.shardingjdbc.repository.EAVEntityJpaRepository;
import com.scio.cloud.shardingjdbc.repository.EAVValueJpaRepository;
import com.scio.cloud.shardingjdbc.util.DataSourceUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EAVValueJpaRepository value;

    @Autowired
    EAVEntityJpaRepository entity;
    
    

    // JdbcTemplate a = new JdbcTemplate(DataSourceUtil.createDataSource("loanapp"));

    JdbcTemplate b = new JdbcTemplate(DataSourceUtil.createDataSource("loanappnew"));

    @Transactional
    @Rollback(value = true)
    @Test
    public void testRollback() {
        //a.execute(
        //    "INSERT INTO `eav_entity`(`loan_app_code`, `entity_code`, `category_code`, `category_type`, `parent_id`, `deleted`) VALUES ('LA6720200201150222843910003', 'EN6720200201150222843910003', 'HOUSE_INFO', 'HOUSING_INFO', NULL, 0);");
        try {
            jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
             e.printStackTrace();
        }
        String loanAppCode = "LA6720200201150222843910003";
        EAVEntityPo po = new EAVEntityPo();
        po.setId(202L);
        po.setLoanAppCode(loanAppCode);
        po.setCategoryCode("HOUSE_INFO");
        po.setCategoryType("HOUSING_INFO");
        po.setEntityCode("EN" + loanAppCode.substring(2));
        po = entity.save(po);
        System.out.println(po.getId());
        
        jdbcTemplate.execute(
            "INSERT INTO `eav_entity`(`loan_app_code`, `entity_code`, `category_code`, `category_type`, `parent_id`, `deleted`) VALUES ('LN6720200201150222843910003', 'EN6720200201150222843910003', 'HOUSE_INFO', 'HOUSING_INFO', NULL, 0);");
        int i=1/0;
    }

    @Transactional
    @Rollback(value = false)
    @Test
    public void testInsert() {
        String loanAppCode = "LA6720200201150222843910003";
        EAVEntityPo po = new EAVEntityPo();
        po.setId(202L);
        po.setLoanAppCode(loanAppCode);
        po.setCategoryCode("HOUSE_INFO");
        po.setCategoryType("HOUSING_INFO");
        po.setEntityCode("EN" + loanAppCode.substring(2));
        po = entity.save(po);
        System.out.println(po.getId());

        try {
            loanAppCode = "LA6720190411150222843910003";
            po = new EAVEntityPo();
            po.setId(203L);
            po.setLoanAppCode(loanAppCode);
            po.setCategoryCode(null);
            po.setCategoryType("HOUSING_INFO");
            po.setEntityCode("EN" + loanAppCode.substring(2));
            // po = entity.save(po);
            // System.out.println(po.getId());
            jdbcTemplate.execute("update eav_entity set deleted=1 where loan_app_code='LA6720190411150222843910003'");

        } catch (Exception e) {
            log.error("error {}", e.getMessage());
        }
        int i = 1 / 0;
        System.out.println("end is ok");
    }

    @Transactional
    @Test
    public void testSelect() {
        String loanAppCode = "LA6720200201150222843910003";
        List<Map<String, Object>> list =
            jdbcTemplate.queryForList("select * from eav_entity where loan_app_code = ? ", loanAppCode);
        System.out.println(list.size());

        try (HintManager hintManger = HintManager.getInstance()) {
            hintManger.addDatabaseShardingValue("eav_value", loanAppCode);
            hintManger.addTableShardingValue("eav_value", loanAppCode);
            List<EAVValuePo> values = value.findByEntityId(1L);
            System.out.println(values);
        }

        loanAppCode = "LA6720200131150222843910003";
        list = jdbcTemplate.queryForList("select * from eav_entity where loan_app_code = ? ", loanAppCode);
        System.out.println(list.size());
        try (HintManager hintManger = HintManager.getInstance()) {
            hintManger.addDatabaseShardingValue("eav_value", loanAppCode);
            hintManger.addTableShardingValue("eav_value", loanAppCode);
            List<EAVValuePo> values = value.findByEntityId(2L);
            System.out.println(values);
        }

        loanAppCode = "LA6720190411150222843910003";
        list = jdbcTemplate.queryForList("select * from eav_entity where loan_app_code = ? ", loanAppCode);
        System.out.println(list.size());
        try (HintManager hintManger = HintManager.getInstance()) {
            hintManger.addDatabaseShardingValue("eav_value", loanAppCode);
            hintManger.addTableShardingValue("eav_value", loanAppCode);
            List<EAVValuePo> values = value.findByEntityId(2L);
            System.out.println(values);
        }
    }
}
