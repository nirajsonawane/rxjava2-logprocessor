package com.logprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.logprocessor.model.LogMessage;


@Component
public class TestUtil {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int getMessageCount() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM LONG_RUNING_MESSAGE", Integer.class);
	}
	
	public int cleanUp() {
		return jdbcTemplate.update("truncate table LONG_RUNING_MESSAGE");
	}

	public LogMessage getMessageByIdAndState(String id) {
		return (LogMessage)jdbcTemplate.queryForObject("SELECT ID, EVENT_DURATION as EVENT_DURATION,ALERT FROM LONG_RUNING_MESSAGE where ID =? ",
				new Object[] { id}, new BeanPropertyRowMapper(LogMessage.class));
	}
	

}
