package com.rei.stats;

import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.h2.Driver;

public class H2UsageStore implements UsageStore {

    private static final BeanListHandler<Usage> BEAN_HANDLER = new BeanListHandler<Usage>(Usage.class);
    private DataSource ds;

    public H2UsageStore(Path homeDir) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(Driver.class.getName());
        ds.setUrl("jdbc:h2:" + homeDir.resolve("data").toAbsolutePath().toString());
        ds.setUsername("sa");
        ds.setPassword("sa");
        ds.setMaxTotal(20);
        this.ds = ds;
    }

    @Override
    public void init() {
        try {
            new QueryRunner(ds).update("create table usages(category varchar(255), key varchar(512), timestamp bigint)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void recordUsage(Usage usage) {
        QueryRunner queryRunner = new QueryRunner(ds);
        try {
            queryRunner.update("insert into usages(category, key, timestamp) values (?, ?, ?)", 
                    usage.getCategory(), usage.getKey(), usage.getTimestamp());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public Collection<Usage> findUsages(int limit) {
        QueryRunner queryRunner = new QueryRunner(ds);
        try {
            return queryRunner.query("select * from usages limit ?", BEAN_HANDLER, limit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Collection<Usage> findUsages(String category, int limit) {
        QueryRunner queryRunner = new QueryRunner(ds);
        try {
            return queryRunner.query("select * from usages where category = ? limit ?", BEAN_HANDLER, category, limit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Usage> findUsages(String category, Instant cutoff, int limit) {
        QueryRunner queryRunner = new QueryRunner(ds);
        try {
            return queryRunner.query("select * from usages where category = ? and timestamp >= ? limit ?", BEAN_HANDLER, category, 
                    cutoff.toEpochMilli(), limit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}