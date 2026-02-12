> Resumo ✏️
# ☕🌱 | Avançado
Foco: Segurança (Security), Testes Automatizados, Performance (Async/Cache) e Observabilidade.

## 🛡️ 1. Segurança e Autenticação (Spring Security)

Contexto: Segurança Avançada | Uso: Obrigatório para Proteção de Dados

O Spring Security funciona através de uma Cadeia de Filtros (Filter Chain). Imagine que antes da requisição chegar no seu Controller, ela precisa passar por vários portões de segurança. Se falhar em um, é rejeitada imediatamente.

### 🧩 1.1. Arquitetura: As Peças do Quebra-Cabeça

Para implementar segurança real com banco de dados, você precisa entender 3 interfaces principais. O Spring não sabe como é a sua tabela Usuario, então você precisa "ensinar" a ele.

| Interface | Função | Quem implementa? |
|------------|----------|-------------------|
| UserDetails | É o contrato de "Usuário" que o Spring entende. Define métodos como `getPassword()`, `getUsername()`, `isAccountNonExpired()`. | Sua Entidade Usuario ou uma classe Wrapper/Adapter. |
| UserDetailsService | É o serviço que sabe buscar o usuário no banco. Tem um único método: `loadUserByUsername(String login)`. | Seu AuthenticationService. |
| PasswordEncoder | Define como a senha é criptografada. Nunca guarde senhas em texto puro. | Geralmente usamos o `BCryptPasswordEncoder`. |

### 🔐 1.2. Fluxo de Autenticação Stateless (JWT)

Em APIs REST modernas, não mantemos sessão no servidor (memória). O "crachá" de acesso fica com o cliente.

- 🔑 **Login:** O usuário envia user/pass. O servidor valida. Se OK, gera um Token JWT (assinado com uma chave secreta) e devolve.

- 📩 **Requisições Seguintes:** O cliente envia o token no Header: `Authorization: Bearer abc123xyz...`

- 🧪 **O Filtro Mágico:** Criamos um filtro (`OncePerRequestFilter`) que intercepta toda requisição, abre o token, valida a assinatura e diz ao Spring: "Este usuário é o João".

### 🧩 1.3. A Configuração (SecurityFilterChain) - Atualizado Spring Boot 3

A sintaxe mudou. Esqueça o `extends WebSecurityConfigurerAdapter`. Agora tudo é via `@Bean` e Lambda DSL.

- 🧩 **Java**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter; // Nosso filtro de token criado manualmente

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF (inútil para API REST)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sem sessão/cookies
            .authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.POST, "/login").permitAll() // Login é público
                .requestMatchers(HttpMethod.POST, "/users").permitAll() // Cadastro é público
                .requestMatchers("/admin/**").hasRole("ADMIN") // Só Admin
                .anyRequest().authenticated() // O resto exige login
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Insere nosso filtro antes do padrão
            .build();
    }

    @Bean // Necessário para injetar o AuthenticationManager no Controller de Login
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean // Define a criptografia (Hash) da senha
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
````

### 🔐 1.4. Tratamento de Senhas (Criptografia)

Regra de Ouro: Senhas no banco de dados devem ser Hashes irreversíveis.

- ❌ **Errado:** Salvar "123456".
- ✅ **Certo:** Salvar $2a$10$wS.... (Resultado do BCrypt).

Como usar:

- 🧩 **Ao criar usuário:** `user.setSenha(passwordEncoder.encode(dto.senha()));`

- 🧩 **Ao logar:** O Spring faz a comparação automaticamente usando o método `matches()`.

### 🧠 1.5. O Contexto de Segurança (SecurityContextHolder)

Se você precisar saber quem está logado em qualquer lugar do código (sem passar por parâmetro), o Spring guarda isso numa ThreadLocal.

- 🧩 **Java**

```java
// Em qualquer Service ou Componente
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String usuarioLogado = auth.getName(); // Pega o username do Token
````

### 🌐 1.6. CORS (Cross-Origin Resource Sharing)

Alerta de Erro Comum: Se seu Front-end (Vue/React) rodar na porta 3000 e o Spring na 8080, o navegador bloqueia a requisição. Você precisa configurar o CORS no Spring Security.

- 🧩 **Adicione no SecurityFilterChain:**

```java
.cors(cors -> cors.configurationSource(request -> {
    var corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    corsConfig.setAllowedHeaders(List.of("*"));
    return corsConfig;
}))
````

### 🔒 1.7. Anotações de Método (Segurança Fina)

1. Além da configuração global, você pode proteger métodos específicos.

2. Habilite na classe main ou config: `@EnableMethodSecurity(securedEnabled = true)`

Use nos métodos:

| Anotação | Uso | Exemplo |
|-----------|------|----------|
| @PreAuthorize | O mais poderoso. Aceita SpEL (Spring Expression Language). | `@PreAuthorize("hasRole('ADMIN') or hasAuthority('GERENTE')")` |
| @PostAuthorize | Executa o método, mas decide se retorna o resultado ou lança erro. | `@PostAuthorize("returnObject.owner == authentication.name")` (Só retorna se o dono do dado for quem tá logado). |


## 🛡️ Resumo Visual do Fluxo Spring Security

Request chega ➔ 2. SecurityFilter (JWT) valida token ➔ 3. SecurityContext é preenchido ➔ 4. AuthorizationFilter checa permissões (hasRole) ➔ 5. Controller executa.

