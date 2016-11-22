package utils;

import java.util.Calendar;
import java.util.Date;

public class Utils {

	public static Date getTimeWarning() {
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.SECOND, -1 * 3600);
		Date timeWarning = cal.getTime();
		return timeWarning;
	}

	public static String normalizeText(String text) {
		if (text == null) {
			return null;
		}
		text = text.replace("\"", "\\\"");
		text = text.replace("\'", "\\\'");
		text = text.replace("\\", "\\\\");
		return text;
	}

	public static void main(String[] args) throws Exception {

	}
}
