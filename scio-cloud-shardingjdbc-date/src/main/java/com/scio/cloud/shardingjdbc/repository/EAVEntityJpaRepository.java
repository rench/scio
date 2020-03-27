package com.scio.cloud.shardingjdbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scio.cloud.shardingjdbc.domain.EAVEntityPo;

/**
 * @author Wang.ch
 * @date 2019-05-05 10:07:45
 */
@Repository
public interface EAVEntityJpaRepository extends JpaRepository<EAVEntityPo, Long> {

}
