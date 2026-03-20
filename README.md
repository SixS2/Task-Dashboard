# ✅ Task Dashboard (Painel de Atividades Inteligente)

## 📌 Sobre a Autoria do Projeto
**💡 Idealização e Escopo:** SixS2 
**💻 Desenvolvimento e Código:** Inteligência Artificial (Agente Autônomo)

Toda a ideia inicial, as regras de negócio e a arquitetura visual foram pensadas por mim para resolver um problema diário com a faculdade. No entanto, **todo o código-fonte (Java, HTML, Tailwind) deste projeto foi gerado e digitado do zero pela Inteligência Artificial**, atuando sob minha coordenação e instruções detalhadas em chat.

> ⚠️ **Aviso Importante sobre Inteligência Artificial:** 
> Este repositório foi construído com o objetivo central de **testar, praticar, conhecer e estudar** o estado atual da Arte das IAs na programação. **Eu NÃO recomendo que você crie códigos ou sistemas inteiros 100% baseados em Inteligências Artificiais** de forma cega. A IA comete erros, toma decisões limitadas e pode gerar gargalos se você não souber o que está acontecendo por trás das cortinas. Use ferramentas de IA para acelerar seus estudos, não para substituir seu conhecimento técnico!

---

## ⚙️ Como a Plataforma Funciona?
O **Task Dashboard** é uma plataforma web pessoal projetada para atuar como um organizador e assistente acadêmico de tarefas. O sistema une três fontes principais:

1. **Coleta de Dados Automática:** O núcleo em Java (backend) se conecta ao calendário oficial do Portal AVA (usando a assinatura XML/ICS do estudante). Ele varre todos os eventos universitários e joga tudo para uma lista local.
2. **Processamento e Filtragem:** O sistema ignora eventos passados, calcula a urgência de cada prazo (colorindo as tarefas na sua tela de verde pra vermelho) e extrai links perdidos no meio dos textos (como links do Teams ou de questionários).
3. **Cérebro de IA (Mentor):** Ao invés de apenas mostrar a ementa da prova, o Dashboard envia a descrição exata da disciplina para a API . A IA assume o papel de "Mentor Virtual", retornando mensagens únicas com dicas encorajadoras focadas na atividade daquele dia.

## 🎯 Público-Alvo e Futuro
Este código será utilizado para a turma de **ADS (Análise e Desenvolvimento de Sistemas)**, com foco inicial na **turma da manhã que tem aula aos Sábados**. Novas funcionalidades e futuras atualizações serão desenvolvidas e integradas com o passar do tempo!

## 🛠️ Tecnologias Utilizadas
- **Backend (O Motor):** Java 17 com Spring Boot 3. Usa `ical4j` para converter calendários e implementa um *Cache em Memória RAM* para garantir respostas instantâneas sem estourar o limite de acessos da IA do Google.
- **Frontend (Interface e UX):** Thymeleaf, JavaScript Vanilla e Tailwind CSS (Mobile First com Dark Mode nativo isolado).

> **Status:** Repositório de código restrito a estudo pessoal e experimentação de IA. Credenciais e tokens sensíveis não estão versionados neste código por segurança.
