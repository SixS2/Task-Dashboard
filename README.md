# ✅ Task Dashboard 

🔗 **Acesso ao Projeto Online:** [Aguardando Link do Deploy...]()

## 📌 Sobre a Autoria do Projeto
**💡 Idealização e Escopo:** SixS2 
**💻 Desenvolvimento e Código:** Inteligência Artificial

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

## 🌍 Como Hospedar e Acessar Online (Deploy Grátis)
Diferente de projetos em HTML básico, sistemas robustos em Java (Spring Boot) **não funcionam no GitHub Pages**. O GitHub Pages hospeda apenas sites estáticos, e o Java precisa de um servidor real (container) rodando 24 horas por dia! 

A forma mais recomendada e **100% gratuita** de hospedar esse código diretamente do seu GitHub é utilizando o **Render.com**.

### Passo a Passo para colocar o Projeto no Ar:
1. Crie uma conta no [Render.com](https://render.com) vinculando o seu GitHub.
2. No painel, clique em **"New Web Service"** (Novo Serviço Web) e conecte o repositório `Task-Dashboard`.
3. O Render vai reconhecer automaticamente o código como Java Maven. Preencha os campos de compilação:
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/study-dashboard-0.0.1-SNAPSHOT.jar`
4. **As Chaves Secretas (Passo Obrigatório):**
   Role a tela do Render até encontrar **Environment Variables** (Variáveis de Ambiente) e adicione suas chaves ocultas para habilitar a Inteligência Artificial e a leitura do Calendário D2L:
   - Add Environment Variable -> Key: `GEMINI_API_KEY` | Value: `SUA_CHAVE_AQUI`
   - Add Environment Variable -> Key: `CALENDAR_URL` | Value: `SEU_LINK_ICS_O_MESMO_QUE_VOCE_TESTOU`
5. Clique no botão **Deploy Web Service**! 

Em alguns minutos, o painel vai carregar o Java na nuvem e te gerar um link oficial azul no canto esquerdo superior (ex: `https://task-dashboard-xx1x.onrender.com`). Pegue esse link gerado, coloque-o ali no topo deste README e pronto: você pode acessar seu portal de estudos do celular de onde estiver!

> **Status Local:** Repositório de código restrito a demonstração. Credenciais e tokens não estão versionados publicamente neste código por segurança.
