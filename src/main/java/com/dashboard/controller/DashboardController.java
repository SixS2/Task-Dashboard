package com.dashboard.controller;

import com.dashboard.dto.TaskDto;
import com.dashboard.service.AiService;
import com.dashboard.service.AvisoService;
import com.dashboard.service.CalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final CalendarService calendarService;
    private final AiService aiService;
    private final AvisoService avisoService;

    public DashboardController(CalendarService calendarService, AiService aiService, AvisoService avisoService) {
        this.calendarService = calendarService;
        this.aiService = aiService;
        this.avisoService = avisoService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/matutino";
    }

    @GetMapping("/matutino")
    public String matutino(Model model) {
        List<TaskDto> upcomingTasks = calendarService.getMatutinoTasks();
        model.addAttribute("tasks", upcomingTasks);
        model.addAttribute("aiMessage", aiService.generateMotivationalSummary(upcomingTasks));
        model.addAttribute("pageTitle", "Período Matutino");
        model.addAttribute("pageDesc", "Acompanhe de perto as disciplinas da manhã.");
        return "index";
    }

    @GetMapping("/noturno")
    public String noturno(Model model) {
        List<TaskDto> upcomingTasks = calendarService.getNoturnoTasks();
        model.addAttribute("tasks", upcomingTasks);
        model.addAttribute("aiMessage", aiService.generateMotivationalSummary(upcomingTasks));
        model.addAttribute("pageTitle", "Período Noturno");
        model.addAttribute("pageDesc", "Foco total para as aulas do período da noite.");
        return "index";
    }

    @GetMapping("/avisos")
    public String avisos(Model model) {
        // Agora usamos a classe de Avisos reais em vez de Tarefas
        model.addAttribute("avisos", avisoService.getActiveAvisos());
        model.addAttribute("isAvisosPage", true);
        model.addAttribute("pageTitle", "Avisos Recentes");
        model.addAttribute("pageDesc", "Mural oficial de comunicados da faculdade e professores.");
        return "index";
    }

}
