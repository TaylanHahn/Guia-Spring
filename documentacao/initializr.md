> Guia de Ferramenta 🛠️

## Spring Initializr <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg" width="24" height="24">
### A Porta de Entrada do Universo Spring

O **Spring Initializr** é uma ferramenta web (e também uma API) que gera a estrutura base de uma aplicação **Spring Boot**.   Ele elimina a necessidade de configurar manualmente arquivos de build complexos, permitindo que você foque no **código de negócio desde o primeiro minuto**.

🔗 Acesse em: **https://start.spring.io**

---

### ⚙️ 1. As Opções de Configuração (Lado Esquerdo)

Ao acessar o site, você verá um painel de configuração. Abaixo está o significado de cada opção:

### 📦 Project (Ferramenta de Build)
Define como o projeto será compilado e como as bibliotecas serão baixadas.

- **Maven** ✅ *(Recomendado para iniciantes)*  
  Usa arquivos XML (`pom.xml`).  
  É o padrão mais robusto, estável e documentado do mercado.

- **Gradle**  
  Usa scripts em **Groovy** ou **Kotlin**.  
  Mais rápido e flexível, porém com maior curva de aprendizado.

---

### 💻 Language
- **Java** ✅ *(Escolha padrão)*  
- **Kotlin / Groovy**: alternativas suportadas pela JVM.

---

### 🌱 Spring Boot
Define a versão do framework.

📌 **Regra de Ouro**  
Escolha sempre a versão mais recente que **NÃO contenha**:
- `(SNAPSHOT)`
- `(M1 / RC)`

✅ Exemplo correto:  
Se houver `3.2.0 (SNAPSHOT)` e `3.1.5`, escolha **3.1.5** (versão estável / GA).

---

### 🏷️ Project Metadata
Os “dados de identidade” do seu projeto:

- **Group**  
  Identificador da organização (domínio invertido).  
  Ex: `br.edu.ifrs`

- **Artifact**  
  Nome do projeto e da pasta.  
  Ex: `todo-list`

- **Name**  
  Nome de exibição do projeto.

- **Package Name**  
  Pacote Java raiz (junção de *Group + Artifact*).

---

### 📦 Packaging
- **Jar** ✅ *(Padrão)*  
  A aplicação roda sozinha com servidor embutido.

- **War**  
  Use apenas para servidores externos legados (Tomcat/JBoss antigos).

---

### ☕ Java
Versão do JDK instalada na máquina.

- **Java 17 ou 21** ✅ *(LTS — recomendado)*

---

### 🧩 2. As Dependências (Lado Direito)

Aqui você escolhe as **“peças de LEGO”** do projeto.  
O Initializr garante que todas sejam **compatíveis entre si**.

**Categorias mais usadas**

#### 🌐 Web
- **Spring Web** — Criação de APIs REST e aplicações MVC.

#### 🗄️ SQL / Persistência
- **Spring Data JPA**
- **MySQL Driver**
- **PostgreSQL Driver**
- **H2 Database** (banco em memória para testes)

#### 🎨 Template Engines
- **Thymeleaf** — Geração de HTML no servidor.

#### 🛠️ Developer Tools
- **Spring Boot DevTools**  
  Reinicia automaticamente o servidor ao salvar o código (Hot Reload).

- **Lombok**  
  Reduz código repetitivo (getters, setters, construtores).

---

### ⭐ 3. Principais Benefícios

### 📦 3.1 Gestão Inteligente de Versões (BOM)
O maior trunfo do Spring Boot.

Ao escolher uma versão (ex: `Spring Boot 3.1.x`), o Initializr configura um **BOM (Bill of Materials)**.

✔️ Ele define automaticamente versões compatíveis de:
- Hibernate
- Tomcat
- Jackson
- Outras bibliotecas internas

👉 Você **não precisa definir versões manualmente** no `pom.xml`, evitando o famoso **inferno de dependências**.

---

### 🗂️ 3.2 Estrutura de Pastas Padronizada
O projeto já nasce no padrão da indústria:

```text
src/main/java        → Código-fonte
src/main/resources  → Configurações e arquivos estáticos
src/test/java       → Testes automatizados
````

### 🧠 3.3 Integração com IDEs

Você **não precisa acessar o site manualmente** para criar um projeto Spring Boot.

As principais IDEs já possuem integração direta com o **Spring Initializr**:

- **IntelliJ IDEA**
- **VS Code** *(com Spring Boot Extension Pack)*

Ambas utilizam a **API do Initializr** para gerar o projeto diretamente na IDE, com todas as configurações e dependências escolhidas.

---

### 📝 Resumo — Projeto To-Do List

Configuração ideal no **Spring Initializr**:

- **Project:** Maven  
- **Language:** Java  
- **Spring Boot:** Última versão estável *(ex: 3.2.x)*  
- **Artifact:** `todo-list`

### 📦 Dependências
- Spring Web  
- Spring Data JPA  
- H2 Database  
- Thymeleaf  
- Lombok  
