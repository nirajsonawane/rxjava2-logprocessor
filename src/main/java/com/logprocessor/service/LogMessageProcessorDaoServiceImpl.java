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

import com.logprocessor.model.BadRecordReport;
import com.logprocessor.model.LogMessage;

@Component
public class LogMessageProcessorDaoServiceImpl implements LogMessageProcessorDaoService {
	private static final Logger log = LoggerFactory.getLogger(LogMessageProcessorDaoServiceImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${sql.insert.stared.table}")
	private String insertStartedsql;

	@Value("${sql.insert.finished.table}")
	private String logMessagesFinished;

	@Value("${sql.report.table}")
	private String reportSql;
	@Value("${event.alert.threshold}")
	private String threshold;

	@Override
	public void insertBadRecord(BadRecordReport badRecord) {

	}

	@Override
	public void insertError(Throwable t) {

		log.info("Inserting Error Data {} ", t.getMessage());
		String errorMessage = t.getMessage();
		if (errorMessage.length() > 100) {
			errorMessage = errorMessage.substring(0, 100);
		}
		String sql = "INSERT INTO BAD_RECORD_REPORT (LINE_NUMBER,ERROR_MESSAGE) VALUES(?,?)";
		jdbcTemplate.update(sql, new Object[] { null, errorMessage });
	}

	private void insertBatch(List<LogMessage> batchMessage, String sql) {

		log.info("Inserting data Batch Size {} ", batchMessage.size());

		try {
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					LogMessage message = batchMessage.get(i);
					ps.setString(1, message.getId());
					ps.setString(2, message.getType());
					ps.setString(3, message.getHost());
					ps.setString(4, message.getTimestamp());
				}

				@Override
				public int getBatchSize() {
					return batchMessage.size();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void insertBatchStaredMessage(List<LogMessage> batchMessage) {

		insertBatch(batchMessage, insertStartedsql);
	}

	@Override
	public void insertFinishedMessage(List<LogMessage> batchMessage) {
		insertBatch(batchMessage, logMessagesFinished);
	}

	@Override
	public void populateReport() {

		log.info("Runing Report");
		/*
		 * String sql =
		 * "CREATE TABLE LONG_RUNING_MESSAGE (ID, EVENT_TYPE,HOST,EVENT_DURATION,ALERT) AS (SELECT \r\n"
		 * + "	 A.ID AS ID, \r\n" + "	 A.EVENT_TYPE AS EVENT_TYPE ,\r\n" +
		 * "	 A.HOST  AS HOST,\r\n" +
		 * "	 (B.EVENT_TIME_STAMP - A.EVENT_TIME_STAMP) AS EVENT_DURATION ,\r\n" +
		 * "	 CASE WHEN Abs( B.EVENT_TIME_STAMP - A.EVENT_TIME_STAMP) >= ?  \r\n" +
		 * "          THEN 'TRUE' ELSE 'FALSE' \r\n" + "       END AS ALERT\r\n" +
		 * "	 FROM \"PUBLIC\".\"LOG_MESSAGES_STARTED\" A ,\"PUBLIC\".\"LOG_MESSAGES_FINISHED\" B  WHERE A.ID=B.ID ) WITH DATA"
		 * ;
		 */

		log.debug("Populating Report SQL  {}", reportSql);
		jdbcTemplate.execute(reportSql.replace("?", threshold));
		log.info("Batch Comlited");

		/*
		 * jdbcTemplate.execute(sql,new PreparedStatementCallback<Boolean>(){
		 * 
		 * @Override public Boolean doInPreparedStatement(PreparedStatement ps) throws
		 * SQLException { ps.setString(1,"4"); return ps.execute(); } });
		 */

	}

}
