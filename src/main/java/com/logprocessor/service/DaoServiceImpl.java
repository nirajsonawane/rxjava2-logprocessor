package com.logprocessor.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.logprocessor.model.LogMessage;
/**
 * 
 * @author Niraj Sonawane
 * Service class for Database related work
 *
 */
@Component
public class DaoServiceImpl implements DaoService {
	private static final Logger log = LoggerFactory.getLogger(DaoServiceImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${sql.insert.report.table}")
	private String insertReportTableSql;
	
	@Override
	public void insertBatch(List<LogMessage> batchMessage) {
		log.info("Inserting data Batch Size {} ", batchMessage.size());	
		try {
			 
			jdbcTemplate.batchUpdate(insertReportTableSql, new BatchPreparedStatementSetter() {				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					LogMessage message = batchMessage.get(i);
					ps.setString(1, message.getId());
					ps.setString(2, message.getType());
					ps.setString(3, message.getHost());
					ps.setLong(4, message.getEventDuration());
					ps.setBoolean(5, message.getAlert());
				}
				@Override
				public int getBatchSize() {
					return batchMessage.size();
				}
			});
		} catch (Exception e) {
			log.error("Error While Inserting ",e);
		}
	}
}
