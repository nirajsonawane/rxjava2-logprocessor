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

import com.logprocessor.model.LogMessage;
import com.sun.org.apache.bcel.internal.generic.NEW;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ShouldCalculateDurationCorrectlyAndAlterFalg_Test {
	private static final Logger log = LoggerFactory.getLogger(ShouldCalculateDurationCorrectlyAndAlterFalg_Test.class);

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
	
	/*{"id":"22", "state":"STARTED", "type":"APPLICATION_LOG","host":"12345", "timestamp":1491377495212}
	{"id":"23", "state":"FINISHED", "timestamp":1491377495214}
	{"id":"23", "state":"STARTED", "timestamp":1491377495213}
	{"id":"22", "state":"FINISHED", "timestamp":1491377495217}*/
	
	
	
	@Test
	public void ShouldCalculateDurationCorrectlyAndAlterFalg() {
				
		 File resourcesDirectory = new File("src/test/resources");
		 String filePath =resourcesDirectory.getAbsolutePath()+"/durationTest.txt";
		 log.info("Using File :{} For Testing", filePath);
	    	logFileProcessor.processFile(filePath);		
		Assert.assertEquals(2,testUtil.getMessageCount());
		LogMessage messageById22 = testUtil.getMessageByIdAndState("22");
		LogMessage messageById23 = testUtil.getMessageByIdAndState("23");
		Assert.assertEquals(true, messageById22.getAlert());
		Assert.assertEquals(false, messageById23.getAlert());
		Assert.assertEquals(new Long(5), messageById22.getEventDuration());
		Assert.assertEquals(new Long(1), messageById23.getEventDuration());
	}

}
