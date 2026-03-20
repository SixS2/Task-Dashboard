package com.dashboard.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto implements Comparable<TaskDto> {
    private String title;
    private String course;
    private String url;
    private String description;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private String formattedDate;
    private String formattedEndDate;
    private String aiInsight;
    
    @Override
    public int compareTo(TaskDto o) {
        ZonedDateTime thisCompare = this.endDateTime != null ? this.endDateTime : this.startDateTime;
        ZonedDateTime oCompare = o.endDateTime != null ? o.endDateTime : o.startDateTime;
        
        if (thisCompare == null || oCompare == null) {
            if (this.startDateTime != null && o.startDateTime != null) {
                 return this.startDateTime.compareTo(o.startDateTime);
            }
            return 0;
        }
        return thisCompare.compareTo(oCompare);
    }
    
    public long getDaysUntilDue() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        ZonedDateTime deadline = this.endDateTime != null ? this.endDateTime : this.startDateTime;
        if (deadline == null) return 999;
        return java.time.temporal.ChronoUnit.DAYS.between(now, deadline.truncatedTo(java.time.temporal.ChronoUnit.DAYS));
    }
    
    public String getFormattedDeadline() {
        if (this.formattedEndDate != null && !this.formattedEndDate.isEmpty()) {
            return this.formattedEndDate;
        }
        return this.formattedDate != null ? this.formattedDate : "Sem Prazo";
    }
}
