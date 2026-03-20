package com.dashboard.controller;

import com.dashboard.dto.TaskDto;
import com.dashboard.service.AiService;
import com.dashboard.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final CalendarService calendarService;
    private final AiService aiService;

    @Autowired
    public DashboardController(CalendarService calendarService, AiService aiService) {
        this.calendarService = calendarService;
        this.aiService = aiService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<TaskDto> upcomingTasks = calendarService.getUpcomingTasks();
        String aiMessage = aiService.generateMotivationalSummary(upcomingTasks);

        model.addAttribute("tasks", upcomingTasks);
        model.addAttribute("aiMessage", aiMessage);

        return "index";
    }
}
