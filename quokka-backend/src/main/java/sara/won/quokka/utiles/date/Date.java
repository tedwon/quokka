package sara.won.quokka.utiles.date;

import java.time.LocalDate;
import java.time.Period;

public class Date {

    public static String calcMyAge(final int year, final int month, final int dayOfMonth) {
        // Get the start date
        LocalDate startDate = LocalDate.of(year, month, dayOfMonth);

        // Get the current date
        LocalDate today = LocalDate.now();

        // Calculate the number of days between the two dates
        Period period = Period.between(startDate, today);

        // Print the number of days
        final String formattedPeriod = String.format("%d years, %d months, %d days", period.getYears(), period.getMonths(), period.getDays());
        return formattedPeriod;
    }
}
