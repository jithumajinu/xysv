package io.crm.app.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateUtil {

    private static final ZoneId jstZoneId = TimeZone.getTimeZone("JST").toZoneId();

    public static LocalDateTime toJstNowDateTime() {
        return DateUtil.toJstDateTime(LocalDateTime.now());
    }

    public static LocalDateTime toJstDateTime(LocalDateTime localDateTime) {
        if( localDateTime == null )
            return null;

        if( !jstZoneId.equals(ZoneId.systemDefault()) ) {
            OffsetDateTime offsetDateTime = localDateTime.atOffset(jstZoneId
                    .getRules()
                    .getOffset(LocalDateTime.now()));
            localDateTime = localDateTime.plusSeconds(offsetDateTime.getOffset().getTotalSeconds());
        }
        return localDateTime;
    }

    public static LocalDate toJstDate(LocalDateTime localDateTime) {
        if( localDateTime == null )
            return null;

        if( !jstZoneId.equals(ZoneId.systemDefault()) ) {
            OffsetDateTime offsetDateTime = localDateTime.atOffset(jstZoneId
                    .getRules()
                    .getOffset(LocalDateTime.now()));
            localDateTime = localDateTime.plusSeconds(offsetDateTime.getOffset().getTotalSeconds());
        }
        return localDateTime.toLocalDate();
    }

    public static String toJstNowDateTimeFormat(String format) {
        return DateUtil.toJstDateTimeFormat(LocalDateTime.now(), format);
    }
    public static String toJstDateTimeFormat(LocalDateTime localDateTime, String format) {
        LocalDateTime jstLocalDateTime = DateUtil.toJstDateTime(localDateTime);
        if( jstLocalDateTime == null)
            return null;

        return jstLocalDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime toUtcDateTime(LocalDateTime ldt, boolean startDateTime) {
        if( ldt == null )
            return null;
        if( startDateTime )
            ldt = ldt.withHour(0).withMinute(0).withSecond(0).withNano(0);
        else
            ldt = ldt.withHour(23).withMinute(59).withSecond(59).withNano(999);

        ZonedDateTime jstZoned = ldt.atZone(ZoneId.of("JST", ZoneId.SHORT_IDS));
        ZonedDateTime utcZoned = jstZoned.withZoneSameInstant(ZoneId.of("UTC"));

        return utcZoned.toLocalDateTime();
    }

    public static LocalDateTime toUtcDateTime(LocalDate ld, boolean startDateTime) {
        if( ld == null )
            return null;
        LocalDateTime ldt = null;
        if( startDateTime )
            ldt = ld.atTime(0, 0, 0, 0);
        else
            ldt = ld.atTime(23, 59, 59, 0);

        ZonedDateTime jstZoned = ldt.atZone(ZoneId.of("JST", ZoneId.SHORT_IDS));
        ZonedDateTime utcZoned = jstZoned.withZoneSameInstant(ZoneId.of("UTC"));

        return utcZoned.toLocalDateTime();
    }

    public static LocalDateTime addTimeToDate(LocalDate ld, boolean startDateTime) {
        if( ld == null )
            return null;
        LocalDateTime ldt = null;
        if( startDateTime )
            ldt = ld.atTime(0, 0, 0, 0);
        else
            ldt = ld.atTime(23, 59, 59, 999);

        return ldt;
    }

    public static String toDateFormat(LocalDate localDate, String format) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }
}