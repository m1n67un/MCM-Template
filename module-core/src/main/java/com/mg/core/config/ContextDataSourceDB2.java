package com.mg.core.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;

/**
 * 공통 데이터베이스 설정
 * mySql
 */
@Configuration
@MapperScan(sqlSessionFactoryRef = "db2SqlSessionFactory", basePackages = { "com.mg.core.mapper.db2" })
@RequiredArgsConstructor
public class ContextDataSourceDB2 extends CommonDatabaseConfig {

    @Primary
    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource-db2")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "db2SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource2") DataSource dataSource,
            ApplicationContext context) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        setConfigureSqlSessionFactory(sqlSessionFactoryBean, dataSource, context, "classpath*:sqlmap/db2/**/*.xml");
        return sqlSessionFactoryBean.getObject();
    }
}
