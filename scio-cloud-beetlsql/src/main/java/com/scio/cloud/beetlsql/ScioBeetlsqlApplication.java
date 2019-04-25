package com.scio.cloud.beetlsql;

import javax.sql.DataSource;

import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.spring4.BeetlSqlDataSource;
import org.beetl.sql.ext.spring4.BeetlSqlScannerConfigurer;
import org.beetl.sql.ext.spring4.SqlManagerFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
/**
 * scio-cloud-beetlsql
 *
 * @doc http://ibeetl.com/guide/#beetlsql
 * @doc 2.1.2 https://docs.spring.io/spring-data/jpa/docs/2.1.2.RELEASE/reference/html/
 * @doc http://ibeetl.com
 * @author Wang.ch
 * @date 2019-4-25 09:38:29
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.scio.cloud.beetlsql")
public class ScioBeetlsqlApplication {

  public static void main(String[] args) {
    SpringApplication.run(ScioBeetlsqlApplication.class, args);
  }

  @Bean(name = "beetlSqlScannerConfigurer")
  public BeetlSqlScannerConfigurer getBeetlSqlScannerConfigurer() {
    BeetlSqlScannerConfigurer conf = new BeetlSqlScannerConfigurer();
    conf.setBasePackage("com.scio.cloud.beetlsql.mapper");
    conf.setDaoSuffix("Mapper");
    conf.setSqlManagerFactoryBeanName("sqlManagerFactoryBean");
    return conf;
  }
  // @Qualifier("datasource")
  @Bean(name = "sqlManagerFactoryBean")
  @Primary
  public SqlManagerFactoryBean getSqlManagerFactoryBean(DataSource datasource) {
    SqlManagerFactoryBean factory = new SqlManagerFactoryBean();
    BeetlSqlDataSource source = new BeetlSqlDataSource();
    source.setMasterSource(datasource);

    factory.setCs(source);
    factory.setDbStyle(new MySqlStyle());
    factory.setInterceptors(new Interceptor[] {new DebugInterceptor()});
    factory.setNc(new UnderlinedNameConversion()); // 开启驼峰
    factory.setSqlLoader(new ClasspathLoader("/sql")); // sql文件路径
    return factory;
  }
}
