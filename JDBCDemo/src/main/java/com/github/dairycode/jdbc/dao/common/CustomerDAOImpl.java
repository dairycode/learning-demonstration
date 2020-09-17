package com.github.dairycode.jdbc.dao.common;

import com.github.dairycode.jdbc.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * DAO: data(base) access object
 * 封装了针对于数据表的通用的操作
 */
public class CustomerDAOImpl extends BaseDAO implements CustomerDAO {

    @Override
    public void insert(Connection conn, Customer cust) {
        String sql = "insert into customer(name,email,birth)values(?,?,?)";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customer where id=?";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, Customer cust) {
        String sql = "update customer set name=?,email=?,birth=? where id=?";
        update(conn, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql = "select id,name,email,birth from customer where id=?";
        Customer customer = getInstance(conn, Customer.class, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql = "select id,name,email,birth from customer";
        List<Customer> list = getForList(conn, Customer.class, sql);
        return list;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customer";
        return getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customer";
        return getValue(conn, sql);
    }
}
