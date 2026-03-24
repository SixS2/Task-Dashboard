package com.dashboard.service;

import com.dashboard.dto.TaskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class AiService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.model.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public AiService() {
        this.restTemplate = new RestTemplate();
    }

    private final List<String> motivationalMessages = List.of(
        "Força e resiliência! Cada página lida é um passo mais perto do seu sonho. 📚🚀",
        "A disciplina é a ponte entre seus objetivos e suas realizações. Vamos com tudo! 💪",
        "Não deixe para amanhã o que pode ser estudado hoje. O sucesso te espera! ⏳✨",
        "Transforme a dificuldade em motivação. Você é capaz de aprender qualquer coisa! 🧠💡",
        "Pequenos progressos diários levam a grandes resultados. Continue no ritmo! 📈🔥",
        "Estudar não é uma obrigação, é uma porta aberta para o seu futuro. Siga firme! 🚪🌟"
    );

    public String generateMotivationalSummary(List<TaskDto> tasks) {
        if (tasks.isEmpty()) {
            return "O cronograma do AVA não possui tarefas pendentes no momento. Aproveite para revisar materiais antigos ou descansar! 🎉🛋️";
        }

        Random rand = new Random();
        String randomMsg = motivationalMessages.get(rand.nextInt(motivationalMessages.size()));
        
        return "Fique de olho nos prazos do AVA! " + randomMsg;
    }

    private final Map<String, String> cache = new java.util.concurrent.ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public void fillInsights(List<TaskDto> tasks) {
        if (apiKey == null || apiKey.equals("YOUR_API_KEY_HERE") || apiKey.isEmpty()) return;

        List<TaskDto> pending = new java.util.ArrayList<>();
        for (TaskDto t : tasks) {
            String cacheKey = t.getTitle() + "|" + t.getStartDateTime();
            if (cache.containsKey(cacheKey)) {
                t.setAiInsight(cache.get(cacheKey));
            } else {
                String desc = t.getDescription() != null ? t.getDescription().trim() : "";
                if (desc.isEmpty() || desc.length() < 10) {
                    String noContentMsg = "Essa tarefa não possui detalhes específicos. Entre direto no AVA para checar o conteúdo completo! 🌐";
                    t.setAiInsight(noContentMsg);
                    cache.put(cacheKey, noContentMsg);
                } else {
                    pending.add(t);
                }
            }
        }

        if (pending.isEmpty()) return;

        StringBuilder prompt = new StringBuilder();
        prompt.append("Como um mentor acadêmico altamente positivo, crie UMA dica curta (1 a 2 frases) de texto PURO (absolutamente SEM negrito, SEM markdown, SEM asteriscos), incluindo 1 emoji, para cada uma das tarefas listadas.\n")
              .append("As dicas DEVEM ser específicas baseadas nos CONTEÚDOS E TEMAS de cada descrição. Dê um conselho real que ajude o aluno naquele assunto específico.\n")
              .append("Responda EXCLUSIVAMENTE em formato JSON, onde a chave é o índice numérico (0, 1...) e o valor é a dica gerada.\n\n");

        int batchSize = Math.min(pending.size(), 30);
        for (int i = 0; i < batchSize; i++) {
            TaskDto t = pending.get(i);
            prompt.append("Tarefa ").append(i).append(": ").append(t.getTitle()).append("\n");
            prompt.append("Matéria: ").append(t.getCourse()).append("\n");
            String desc = t.getDescription() != null ? t.getDescription() : "";
            if (desc.length() > 250) desc = desc.substring(0, 250);
            prompt.append("Conteúdo/Tema: ").append(desc).append("\n\n");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt.toString())))));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String urlWithKey = apiUrl + "?key=" + apiKey;

            Map<String, Object> response = restTemplate.postForObject(urlWithKey, request, Map.class);
            
            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> contentObj = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) contentObj.get("parts");
                    String text = (String) parts.get(0).get("text");
                    
                    text = text.replace("```json", "").replace("```", "").trim();
                    
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    Map<String, String> jsonMap = mapper.readValue(text, new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {});
                    
                    for (int i = 0; i < batchSize; i++) {
                        TaskDto t = pending.get(i);
                        String insight = jsonMap.get(String.valueOf(i));
                        if (insight != null && !insight.isBlank()) {
                            t.setAiInsight(insight);
                            cache.put(t.getTitle() + "|" + t.getStartDateTime(), insight);
                        } else {
                            t.setAiInsight("Foque em iniciar esta atividade! 🚀");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Gemini API Error: " + e.getMessage() + " (Using fallback tips)");
            for (int i = 0; i < batchSize; i++) {
                pending.get(i).setAiInsight("Dica automática: Mantenha o foco e avance um passo de cada vez! 📚");
            }
        }
        
        for (int i = batchSize; i < pending.size(); i++) {
            pending.get(i).setAiInsight("Organize seu cronograma com calma! ⏰");
        }
    }
}
