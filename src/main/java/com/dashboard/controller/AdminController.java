package com.dashboard.controller;

import com.dashboard.dto.Aviso;
import com.dashboard.service.AvisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
public class AdminController {

    @Value("${admin.user:}")
    private String adminUser;

    @Value("${admin.password:}")
    private String adminPassword;

    @Autowired
    private AvisoService avisoService;

    @Autowired
    private com.dashboard.service.CalendarService calendarService;

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("admin_logged_in") != null) {
            return "redirect:/admin";
        }
        return "login";
    }

    @PostMapping("/login")
    public String performLogin(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        if (adminUser.equals(username) && adminPassword.equals(password)) {
            // Session Fixation Protection: Invalidate old session and create a new one
            request.getSession().invalidate();
            HttpSession newSession = request.getSession(true);
            
            newSession.setAttribute("admin_logged_in", true);
            
            // Generate CSRF Token for the session
            String csrfToken = java.util.UUID.randomUUID().toString();
            newSession.setAttribute("csrf_token", csrfToken);
            
            return "redirect:/admin";
        }
        redirectAttributes.addFlashAttribute("error", "Usuário ou senha inválidos.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("avisos", avisoService.getAllAvisos());
        return "admin";
    }

    @PostMapping("/admin/add")
    public String addAviso(@RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam("dias") int diasAtivos,
                           @RequestParam(value = "color", defaultValue = "indigo") String color,
                           @RequestParam(value = "authorName", defaultValue = "Coordenacao") String authorName,
                           @RequestParam(value = "authorImage", required = false) MultipartFile authorImage,
                           @RequestParam(value = "image", required = false) MultipartFile image) {
        Aviso aviso = new Aviso();
        aviso.setTitle(title);
        aviso.setContent(content);
        aviso.setColor(color);
        aviso.setAuthorName(authorName != null && !authorName.trim().isEmpty() ? authorName.trim() : "Coordenacao");
        aviso.setExpiresAt(LocalDate.now().plusDays(diasAtivos));
        avisoService.addAviso(aviso, image, authorImage);
        return "redirect:/admin";
    }

    @PostMapping("/admin/remove")
    public String removeAviso(@RequestParam("id") String id) {
        avisoService.removeAviso(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/sync")
    public String sync(HttpServletRequest request) {
        calendarService.clearCache();
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    // API endpoint for polling (avisos page auto-refresh)
    @org.springframework.web.bind.annotation.ResponseBody
    @GetMapping("/api/avisos/count")
    public java.util.Map<String, Object> avisosCount() {
        java.util.List<Aviso> active = avisoService.getActiveAvisos();
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("count", active.size());
        if (!active.isEmpty()) {
            result.put("latestId", active.get(active.size() - 1).getId());
        }
        return result;
    }
}
