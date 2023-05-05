package com.example.calendar.api.dto;

import com.example.calendar.core.util.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import lombok.Getter;
import org.springframework.data.util.Pair;

public class EngagementMailDto {

    public static final String MAIL_TIME_FORMAT = "yyyy년 MM월 dd일(E) a hh시 mm분";

    @Getter
    private final Long engagementId;
    @Getter
    private final String from;
    @Getter
    private final String to;
    private final Set<String> attendees;
    private final String title;
    private final Period period;
    private final String periodStr;

    public EngagementMailDto(Long engagementId, String from, String to, Set<String> attendees, String title,
        Period period) {
        this.engagementId = engagementId;
        this.from = from;
        this.to = to;
        this.attendees = attendees;
        this.title = title;
        this.period = period;
        this.periodStr = getPeriodStr();
    }

    public String getSubject() {
        return "[초대장 ]"
            + title
            + " - "
            + period.toString()
            + "("
            + to
            + ")";
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("title", title);
        props.put("period", period.toString());
        props.put("from", from);
        props.put("calendar", to);
        props.put("attendees", attendees);
        return props;
    }


    private String getPeriodStr() {
        return String.format("%s - %s",
            period.getStartAt().format(DateTimeFormatter.ofPattern(MAIL_TIME_FORMAT)),
            period.getEndAt().format(DateTimeFormatter.ofPattern(getEndAtFormat()))
        );
    }

    public String getEndAtFormat() {
        String endAtFormat = MAIL_TIME_FORMAT;
        if (period.getEndAt().getYear() == period.getStartAt().getYear()) {
            endAtFormat = endAtFormat.replace("yyyy년 ", "");
            if (period.getEndAt().getMonth() == period.getStartAt().getMonth()) {
                endAtFormat = endAtFormat.replace("MM월 ", "");
                if (period.getEndAt().getDayOfMonth() == period.getStartAt().getDayOfMonth()) {
                    endAtFormat = endAtFormat.replace("dd일(E) ", "");
                }
            }
        }
        return endAtFormat;
    }

    @Override
    public String toString() {
        return "EngagementMailDto{" + "engagementId=" + engagementId
            + ", from='" + from
            + ", to='" + to
            + ", attendees=" + attendees
            + ", title='" + title
            + ", period=" + period
            + '}';
    }
}
