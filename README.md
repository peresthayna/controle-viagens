# Gestão de Viagens Corporativas

O aplicativo Gestão de Viagens é uma solução completa (Full Stack) desenvolvida para o controle e monitoramento de viagens. O projeto foca em uma experiência de usuário fluida e em um backend robusto com regras de negócio sólidas e segurança via JWT.

---

## Tecnologias Utilizadas

Front-end (Mobile):
- *Flutter e Dart*: Interface reativa e moderna.
- *Provider*: Gerenciamento de estado eficiente.
- *Flutter Secure Storage*: Armazenamento seguro de tokens JWT.
- *http*: Comunicação com a API REST.

Back-end:
- *Java 17 e Spring Boot*: Base sólida e escalável.
- *Spring Security e JWT*: Autenticação e autorização (Access/Refresh Token).
- *Spring Data JPA*: Persistência de dados com H2 (In-memory).
- *Bean Validation*: Garantia da integridade dos dados.

---

## Funcionalidades Principais

- *Autenticação Segura*: Login com persistência de sessão e renovação automática de acesso via Refresh Token.
- *Gestão de Viagens*: Listagem, criação e detalhamento de viagens.
- *Fluxo de Status Inteligente*: Botões de ação que mudam conforme o estado da viagem (Agendada -> Em Andamento -> Concluída).
- *UX Resiliente*: 
  - Tratamento de erros de rede com botões de "Tentar Novamente".
  - Timeouts configurados para evitar travamentos de interface.
  - Indicadores visuais de status com ícones e cores dinâmicas.
- *Regras de Negócio no Backend*: Validação rigorosa de datas (Data de volta nunca anterior à de ida) e transições de status.

---

## Desafios Técnicos e Aprendizados

Este projeto foi além do CRUD básico, focando em boas práticas de engenharia de software:

1. *Segurança e Refresh Token*: Implementação de um fluxo de autenticação que renova o token de acesso de forma transparente para o usuário, garantindo segurança sem prejudicar a usabilidade.
2. *Async Gaps no Flutter*: Uso de referências capturadas e checagens de `mounted` para evitar crashes em operações assíncronas de rede.
3. *Tratamento de Exceções Global*: No backend, o tratamento de erros está centralizado em um `GlobalExceptionHandler`, enviando mensagens amigáveis e estruturadas para o frontend.
4. *Resiliência de Rede*: Implementação de timeouts e capturas de erro de conexão para garantir que o app nunca fique "preso" em um carregamento infinito se o servidor estiver offline.

---

## Como Rodar o Projeto

1. *Back-end*:
#### Clone o repositório e entre na pasta do servidor
`./mvnw spring-boot:run`
A API estará disponível em `http://localhost:8080`. 

2. *Front-end*:
#### Na pasta do projeto Flutter, configure o IP do seu servidor no arquivo de configuração
`flutter pub get`
`flutter run`

---

## Desenvolvedora
Thayná Assis - [https://www.linkedin.com/in/thayna-p-p-assis/] [https://github.com/peresthayna]
