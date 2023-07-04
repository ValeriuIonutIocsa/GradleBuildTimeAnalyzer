package com.personal.gradle.build_time;

import org.junit.jupiter.api.Test;

class AppStartGradleBuildTimeAnalyzerTest {

	@Test
	void testMain() throws Exception {

		final String[] args;
		final int input = Integer.parseInt("1");
		if (input == 1) {
			args = new String[] {};
		} else {
			throw new RuntimeException();
		}

		AppStartGradleBuildTimeAnalyzer.main(args);
	}
}
