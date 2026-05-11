# viagens-backend

Backend scaffold do desafio técnico AGT — **Controle de Viagens Corporativas**.

---

## Pré-requisitos

- Java 21+
- Maven 3.9+

Sem banco externo — usa H2 em memória, sobe com um comando.

---

## Como rodar

```bash
./mvnw spring-boot:run
```

ou via IDE: executar a classe `ViagemBackendApplication`.

Aguarde a linha no console:

```
Seed concluído — usuário de teste criado: usuario.teste
```

---

## URLs após subir

| Recurso    | URL                                      |
|------------|------------------------------------------|
| Swagger UI | http://localhost:8080/swagger-ui.html    |
| API base   | http://localhost:8080                    |

> **H2 Console** disponível apenas com perfil dev:
> `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`
> → http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:viagens`, usuário: `sa`, senha: vazia)

---

## Credencial de teste

| Campo    | Valor           |
|----------|-----------------|
| username | `usuario.teste` |
| password | `senha123`      |

---

## Endpoints prontos (scaffold)

| Método | Endpoint              | Descrição                        | Auth  |
|--------|-----------------------|----------------------------------|-------|
| POST   | `/auth/login`         | Login — retorna JWT + refresh    | Não   |
| POST   | `/auth/refresh`       | Renova o access token            | Não   |
| GET    | `/dominios/finalidades` | Lista de finalidades           | Sim   |
| GET    | `/dominios/transportes` | Lista de meios de transporte   | Sim   |

---

## ⚠️ O que você precisa implementar

Todos os endpoints de `/viagens` retornam **HTTP 501** até você implementar a lógica.

### Arquivo único a editar:

```
src/main/java/com/agt/viagens/application/service/ViagemServiceImpl.java
```

Implemente os três métodos marcados com `// TODO`:

---

### Método 1 — `listarPorUsuario`

```java
// Endpoint: GET /viagens
// Retorna: 200 com lista de viagens do usuário autenticado
public List<ViagemResponse> listarPorUsuario(Usuario usuario)
```

---

### Método 2 — `criar`

```java
// Endpoint: POST /viagens
// Retorna: 201 Created com a viagem criada
public ViagemResponse criar(CriarViagemRequest request, Usuario usuario)
```

---

### Método 3 — `atualizarStatus`

```java
// Endpoint: PATCH /viagens/{id}/status
// Retorna: 200 com a viagem atualizada
// Retorna: 404 se a viagem não existir ou não pertencer ao usuário
// Retorna: 422 se a transição de status for inválida
public ViagemResponse atualizarStatus(Long id, AtualizarStatusRequest request, Usuario usuario)
```

---

## Recursos disponíveis para implementação

### Acesso ao banco

```java
private final ViagemPort viagemPort;

viagemPort.buscarPorUsuario(usuario);             // SELECT por usuário
viagemPort.buscarPorIdEUsuario(id, usuario);      // SELECT por id + usuário
viagemPort.salvar(viagem);                        // INSERT / UPDATE
```

### Conversão de entidade para response

```java
private final ViagemMapper viagemMapper;

viagemMapper.toResponse(viagem);                  // Viagem → ViagemResponse
```

### Construir uma nova Viagem

```java
Viagem.builder()
    .destino(request.destino())
    .dataIda(request.dataIda())
    .dataVolta(request.dataVolta())
    .finalidade(request.finalidade())
    .transporte(request.transporte())
    .observacoes(request.observacoes())
    .usuario(usuario)
    .build();
```

### Exceções de domínio (já criadas — só lançar)

```java
throw new ViagemNaoEncontradaException(id);                        // → 404
throw new TransicaoStatusInvalidaException(statusAtual, novoStatus); // → 422
```

### Regra de transição de status

```java
// Em StatusViagem.java — use antes de alterar o status
if (!viagem.getStatus().podeTransicionarPara(request.status())) {
    throw new TransicaoStatusInvalidaException(...);
}
```

---

## Estrutura do projeto

```
src/main/java/com/agt/viagens/
│
├── domain/
│   ├── model/          → Viagem, Usuario, RefreshToken, StatusViagem (regras aqui)
│   ├── port/           → interfaces de repositório (ViagemPort, UsuarioPort...)
│   └── exception/      → ViagemNaoEncontradaException, TransicaoStatusInvalidaException
│
├── application/
│   ├── service/        → ViagemService (interface) + ViagemServiceImpl ← VOCÊ IMPLEMENTA AQUI
│   ├── dto/            → CriarViagemRequest, ViagemResponse, AtualizarStatusRequest...
│   └── mapper/         → ViagemMapper
│
├── infrastructure/
│   ├── persistence/    → adapters JPA (implementam os ports)
│   └── security/       → JWT, filtro de autenticação
│
└── presentation/
    └── rest/           → controllers REST (AuthController, ViagemController, DominioController)
```

---

## Fluxo esperado de desenvolvimento

```
1. ./mvnw spring-boot:run          → sobe o servidor
2. POST /auth/login                → obtém o token JWT
3. GET /viagens                    → recebe 501 (ainda não implementado)
4. Implementa ViagemServiceImpl    → os 3 métodos com TODO
5. Reinicia o servidor
6. GET /viagens                    → recebe 200 com a lista
7. POST /viagens                   → cria uma viagem
8. PATCH /viagens/{id}/status      → atualiza o status
```
