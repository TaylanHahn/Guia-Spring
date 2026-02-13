> 📚 Documentação
## Spring Security Legado (Java 8)

Este projeto demonstra a implementação clássica de segurança em APIs REST utilizando o ecossistema Spring Boot 2.x. Na época, a configuração era baseada fortemente em herança, onde estendíamos classes base do framework para customizar o comportamento.

### 🧭 1. O Contexto Tecnológico

- 🧩 **Linguagem:** Java 8.
- 🧩 **Framework:** Spring Boot 2.x.
- 🧩 **Abordagem:** Segurança baseada em Configurações Imperativas (Herança de Classe).
- 🎯 **Objetivo:** Proteger endpoints com base em perfis de acesso (Roles) armazenados em memória.

---

### 🧱 2. Componentes Principais e Anotações

Abaixo, os "pilares" que fazem essa segurança funcionar no código fornecido:

| Anotação / Classe | Função no Sistema |
|-------------------|-------------------|
| @EnableWebSecurity | Ativa a integração do Spring Security com o Spring MVC. Sem ela, as configurações não são aplicadas. |
| WebSecurityConfigurerAdapter | A classe base "mãe". Ela fornecia métodos `configure` que o desenvolvedor sobrescrevia para definir regras. |
| @EnableGlobalMethodSecurity | Habilita a segurança a nível de método (permite usar `@PreAuthorize` no Controller), embora no código atual ela esteja sem parâmetros. |
| antMatchers() | Define os padrões de URL que serão protegidos ou liberados. Utiliza a sintaxe Ant path (ex: `/**`). |
| {noop} | Indica ao Spring que a senha está em texto puro (No Operation), ou seja, não há criptografia (comum apenas em testes). |

---

### 🧬 3. Anatomia da Configuração (WebSecurityConfig.java)

Neste arquivo, o professor dividiu a segurança em duas frentes: Autorização (o que pode ser feito) e Autenticação (quem você é).

### 🛂 A. Controle de Acesso (Autorização)

No método `configure(HttpSecurity http)`, as regras são lidas de cima para baixo:

- 🌍 **Rotas Públicas:** `/` e `/login` são liberadas para qualquer um via `.permitAll()`.

- 🔐 **Restrição por Role:** Apenas quem tem a role MANAGERS acessa `/managers`.

- 👥 **Acesso Compartilhado:** Tanto USERS quanto MANAGERS acessam `/users`.

- ⛔ **Bloqueio Total:** Qualquer outra rota não especificada exige que o usuário esteja logado (`.authenticated()`).

- 🧩 **Interface:** O `.formLogin()` habilita aquela página de login padrão do Spring que aparece no navegador.

### 👤 B. Gestão de Usuários (Autenticação)

No método `configure(AuthenticationManagerBuilder auth)`, os usuários são criados "no código":

- 🧩 **Usuários em memória:** Cria-se um usuário `user` e um `admin`.

- 🏷️ **Prefixo automático:** O Spring Security automaticamente adiciona o prefixo ROLE_ internamente. Portanto, ao definir `.roles("USERS")`, o Spring entende como `ROLE_USERS`.

---

### 🎮 4. O Controller Protegido (WelcomeController.java)

O Controller é simples, mas serve para validar as regras:

- 🌍 **welcome():** Rota raiz, acessível a todos.

- 👥 **users():** Protegida. O Spring verifica se o usuário logado possui a Role USERS ou MANAGERS antes de permitir a entrada.

- 🛡️ **managers():** A "sala VIP". Só entra quem for MANAGERS.

---

### ⚠️ 5. Pontos de Atenção para Sistemas Legados

Se você encontrar esse código em uma empresa hoje, saiba que:

- 🧱 **Herança Obrigatória:** Se você apagar o `extends WebSecurityConfigurerAdapter`, o código para de compilar.

- 🔒 **Rigidez:** Para adicionar novos usuários, você precisa reiniciar a aplicação (já que estão "hardcoded" no método de configuração).

- 📏 **Ordem Importa:** Se você colocar `.anyRequest().authenticated()` no topo da lista, ele vai ignorar todos os `.permitAll()` que vierem abaixo.

---

## 💡 Resumo do Fluxo

O usuário tenta acessar `/users` ➡️ O Spring Security intercepta ➡️ Verifica se há sessão ➡️ Se não, redireciona para o formulário de login ➡️ Após o login, verifica se o usuário tem a "etiqueta" (Role) necessária ➡️ Libera ou nega o acesso.
