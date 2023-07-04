package com.personal.gradle.build_time;

import java.io.BufferedReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.utils.io.IoUtils;
import com.utils.io.ReaderUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.vitesco.pa.writers.file_writers.DataFileWriterXlsx;
import com.vitesco.pa.writers.file_writers.data.DataTable;

final class AppStartGradleBuildTimeAnalyzer {

	private AppStartGradleBuildTimeAnalyzer() {
	}

	public static void main(
			final String[] args) throws Exception {

		final Instant start = Instant.now();
		Logger.setDebugMode(true);

		Logger.printProgress("--> starting GradleBuildTimeAnalyzer");

		final Map<String, List<Long>> nameToTimeListMap = new HashMap<>();
		readInputFile(nameToTimeListMap);

		final List<TimeInterval> timeIntervalList = new ArrayList<>();
		fillTimeIntervalList(nameToTimeListMap, timeIntervalList);

		writeOutputFile(timeIntervalList);

		Logger.printFinishMessage(start);
	}

	private static void readInputFile(
			final Map<String, List<Long>> nameToTimeListMap) throws Exception {

		final String inputFilePathString = "D:\\tmp\\GradleBuildTimeAnalyzer\\input.txt";
		final Pattern pattern = Pattern.compile("task (.*) (start|end) time: (\\d+)");
		try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(inputFilePathString)) {

			String line;
			while ((line = bufferedReader.readLine()) != null) {

				final Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {

					final String name = matcher.group(1);
					final String timeString = matcher.group(3);
					final long time = StrUtils.tryParsePositiveLong(timeString);
					final List<Long> timeList =
							nameToTimeListMap.computeIfAbsent(name, k -> new ArrayList<>());
					timeList.add(time);
				}
			}
		}
	}

	private static void fillTimeIntervalList(
			final Map<String, List<Long>> nameToTimeListMap,
			final List<TimeInterval> timeIntervalList) {

		long minTime = Long.MAX_VALUE;
		for (final List<Long> timeList : nameToTimeListMap.values()) {

			for (final long time : timeList) {
				minTime = Math.min(minTime, time);
			}
		}

		for (final Map.Entry<String, List<Long>> mapEntry : nameToTimeListMap.entrySet()) {

			final String name = mapEntry.getKey();
			final List<Long> timeList = mapEntry.getValue();
			timeList.sort(Comparator.naturalOrder());

			final long startTime = timeList.get(0) - minTime;
			final long endTime = timeList.get(timeList.size() - 1) - minTime;

			final TimeInterval timeInterval = new TimeInterval(name, startTime, endTime);
			timeIntervalList.add(timeInterval);
		}

		timeIntervalList.sort(Comparator.comparing(TimeInterval::getStartTime));
	}

	private static void writeOutputFile(
			final List<TimeInterval> timeIntervalList) throws Exception {

		final String outputFilePathString = "D:\\tmp\\GradleBuildTimeAnalyzer\\output.xlsx";

		final List<DataTable> dataTableList = new ArrayList<>();
		dataTableList.add(new DataTable("time intervals", "TimeIntervals", "TimeInterval",
				TimeInterval.COLUMNS, timeIntervalList));
		new DataFileWriterXlsx().write("time chart", outputFilePathString, dataTableList);

		IoUtils.openFileWithDefaultApp(outputFilePathString);
	}
}
