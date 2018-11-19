package com.logprocessor;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ShouldProcessAllValidMessagesTests {

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
	public void shouldLoadAllValidMessagesInDataBase() {
				
		 File resourcesDirectory = new File("src/test/resources");
	    	logFileProcessor.processFile(resourcesDirectory.getAbsolutePath()+"/validMessages.txt");		
		Assert.assertEquals(3,testUtil.getMessageCount() );
		
	}

}