- ❌ **Se o passo 2 falhar (token inválido):** retorna 403 Forbidden.
- ⛔ **Se o passo 4 falhar (usuário sem permissão):** retorna 403 Forbidden.
- 🚫 **Se não enviar token:** retorna 401 Unauthorized.

---

## 2. Testes Automatizados (Testing) 🧪
> Contexto: Qualidade de Código — Uso: Profissional

O Spring Boot facilita testes de integração que sobem o contexto da aplicação (simulam o servidor rodando).

| Anotação           | Contexto                  | Função                                                                 | Diferença Chave                                                                 |
|--------------------|---------------------------|------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| ***@SpringBootTest***    | Teste de Integração       | Carrega todo o contexto da aplicação (banco, configurações e beans).   | Mais lento, porém testa o fluxo real completo da aplicação.                      |
| ***@WebMvcTest***        | Teste de Fatia (Slice)    | Carrega apenas a camada Web (Controllers).                              | Rápido; não carrega `Service` nem `Repository`.                                  |
| ***@MockBean***          | Mocking (Simulação)       | Cria um mock de um Bean e o injeta no contexto do Spring.               | Essencial para isolar camadas (ex: testar o Controller simulando o Service).     |
| ***@ActiveProfiles***    | Configuração              | Define qual perfil será utilizado durante o teste (ex: `"test"`).      | Útil para usar banco H2 em memória ou configurações específicas de teste.        |

**Cenário Típico de Teste de Controller:**
````java
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired MockMvc mockMvc; // Simula chamadas HTTP
    @MockBean UsuarioService service; // Simula a lógica de negócio

    @Test
    void deveRetornarSucesso() throws Exception {
        // Arrange (Preparação)
        when(service.buscarPorId(1L)).thenReturn(new UsuarioDTO(...));

        // Act & Assert (Ação e Verificação)
        mockMvc.perform(get("/usuarios/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nome").value("Teste"));
    }
}
````

---
## 3. Performance e Assincronismo⚡
> Contexto: Otimização — Uso: Cenários de Alta Carga

Não bloqueie a thread principal do usuário com tarefas lentas (envio de e-mail, geração de relatórios pesados).

### `@Async` e `@EnableAsync`
- 🧠 **Função** ➜ Executa o método em uma thread separada (background). O Controller responde imediatamente ao usuário enquanto o processo roda no fundo.
- 🧪 **Requisito** ➜ Adicionar @EnableAsync na classe main/config.
- ✅ **Boas Práticas** ➜ Métodos @Async não devem retornar valores diretamente (use CompletableFuture ou void).

### `@Cacheable` e `@EnableCaching`
- 🧠 **Função** ➜ Armazena o retorno de um método em cache (Redis, memória, etc.). Na próxima chamada com os mesmos parâmetros, o método não é executado; o valor é retornado do cache.

**Exemplo:**
````java
@Cacheable("produtos") // Nome do cache
public List<Produto> listarTodos() {
    return repository.findAll(); // Só executa se não estiver no cache
}
````
> ⚠️ *Atenção: Lembre-se de usar @CacheEvict para limpar o cache quando os dados forem atualizados.*

### `@Scheduled`
- 🧠 **Função** ➜ Executa métodos automaticamente em intervalos definidos (Cron Jobs).
- 🔨 **Uso** ➜ Relatórios noturnos, limpeza de banco de dados.

Exemplo: `@Scheduled(cron = "0 0 0 * * ?")` (Meia-noite todo dia).

---

## 4. Gerenciamento de Ambientes (Profiles) 👤
> Contexto: DevOps — Uso: Essencial

Nunca use configurações de Produção em Desenvolvimento.

### `@Profile`
- 🧠 **Função** ➜ Indica que um Bean ou Configuração só deve ser carregado em um perfil específico.
- 🔨 **Uso** ➜ Ter um Bean de envio de e-mail real para "prod" e um Bean que apenas loga no console para "dev".

- **Configuração via Properties** ~ Crie arquivos separados:

`application-dev.properties` (Banco local, logs verbose)

`application-prod.properties` (Banco nuvem, logs error)

No `application.properties` principal, ative: `spring.profiles.active=dev`

---

## 5. Observabilidade (Actuator) 👀
> Contexto: Operações/SRE — Uso: Monitoramento

Como saber se sua aplicação está viva e saudável em produção?

- 🧩 **Dependência** ➜ `spring-boot-starter-actuator`
- 🪄 **Endpoints Mágicos** ➜ O Spring expõe URLs nativas para monitoramento.
  - `/actuator/health`: Status da aplicação (UP/DOWN) e de dependências (Banco, Disk Space).
  - `/actuator/metrics`: Métricas detalhadas (uso de memória, CPU, requisições HTTP).
  - `/actuator/loggers`: Permite mudar o nível de log (DEBUG/INFO) em tempo de execução sem reiniciar o app.

--- 
### Resumo visual geral 🧠

Imagine sua aplicação Spring Boot como uma cebola em camadas.🧅
Aqui está onde cada parte do nosso guia se encaixa:

- **Núcleo (Infra)** ⟶ *ApplicationContext, Profiles, Actuator*.
- **Dados (Repository)** ⟶ *JPA, Hibernate, Transactions*.
- **Lógica (Service)** ⟶ `@Service`, *Async, Caching, Regras de Negócio*.
- **Interface (Web)** ⟶ *RestController, DTOs, Validation, ExceptionHandling*.
- **Borda (Segurança)** ⟶ *Spring Security, JWT Filter, CORS.*
