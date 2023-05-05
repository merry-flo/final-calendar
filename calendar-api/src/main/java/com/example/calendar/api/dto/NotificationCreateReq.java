package com.example.calendar.api.dto;

import com.example.calendar.core.exception.CalendarException;
import com.example.calendar.core.exception.ErrorCode;
import com.example.calendar.core.util.TimeUnit;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationCreateReq {
    @NotBlank
    private final String title;

    @NotNull
    private final LocalDateTime notifyAt;
    private final RepeatInfo repeatInfo;

    public List<LocalDateTime> getRepeatTimes() {
        if (repeatInfo == null) {
            return Collections.singletonList(notifyAt);
        }

        return IntStream.range(0, repeatInfo.times)
                        .mapToObj(i -> {
                            long increment = (long) repeatInfo.interval.intervalValue * i;
                            switch(repeatInfo.interval.timeUnit) {
                                case DAY:
                                    return notifyAt.plusDays(increment);
                                case WEEK:
                                    return notifyAt.plusWeeks(increment);
                                case MONTH:
                                    return notifyAt.plusMonths(increment);
                                case YEAR:
                                    return notifyAt.plusYears(increment);
                                default:
                                    throw new CalendarException(ErrorCode.NOT_SUPPORTED_OPERATION);
                            }
                        })
                        .collect(Collectors.toList());
    }

    @Data
    public static class RepeatInfo {
        private final Interval interval;
        private final int times;
    }

    @Data
    public static class Interval {
        private final int intervalValue;
        private final TimeUnit timeUnit;
    }
}
