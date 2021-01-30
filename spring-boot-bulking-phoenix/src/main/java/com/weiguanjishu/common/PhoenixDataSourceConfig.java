package com.weiguanjishu.common;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.phoenix.queryserver.client.Driver;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = PhoenixDataSourceConfig.PACKAGE, sqlSessionTemplateRef = "hbasePhoenixSqlSessionTemplate")
public class PhoenixDataSourceConfig extends SimpleDriverDataSource {

    static final String PACKAGE = "com.weiguanjishu.domain.mapper";

    @Value("${spring.phoenix.datasource.url}")
    private String url;
    @Value("${spring.phoenix.datasource.driver-class-name}")
    private String driverClass;


    @Bean(name = "hbasePhoenixDataSource")
    public DataSource hbasePhoenixDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class)
                .driverClassName(Driver.class.getName())
//                .driverClassName("org.apache.phoenix.queryserver.client.Driver")
                .url("jdbc:phoenix:thin:url=" + url + ";serialization=PROTOBUF")
//                .url("jdbc:phoenix:thin:url=localhost:8765;serialization=JSON")
                .build();
    }


    @Bean(name = "hbasePhoenixTransactionManager")
    public DataSourceTransactionManager hbasePhoenixTransactionManager(@Qualifier("hbasePhoenixDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "hbasePhoenixSqlSessionFactory")
    public SqlSessionFactory hbasePhoenixSqlSessionFactory(@Qualifier("hbasePhoenixDataSource") DataSource hbasePhoenixDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hbasePhoenixDataSource);
        return sessionFactory.getObject();
    }

    @Bean("hbasePhoenixSqlSessionTemplate")
    public SqlSessionTemplate itemSqlSessionTemplate(@Qualifier("hbasePhoenixSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
