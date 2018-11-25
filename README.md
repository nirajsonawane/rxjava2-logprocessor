# RXJava-Log-Processor
RXJava2 Application For Processing Log File

### How To Build ### 
**Gradle:** gradle build 

**Maven:**  mvn clean install

### How To Run ###

java -jar rxjava2-logprocessor-1.0.0-SNAPSHOT.jar <File-Path>

Sample : java -jar rxjava2-logprocessor-1.0.0-SNAPSHOT.jar D:/Niraj/Programming/logFile.txt 

Application is build on Spring Boot and make use of `hsqldb` file based database. To View the file based database please uncommnet below code in `BatchConfiguration`

```
//@PostConstruct
	public void getDbManager() {
		System.setProperty("java.awt.headless", "false");
		DatabaseManagerSwing.main(
				new String[] { "--url", "jdbc:hsqldb:file:db/log-messagees-db", "--user", "sa", "--password", "" });
	}
```

`DataGenerator` class can be used to generate test data.(Move this class to Main package from Test package)

**Key Points About Solution** 

1: Build on Reactive Programming paradigm using RXjava.

2: Build on Stream processing so can handle large files.

3: Individual code component can be tested using provided unit test cases. 

4: Open-source library's like Spring-boot, hibernate.validator & project Lombok are used. 

5: Code is readable, testable and extensible 

**External dependency:** For Faster development application libraries like  Spring-boot,hibernate.validator & project lombok are used.

**High level Flow Of Application:**

`LogFileObservableSource` subscribe's to log file and emits the complete json objects. 

 `Observable` Maps the String Json to Java Objects. 
 
 Then `Filter` messages with invalid json and missing Mandatory fields.
 
 Then `Map` to Calculate duration of Message and Alert flag.      
 
 All this information is passed in chuks to `Subscriber`
 
The new item emitted from the observable,are processed by subscriber and are inserted in database.

```
final Observable<List<LogMessage>> observable = Observable
				.defer(() -> new LogFileObservableSource(filePath)) // Emits json string from file
				.map(Util::logMessageMapper)  // Convert String to Java Objects 
				.filter(Util::isValidatMessage) // Filter invalid MEssages 
				.map(Util::durationMapper) // calculate duration of message and update alter flag
				.filter(LogMessage::getIsValid)  // fiter messages incase does not able to map in last step
				.buffer(chunkSize); //create chunk
```

```
observable.subscribe(
   messageData -> daoService.insertBatch(messageData), // Insert chuk data to database 
   Throwable::printStackTrace,                         //Handel errors , TODO we can re direct erros to files or database 
   () -> log.info("File Processing Completed")         //hadel End of batch 
	  );
```
**Update** 
```
observable.subscribe(
     messageData -> completableFuturelist.add(daoService.insertBatch(messageData)),//insert chunk and store refrance of ompletableFutur	      Throwable::printStackTrace,						  
     () ->{	
	     log.info("Waiting for all database insert threads to complete  ");
	     CompletableFuture.allOf(completableFuturelist.toArray(new CompletableFuture[completableFuturelist.size()])).join();
	     log.info("Total Message Count {} ", daoService.getTotalRowCount());
	     executor.shutdown();
	   }
     );
```

**Assumptions** Log File will not have log messages spanning more 2 lines.  

**TODO**
1 Improve subscriber using `subscribeOn(..)` - **Update** Insted of using `subscribeOn` used Spring `@Async` Method called. Updated insertBatch method to return `CompletableFuture`. Time Spend 30 mins     


Approximate Time Spend: Around 3.5 to 4 hours




