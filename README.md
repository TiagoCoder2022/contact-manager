# 📇 Contact Manager

Aplicação de gerenciamento de contatos desenvolvida em Java 21 utilizando Spring Boot e princípios de arquitetura hexagonal (Ports & Adapters).
O sistema permite criar, atualizar, listar e remover contatos, além de gerenciar usuários com autenticação e autorização via JWT.

# ⚙️ Tecnologias Utilizadas
## Linguagem e Frameworks

- Java 21 → linguagem principal.

- Spring Boot → para acelerar o desenvolvimento com módulos:

- Spring Web → construção da API REST.

- Spring Data JPA → integração com banco de dados.

- Spring Security + JWT → autenticação e autorização.

- Flyway → versionamento e migração de banco.

- H2 Database → banco em memória para testes.

## Bibliotecas e Ferramentas de Apoio

- Lombok → redução de boilerplate (getters, setters, construtores).

- Jakarta Validation / Hibernate Validator → validação de dados em requisições.

- JUnit 5 + Mockito → testes unitários.

# 🏗️ Estrutura do Projeto

```com.seunome.contacts
├── domain/                     # 🟢 CAMADA DE DOMÍNIO
│   ├── model/
│   │   ├── Contact.java
│   │   ├── Phone.java
│   │   ├── Relationship.java
│   │   └── User.java
│   └── repository/             # Ports (interfaces) de persistência
│       ├── ContactRepositoryPort.java
│       └── UserRepositoryPort.java
│
├── application/                # 🟡 CAMADA DE APLICAÇÃO
│   ├── auth/
│   │   └── AuthService.java
│   └── contact/
│       └── ContactService.java
│
├── infrastructure/             # 🔴 CAMADA DE INFRAESTRUTURA
│   ├── adapter/
│   │   ├── in/                 # Inbound Adapters (entrada)
│   │   │   └── web/
│   │   │       ├── AuthController.java
│   │   │       └── ContactController.java            
│   │   └── out/                # Outbound Adapters (saída)
│   │       └── persistence/
│   │           ├── jpa/
│   │           │   ├── ContactJpaRepository.java
│   │           │   └── UserJpaRepository.java
│   │           ├── ContactRepositoryPortAdapter.java
│   │           └── UserRepositoryPortAdapter.java
│   └── security/
│       ├── JwtAuthFilter.java
│       ├── JwtService.java
│       └── SecurityConfig.java
│
├── interfaces/                 # DTOs organizados aqui
│   ├── auth/
│   │   └── dto/
│   │       ├── AuthResponse.java
│   │       ├── LoginRequest.java
│   │       └── RegisterRequest.java
│   └── contact/
│       └── dto/
│           ├── ApiMessage.java
│           ├── ContactCreateRequest.java
│           ├── ContactResponse.java
│           ├── ContactUpdateRequest.java
│           └── PhoneDTO.java
│
└── shared/                     # Exceções compartilhadas
    ├── ApiExceptionHandler.java
    ├── BusinessException.java
    ├── JwtAuthenticationEntryPoint.java
    ├── NotFoundException.java
    └── UnauthorizedException.java
```
# 🚀 Como Rodar o Projeto
1. Clone o repositório:
```
git clone https://github.com/TiagoCoder2022/contact-manager.git
```

2. Acesse a pasta do projeto (backend – API):
```
cd contact-manager/api
```

3. Configure o banco de dados no arquivo application.properties ou utilize o H2 em memória (já configurado para testes).
4. Rode o projeto com Maven/Gradle ou via IDE:
```
./mvnw spring-boot:run
```

5. A API estará disponível em:
```
http://localhost:8080
```
# 📌 Endpoints Principais
## Autenticação

- POST /auth/register → Registro de usuário.

- POST /auth/login → Login e obtenção do token JWT.

## Contatos

- POST /contacts → Criar contato.

- GET /contacts → Listar todos os contatos.

- GET /contacts/{id} → Buscar contato por ID.

- PUT /contacts/{id} → Atualizar contato.

- DELETE /contacts/{id} → Remover contato.

# ✅ Testes

Executar testes unitários com:
```
./mvnw test
```
