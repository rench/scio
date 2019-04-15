package com.scio.cloud.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.scio.cloud.jpa.domain.UserInfo;
import com.scio.cloud.jpa.repository.UserInfoJpaRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserInfoJpaRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserInfoJpaRepository repository;

  @Test
  public void testSave() throws Exception {
    this.entityManager.persist(new UserInfo("sboot", "1234"));
    Optional<UserInfo> user = this.repository.findByUsername("sboot");
    assertThat(user.isPresent()).isTrue();
    assertThat(user.get().getUsername()).isEqualTo("sboot");
  }
}
