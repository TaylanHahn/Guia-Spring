> 📚 Documentação

## Spring Security Moderno 🔐

### 🧭 1. Visão Geral da Arquitetura

A principal mudança nesta versão é a transição da Configuração Imperativa (baseada em estender classes) para a Configuração Funcional (baseada em Beans). No Spring Boot 3, não "somos" um configurador de segurança; nós definimos uma série de filtros que o Spring deve aplicar às requisições.

- 🧩 **Abordagem:** Component-based Security.
- 🧱 **Padrão de Design:** Filtros em cadeia (Filter Chain).
- 🧪 **Linguagem de Configuração:** Lambda DSL (uso intensivo de expressões lambda para maior clareza).

---

### 🏛️ 2. Os Pilares da Nova Segurança

#### ❤️ A. Security Filter Chain (O Coração)

Substituindo o antigo `WebSecurityConfigurerAdapter`, o `SecurityFilterChain` é o componente que define as regras de tráfego. Ele funciona como uma lista de verificação para cada requisição que chega à API.

- 🎯 **Request Matchers:** Substituem o antigo `antMatchers`. São mais precisos na identificação de URLs e protegem contra vulnerabilidades de caminho (path traversal).

- 🧩 **Desacoplamento:** Como é um `@Bean`, você pode ter múltiplas cadeias de filtros para diferentes partes da aplicação (ex: uma para a API e outra para o Console de Administração).

#### 👤 B. Autenticação Baseada em Beans

Em vez de sobrescrever métodos de autenticação, agora definimos um `UserDetailsService`.

- 🗂️ **Gerenciamento de Usuários:** O `InMemoryUserDetailsManager` serve para definir credenciais de forma rápida durante o desenvolvimento, sem a necessidade de um banco de dados imediato.

- 🧱 **Imutabilidade:** Os objetos de usuário (`UserDetails`) são criados usando o padrão Builder, garantindo que as credenciais não sejam alteradas após a criação.

#### 🔒 C. Criptografia Obrigatória

No Java 22, o uso de senhas em texto puro (como o antigo `{noop}`) é desencorajado.

- 🔐 **BCrypt:** Utilizamos o `BCryptPasswordEncoder`, que é um algoritmo de hashing adaptável, protegendo o sistema contra ataques de força bruta e dicionário.

---

### ⚙️ 3. Principais Diferenças Técnicas

| Recurso | Modelo Legado (Java 8) | Modelo Moderno (Java 22) |
|----------|--------------------------|---------------------------|
| Estrutura Base | Herança (`extends`) | Composição (`@Bean`) |
| Encadeamento | Método `.and()` | Blocos Lambda (`auth -> ...`) |
| Segurança de Método | `@EnableGlobalMethodSecurity` | `@EnableMethodSecurity` |
| Mapeamento de URL | `antMatchers()` | `requestMatchers()` |
| Configuração CSRF | Habilitada por padrão | Customizável via Lambda |

---

### 🧩 4. Boas Práticas Implementadas

- 🔐 **Princípio do Menor Privilégio:** As rotas são liberadas especificamente (`/`, `/login`, `/swagger-ui/**`), enquanto o restante da aplicação permanece bloqueado por padrão (`anyRequest().authenticated()`).

- 🛡️ **Defesa em Profundidade:** Além das regras globais na classe de configuração, a anotação `@PreAuthorize` nos Controllers serve como uma segunda camada de proteção, garantindo que mesmo que a configuração global falhe, o método individual permaneça seguro.

- 🚫 **Stateless por Natureza:** APIs REST modernas evitam o uso de sessões de servidor (HTTP Session), preparando o terreno para autenticação via JWT no futuro.

---

### ☁️ 5. Contexto de Execução

Esta configuração tira proveito das melhorias de performance da JVM 22 e das novas especificações do Jakarta EE, garantindo que a aplicação esteja pronta para ambientes de nuvem (Cloud Native) e escalabilidade.
