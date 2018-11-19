package com.logprocessor;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ShouldFilterMessagesMissingEndtMessage_Test {
	private static final Logger log = LoggerFactory.getLogger(ShouldFilterMessagesMissingEndtMessage_Test.class);

	@Autowired
	private LogFileProcessor logFileProcessor;
	
	@Autowired
	private TestUtil testUtil;
	@Before
	public void setup()
	{
		testUtil.cleanUp();
		Util.cleanupMap();
	}
	
	@Test
	public void shouldFilterMessagesMissingStartMessage() {
				
		 File resourcesDirectory = new File("src/test/resources");
		 String filePath =resourcesDirectory.getAbsolutePath()+"/missingend.txt";
		 log.info("Using File :{} For Testing", filePath);
	    	logFileProcessor.processFile(filePath);		
		Assert.assertEquals(0,testUtil.getMessageCount() );
		
	}

}
