package com.scio.cloud.jpa.config;

import java.sql.Types;

import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.stereotype.Component;
/**
 * build type datetime(3)
 *
 * @author Wang.ch
 * @date 2019-04-15 10:23:13
 */
@Component
public class MySQL5PlusDialect extends MySQL5Dialect {
  public MySQL5PlusDialect() {
    super();
    registerColumnType(Types.TIMESTAMP, 6, "datetime($l)");
  }
}
