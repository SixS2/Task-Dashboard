package com.dashboard.service;

import com.dashboard.dto.TaskDto;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CalendarService {

    private final AiService aiService;

    public CalendarService(AiService aiService) {
        this.aiService = aiService;
    }

    @Value("${calendar.url}")
    private String calendarUrl;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private List<TaskDto> cachedTasks = null;
    private long lastFetchTime = 0;
    private static final long CACHE_DURATION_MS = 5 * 60 * 1000; // 5 minutos
    private static final java.util.regex.Pattern URL_PATTERN = java.util.regex.Pattern.compile("https?://[^\\s\\r\\n]+");

    public List<TaskDto> getUpcomingTasks() {
        if (cachedTasks != null && (System.currentTimeMillis() - lastFetchTime < CACHE_DURATION_MS)) {
            return cachedTasks;
        }
        
        List<TaskDto> tasks = new ArrayList<>();
        try (InputStream inputStream = URI.create(calendarUrl).toURL().openStream()) {
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(inputStream);

            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

            for (Object componentObj : calendar.getComponents(Component.VEVENT)) {
                VEvent event = (VEvent) componentObj;
                
                DtStart dtStart = event.getStartDate();
                if (dtStart != null && dtStart.getDate() != null) {
                    
                    // Handle converting the ical4j date to ZonedDateTime securely
                    ZonedDateTime eventDate = ZonedDateTime.ofInstant(dtStart.getDate().toInstant(), ZoneId.systemDefault());

                    String summary = event.getSummary() != null ? event.getSummary().getValue() : "Sem Título";
                        String rawDescription = event.getDescription() != null ? event.getDescription().getValue() : "";
                        String url = "";
                        java.util.regex.Matcher matcher = URL_PATTERN.matcher(rawDescription);
                        if (matcher.find()) {
                            url = matcher.group();
                        }
                        
                        // Remover URLs e qualquer variação de 'Exibir evento', além de traços no final
                        String description = rawDescription.replaceAll("https?://[^\\s\\r\\n]+", "")
                                                 .replaceAll("Exibir evento.*", "")
                                                 .replaceAll("\\s*[-–—]?\\s*$", "")
                                                 .trim();
                        
                        String course = "Geral";
                        Property locationProp = event.getProperty(Property.LOCATION);
                        if (locationProp != null && locationProp.getValue() != null) {
                            String locVal = locationProp.getValue().trim();
                            if (!locVal.startsWith("http")) {
                                String[] parts = locVal.split(" - ");
                                course = parts[0].trim();
                            } else if (url.trim().isEmpty()) {
                                url = locVal.split(" ")[0];
                            }
                        }
                        
                        TaskDto dto = new TaskDto();
                        dto.setTitle(summary);
                        dto.setCourse(course);
                        dto.setUrl(url);
                        dto.setDescription(description);
                        dto.setStartDateTime(eventDate);
                        dto.setFormattedDate(eventDate.format(FORMATTER));
                        
                        DtEnd dtEnd = event.getEndDate();
                        if (dtEnd != null && dtEnd.getDate() != null) {
                            ZonedDateTime endDate = ZonedDateTime.ofInstant(dtEnd.getDate().toInstant(), ZoneId.systemDefault());
                            dto.setEndDateTime(endDate);
                            dto.setFormattedEndDate(endDate.format(FORMATTER));
                        } else {
                            dto.setEndDateTime(null);
                            dto.setFormattedEndDate("");
                        }
                        ZonedDateTime deadline = dto.getEndDateTime() != null ? dto.getEndDateTime() : dto.getStartDateTime();
                        if (deadline != null && deadline.isBefore(now)) {
                            continue;
                        }
                        
                        tasks.add(dto);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar calendário: " + e.getMessage());
            // Retorna lista vazia em caso de falha.
        }
        
        Collections.sort(tasks);
        aiService.fillInsights(tasks);
        
        this.cachedTasks = tasks;
        this.lastFetchTime = System.currentTimeMillis();
        
        return tasks;
    }
}
