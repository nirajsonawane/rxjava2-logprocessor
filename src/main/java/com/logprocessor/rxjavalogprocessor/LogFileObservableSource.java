package com.logprocessor.rxjavalogprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public class LogFileObservableSource implements ObservableSource<String> {

	private final String filename;

	LogFileObservableSource(String filename) {
		this.filename = filename;
	}

	@Override
	public void subscribe(Observer<? super String> observer) {
		try {
			String temp = "";
			List<String> strings = new ArrayList<>();

			Files.lines(Paths.get(filename))
					.forEach((str) ->
						{
							if (str.contains("{") && str.contains("}"))
								observer.onNext(str);
							else {
								if (strings.isEmpty()) {
									strings.add(str);
								} else {
									observer.onNext(strings.get(0) + str);
									strings.clear();
								}
							}

						});
			observer.onComplete();
		} catch (IOException e) {
			observer.onError(e);
		}
	}
}
