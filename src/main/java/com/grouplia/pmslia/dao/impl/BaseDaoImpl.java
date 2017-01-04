package com.grouplia.pmslia.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class BaseDaoImpl {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String msg(String pattern, Object... arguments) {
		return String.format(pattern, arguments);
	}

	@Resource
	private JdbcTemplate jdbcTemplate;

	private Long currentId = System.currentTimeMillis();

	protected Long getNextId() {
		return currentId++;
	}

	protected int insert(String sql, Object[] args, int[] argTypes) {
		int result = jdbcTemplate.update(sql, args, argTypes);
		return result;
	}

	protected int update(String sql, Object[] args, int[] argTypes) {
		int result = jdbcTemplate.update(sql, args, argTypes);
		return result;
	}

	protected int delete(String sql, Object[] args, int[] argTypes) {
		int result = jdbcTemplate.update(sql, args, argTypes);
		return result;
	}

	protected <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType) {
		try {
			return jdbcTemplate.queryForObject(sql, args, argTypes, requiredType);
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	protected <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
		try {
			return jdbcTemplate.queryForObject(sql, args, argTypes, rowMapper);
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	protected <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
			throws DataAccessException {
		return jdbcTemplate.query(sql, args, argTypes, rowMapper);
	}

}
