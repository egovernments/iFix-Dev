package org.digit.exchange;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
class DigitExchangeApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	public void testDomain() {
		String str1 = "program1@https://unified-qa.digit.org/adf/";
		String str2 = "program1@http://unified-qa.digit.org/adf/";
		String str3 = "program1@htt://qa.digit.org/adf/";

		String domain1 = extractDomain(str1);
		String domain2 = extractDomain(str2);
		String domain3 = extractDomain(str3);

		log.info("Domain 1: " + domain1);
		log.info("Domain 2: " + domain2);
		log.info("Domain 3: " + domain3);
	}
	private static String extractDomain(String input) {
		// Regular expression pattern to match the domain with http/https
		Pattern pattern = Pattern.compile("(https?://[a-zA-Z0-9.-]+)");
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			return matcher.group(1); // Returns the matched domain with http/https
		} else {
			return null; // No match found
		}
	}

}
