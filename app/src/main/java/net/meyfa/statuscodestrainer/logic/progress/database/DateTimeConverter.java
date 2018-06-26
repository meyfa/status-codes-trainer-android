package net.meyfa.statuscodestrainer.logic.progress.database;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTime;

/**
 * Offers type converter methods for converting between {@link DateTime} and {@link String}.
 */
public class DateTimeConverter
{
    @TypeConverter
    public static DateTime fromString(String string)
    {
        return DateTime.parse(string);
    }

    @TypeConverter
    public static String dateTimeToString(DateTime dateTime)
    {
        return dateTime.toString();
    }
}
