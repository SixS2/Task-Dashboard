# 🎓 Study Dashboard (Painel de Estudos Inteligente)

Um dashboard moderno e minimalista desenvolvido em **Java (Spring Boot)** e **Tailwind CSS**, criado para organizar suas tarefas universitárias extraindo dados automaticamente do calendário D2L Brightspace (AVA). O painel também utiliza a **API Gemini do Google** para gerar insights motivacionais e dicas de estudo personalizadas baseadas no conteúdo das suas disciplinas!

## 🚀 Funcionalidades

- **Sincronização Automática:** Lê o feed `.ics` da sua universidade e puxa as tarefas mais recentes.
- **Dicas com Inteligência Artificial:** Integração via REST com o Google Gemini 2.5 Flash para ler a ementa da sua tarefa e dar as melhores dicas em tempo real.
- **Cache Inteligente:** Otimizado com RAM Cache para evitar "Rate Limits" (Excesso de requisições) na IA e de rede.
- **Design Moderno e Responsivo:** Cores Soft (Teal/Slate) feitas em Tailwind, perfeitamente adaptado para telas de celulares.
- **Dark Mode Nativo:** Botão de Lua/Sol que altera completamente o tema do sistema, salvando as preferências do usuário no navegador.
- **Links Rápidos:** Extrai links de Teams/Zoom ou tarefas do AVA e gera botões instantâneos de "Acessar Atividade".

## 🛠️ Tecnologias Utilizadas

- **Backend:** Java 17, Spring Boot 3, Maven.
- **Integração e Automação:** `ical4j` para processamento de calendários, `RestTemplate` para consumo da API do Gemini.
- **Frontend:** Thymeleaf (Template Engine), Vanilla JS, Tailwind CSS via CDN.

## ⚙️ Como rodar o projeto localmente

Para rodar o projeto localmente de forma segura (sem expor suas senhas e tokens na internet), siga os passos abaixo:

1. **Clone este repositório** para seu computador:
   ```bash
   git clone https://github.com/SEU_USUARIO/study-dashboard.git
   cd study-dashboard
   ```

2. **Configure suas chaves ambientais:**
   O projeto exige dois dados sensíveis: sua chave da API do Google Gemini e a URL do seu calendário `.ics` (que contém um token privativo do AVA).
   
   - Na pasta `src/main/resources/`, crie um arquivo chamado **`application-local.properties`**.
   - Note que este arquivo já está incluído nas regras do `.gitignore`, então ele **nunca** será upado para o GitHub, seus dados estão seguros!
   - Adicione suas credenciais nele exatamente deste jeito (sem aspas):
     ```properties
     ai.api.key=SUA_CHAVE_DO_GEMINI_AQUI
     calendar.url=https://LINK_DO_SEU_CALENDARIO_AQUI_COM_TOKEN
     ```

3. **Inicie o Servidor Spring Boot:**
   Utilizando o Maven Command Line (no terminal local da pasta raiz):
   ```bash
   mvn spring-boot:run
   ```
   *Certifique-se de que a porta 8080 está livre no seu computador.*

4. **Acesse a Aplicação:**
   Abra seu navegador maravilhoso e acesse [http://localhost:8080](http://localhost:8080).

## 🔒 Hospedando (Deploy em Servidor de Produção)

Se quiser colocar seu painel na nuvem (como Railway, Render ou Heroku) para acessar do celular sem estar no PC:
Conecte este repositório na plataforma escolhida e preencha as Variáveis de Ambiente ("Environment Variables") nos Painéis de Configuração do Servidor usando os exatos mesmos nomes:

- Clave Variável: `GEMINI_API_KEY` (Sua chave)
- Clave Variável: `CALENDAR_URL` (Sua URL .ics)

O Spring Boot foi magicamente configurado para puxar as variáveis locais *ou* do servidor em nuvem automaticamente!
