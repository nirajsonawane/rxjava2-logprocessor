package com.logprocessor.rxjavalogprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.logprocessor.rxjavalogprocessor.model.BadRecordReport;
import com.logprocessor.rxjavalogprocessor.model.LogMessage;

@Component
public class LogMessageProcessorDaoServiceImpl implements LogMessageProcessorDaoService {
	private static final Logger log = LoggerFactory.getLogger(LogMessageProcessorDaoServiceImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void insertBadRecord(BadRecordReport badRecord) {

	}

	@Override
	public void insertStaredMessage(LogMessage message) {

		String sql = "INSERT INTO LOG_MESSAGES_STARTED (ID,EVENT_TYPE,HOST,EVENT_TIME_STAMP) VALUES (?, ?, ?,?)";
		jdbcTemplate.update(sql,
				new Object[] { message.getId(), message.getType(), message.getHost(), message.getTimestamp() });
	}

	@Override
	public void insertFinishedMessage(LogMessage message) {

		String sql = "INSERT INTO LOG_MESSAGES_FINISHED (ID,EVENT_TYPE,HOST,EVENT_TIME_STAMP) VALUES (?,? ,?,?)";
		jdbcTemplate.update(sql,
				new Object[] { message.getId(), message.getType(), message.getHost(), message.getTimestamp() });
	}

	@Override
	public void populateReport() {

		log.info("Runing Report");
		String sql = "CREATE TABLE LONG_RUNING_MESSAGE (ID, EVENT_TYPE,HOST,EVENT_DURATION,ALERT) AS (SELECT \r\n"
				+ "	 A.ID AS ID, \r\n" + "	 A.EVENT_TYPE AS EVENT_TYPE ,\r\n" + "	 A.HOST  AS HOST,\r\n"
				+ "	 (B.EVENT_TIME_STAMP - A.EVENT_TIME_STAMP) AS EVENT_DURATION ,\r\n"
				+ "	 CASE WHEN Abs( B.EVENT_TIME_STAMP - A.EVENT_TIME_STAMP) >= ?  \r\n"
				+ "          THEN 'TRUE' ELSE 'FALSE' \r\n" + "       END AS ALERT\r\n"
				+ "	 FROM \"PUBLIC\".\"LOG_MESSAGES_STARTED\" A ,\"PUBLIC\".\"LOG_MESSAGES_FINISHED\" B  WHERE A.ID=B.ID ) WITH DATA";

		log.debug("Populating Report SQL  {}", sql);
		jdbcTemplate.execute(sql.replace("?", "4"));

		/*
		 * jdbcTemplate.execute(sql,new PreparedStatementCallback<Boolean>(){
		 * 
		 * @Override public Boolean doInPreparedStatement(PreparedStatement ps) throws
		 * SQLException { ps.setString(1,"4"); return ps.execute(); } });
		 */

	}

	@Override
	public void insertError(Throwable t) {

		log.error("Inserting Error Data {} ", t.getMessage());
		String errorMessage = t.getMessage();
		if (errorMessage.length() > 100) {
			errorMessage = errorMessage.substring(0, 100);
		}
		String sql = "INSERT INTO BAD_RECORD_REPORT (LINE_NUMBER,ERROR_MESSAGE) VALUES(?,?)";
		jdbcTemplate.update(sql, new Object[] { null, errorMessage });
	}

}
