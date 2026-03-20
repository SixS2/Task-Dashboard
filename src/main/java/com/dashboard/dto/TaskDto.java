package com.dashboard.dto;

import java.time.ZoneId;
import java.time.ZonedDateTime;


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

    public TaskDto() {}

    public TaskDto(String title, String course, String url, String description, ZonedDateTime startDateTime, ZonedDateTime endDateTime, String formattedDate, String formattedEndDate, String aiInsight) {
        this.title = title;
        this.course = course;
        this.url = url;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.formattedDate = formattedDate;
        this.formattedEndDate = formattedEndDate;
        this.aiInsight = aiInsight;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ZonedDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(ZonedDateTime startDateTime) { this.startDateTime = startDateTime; }

    public ZonedDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(ZonedDateTime endDateTime) { this.endDateTime = endDateTime; }

    public String getFormattedDate() { return formattedDate; }
    public void setFormattedDate(String formattedDate) { this.formattedDate = formattedDate; }

    public String getFormattedEndDate() { return formattedEndDate; }
    public void setFormattedEndDate(String formattedEndDate) { this.formattedEndDate = formattedEndDate; }

    public String getAiInsight() { return aiInsight; }
    public void setAiInsight(String aiInsight) { this.aiInsight = aiInsight; }
    
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
