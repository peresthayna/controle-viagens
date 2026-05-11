# App Controle de Viagens

Este é o cliente mobile do sistema de Gestão de Viagens, desenvolvido com *Flutter*. O aplicativo oferece uma interface intuitiva para que colaboradores possam gerenciar suas viagens corporativas em tempo real, com foco em alta disponibilidade e usabilidade.

---

## Design & UX

- *Material Design 3*: Utilização dos padrões mais recentes de design do Google.
- *Feedback Visual Dinâmico*:
  - Ícones de decolagem (flight_takeoff) e pouso (flight_land) para facilitar a leitura das datas.
  - Cores semitransparentes nos badges de status para melhor hierarquia visual.
- *Resiliência*: Tratamento completo de estados (Loading, Empty, Error e Retry).

## Arquitetura e Padrões

O projeto segue uma arquitetura baseada em Camadas (Layers) para garantir manutenibilidade:

- *View (Screens/Widgets)*: UI reativa que consome os dados do Provider.
- *Provider (State Management)*: Gerenciamento de estado centralizado, tratando a lógica de negócio e estados de carregamento.
- *Repository*: Camada de abstração de dados que lida com as requisições HTTP e persistência local.
- *Models*: Classes imutáveis com serialização JSON robusta.

## Segurança e Redes

- *JWT Authentication*: Fluxo completo de accessToken e refreshToken.
- *Interceptação de Erros*: Implementação de lógica de auto-refresh caso o token expire durante uma requisição.
- *Secure Storage*: Uso do flutter_secure_storage para proteger dados sensíveis.
- *Network Timeouts*: Configuração de tempos de espera para garantir que o app não trave em redes instáveis.

## Bibliotecas Principais

- *provider*: Gerenciamento de estado.
- *http*: Comunicação REST API.
- *flutter_secure_storage*: Persistência de tokens.
- *intl*: Formatação de datas e moedas.

---

## Como configurar o ambiente

1. Certifique-se de que o Backend está rodando.
2. Descubra o IP da sua máquina na rede local.
3. Em AuthRepository e ViagemRepository, altere o baseUrl:
  - Se estiver no Emulador Android:
    - static const String baseUrl = 'http://10.0.2.2:8080'; //padrão atual
  - Se estiver no dispositivo físico:
    - static const String baseUrl = 'http://192.168.X.X:8080';

4. Execute os comandos:
`flutter pub get` `flutter run`

---

## Diferenciais Técnicos Implementados

- *Tratamento de Async Gaps*: Proteção de context em chamadas assíncronas.
- *Validação de Formulários*: Verificação de campos obrigatórios e lógica de datas antes do envio.
- *UX de Erro*: SnackBars estilizados com behavior: SnackBarBehavior.floating.
