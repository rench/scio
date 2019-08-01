package com.scio.cloud.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.scio.cloud.querydsl.config.ScioJPAQueryFactory;
import com.scio.cloud.querydsl.domain.QUserInfo;
import com.scio.cloud.querydsl.domain.UserInfo;
import com.scio.cloud.querydsl.repository.UserInfoJpaRepository;
import com.scio.cloud.querydsl.service.UserInfoService;
import com.scio.cloud.querydsl.web.vo.UserInfoVo;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserInfoJpaRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserInfoJpaRepository repository;

  @Autowired private UserInfoService service;

  @Test
  @Rollback
  public void testSave() throws Exception {
    this.entityManager.persist(new UserInfo("sboot", "1234"));
    Optional<UserInfo> user = this.repository.findByUsername("sboot");
    assertThat(user.isPresent()).isTrue();
    assertThat(user.get().getUsername()).isEqualTo("sboot");
  }

  @Test
  @Rollback
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public void testFindAll() throws Exception {
    ScioJPAQueryFactory dsl = new ScioJPAQueryFactory(entityManager.getEntityManager());
    testSave();
    List<UserInfoVo> list = service.findAll();
    assertThat(list).isNotEmpty();
    assertThat(list.get(0).getUsername()).isEqualTo("sboot");
    UserInfoVo vo = list.get(0);
    vo.setPassword("scio@1234");
    service.save(vo);
    // why not flush the entity,this section query for userInfo ??
    //    long affectedRows =
    //        dsl.update(QUserInfo.userInfo)
    //            .set(QUserInfo.userInfo.password, "scio@1234")
    //            .where(QUserInfo.userInfo.id.eq(list.get(0).getId()))
    //            .execute();
    //    assertThat(affectedRows).isEqualByComparingTo(1L);
    boolean useIdQuery = true;
    UserInfo userinfo =
        dsl.selectFrom(QUserInfo.userInfo)
            .whereIfTrue(useIdQuery, QUserInfo.userInfo.id.eq(list.get(0).getId()))
            .fetchOne();
    assertThat(userinfo.getPassword()).isEqualTo("scio@1234");
    list = service.findAll();
    assertThat(list).isNotEmpty();
    assertThat(list.get(0).getPassword()).isEqualTo("scio@1234");
  }
}
