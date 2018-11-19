package com.logprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public class LogFileObservableSource implements ObservableSource<String> {

	private final String filename;

	LogFileObservableSource(String filename) {
		this.filename = filename;
	}

	@Override
	public void subscribe(Observer<? super String> observer) {
		try (Stream<String> lines = Files.lines(Paths.get(filename))) {

			List<String> strings = new ArrayList<>();			
			
			lines.forEach(inputLine ->{
					sendDataToObserver(observer, strings, inputLine);
				});
			observer.onComplete();
		} catch (IOException e) {
			observer.onError(e);
		}
	}

	private void sendDataToObserver(Observer<? super String> observer, List<String> strings, String inputLine) {
		if (isLineContainsCompleteObject(inputLine))
			observer.onNext(inputLine);
		else {
			handelMultilineData(observer, strings, inputLine);
		}
	}

	private boolean isLineContainsCompleteObject(String inputLine) {
		return inputLine.contains("{") && inputLine.contains("}");
	}

	private void handelMultilineData(Observer<? super String> observer, List<String> strings, String str) {
		if (strings.isEmpty()) {
			strings.add(str);
		} else {
			observer.onNext(strings.get(0) + str);
			strings.clear();
		}
	}
}
