package com.mg.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 공통 데이터베이스 설정
 */
@Slf4j
public class CommonDatabaseConfig {

    @Autowired
    Environment environment;

    /**
     * Mybatis 정보 설정
     * 
     * @param sqlSessionFactoryBean SqlSessionFactoryBean
     * @param dataSource            DataSource
     * @throws IOException
     */
    protected void setConfigureSqlSessionFactory(SqlSessionFactoryBean sqlSessionFactoryBean, DataSource dataSource,
            ApplicationContext context, String mapperLocation) throws IOException {
        sqlSessionFactoryBean.setDataSource(dataSource);

        Configuration configuration = new Configuration();
        configuration.setCacheEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);

        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setMapperLocations(context.getResources(mapperLocation));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.mg");
    }

}
