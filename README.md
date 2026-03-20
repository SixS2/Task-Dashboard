# 🎓 Study Dashboard (Meu Painel de Estudos Inteligente)

## 💡 Motivação e Objetivo
Criei este projeto para resolver um problema pessoal real: a extrema dificuldade e ansiedade de acompanhar dezenas de prazos e atividades da universidade (Católica) dispersos pelo portal acadêmico (AVA/D2L Brightspace). 

O objetivo principal desta aplicação é **centralizar, organizar e priorizar** meus estudos de forma inteligente e automatizada. Ao invés de perder tempo abrindo o portal repetidas vezes e caçando o que está vencendo, o sistema puxa todos os dados por mim e cria uma hierarquia visual de urgência.

## 🧠 Por que integrar Inteligência Artificial?
Apenas listar tarefas como um calendário comum não era o suficiente para impulsionar minha produtividade. Eu queria algo que me trouxesse engajamento. 
Para resolver isso, integrei a API do **Google Gemini 2.5 Flash**. A Inteligência Artificial analisa a ementa ou descrição de cada quiz/atividade (extraídos do próprio feed da faculdade) e atua como um "mentor virtual". Ela gera dicas motivacionais, alertas sobre o nível de foco exigido e conselhos específicos baseados na matéria que preciso estudar naquele dia!

## 🛠️ Como o ecossistema foi construído
O desenvolvimento do projeto uniu a prática de Engenharia de Software com a necessidade do dia a dia:

- **Backend (O Cérebro):** Desenvolvido em **Java 17 operando com Spring Boot**. Ele consome assinaturas `XML/ICS`, filtra lixos e atividades passadas, e faz orquestrações complexas. Implementei um *Cache em Memória RAM de 5 minutos* para garantir respostas em milissegundos na tela, esticando a cota grátis da IA e poupando banda.
- **Frontend (Interface e UX):** Criado com **Thymeleaf e Tailwind CSS**. Criei um design altamente focado e relaxante (`Teal`/`Slate`), totalmente "Mobile First" (perfeitamente alinhado em celulares) e implementei um Dark Mode isolado via `localStorage`.

> **Status:** Repositório de código restrito a estudo pessoal e uso particular de rotina. Nenhuma chave secreta ou API vaza neste código-fonte.
