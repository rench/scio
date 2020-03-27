package com.scio.cloud.shardingjdbc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scio.cloud.shardingjdbc.domain.EAVValuePo;

/**
 * @author Wang.ch
 * @date 2019-05-05 10:07:45
 */
@Repository
public interface EAVValueJpaRepository extends JpaRepository<EAVValuePo, Long> {

    List<EAVValuePo> findByEntityId(Long entityId);
}
