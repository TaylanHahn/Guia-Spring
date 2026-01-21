> Guia de Ferramenta 🛠️

## 🧱 Maven & Gradle 🐘

### ❓ O que eles resolvem?

Antigamente, para criar um projeto Java, você precisava:

- Baixar dezenas de arquivos `.jar` de sites aleatórios
- Colocar tudo em uma pasta `lib`
- Configurar o **Classpath** manualmente
- Resolver dependências transitivas “na unha” (descobrir e baixar uma por uma)

### ⚙️ Como Maven e Gradle ajudam?

Maven e Gradle automatizam todo esse processo, facilitando o desenvolvimento e padronizando builds.

### 📦 Gestão de Dependências
Você pede uma dependência, por exemplo **Spring Web**, e a ferramenta automaticamente baixa:
- Spring Web  
- Tomcat  
- Jackson  
- Logback  
- Outras dependências necessárias  

Tudo isso sem configuração manual.

### 🔄 Ciclo de Vida (Build Lifecycle)
Com um único comando, Maven ou Gradle conseguem:

- Limpar o projeto
- Compilar o código
- Executar testes unitários
- Gerar o arquivo final (`.jar` ou `.war`)

---

## 🧱 Apache Maven (o “Clássico”)

🗓️ **Lançado em 2004**, o Maven se tornou o **padrão de mercado** para projetos Java corporativos.

### 🧠 Filosofia: *Convenção sobre Configuração*

O Maven é **opinativo**.  
Ele espera que você siga uma estrutura padrão, por exemplo:

- Código-fonte em `src/main/java`
- Testes em `src/test/java`

Seguindo essa convenção, o projeto funciona com **configuração mínima ou até zero**.

### 📄 O Arquivo: `pom.xml`

Toda a configuração do projeto reside no **Project Object Model (POM)**.

- Formato: **XML**
- Características:
  - Verboso
  - Muito legível
  - Altamente estruturado

É nele que você define:
- Dependências
- Plugins
- Versão do Java
- Metadados do projeto

### 🗂️ Estrutura Básica de um Projeto Maven
````XML
<project>
    <groupId>com.exemplo</groupId>
    <artifactId>todo-list</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
</project>
````

### ⌨️ Comandos Essenciais (Terminal)

- **`mvn clean`**  
  🧹 Apaga a pasta `target`, onde ficam os arquivos compilados.

- **`mvn install`**  
  🔨 Compila o projeto, executa os testes e salva o arquivo `.jar` no repositório local (`.m2`).

- **`mvn spring-boot:run`**  
  🚀 Sobe a aplicação Spring Boot diretamente pelo Maven.

---

## 🐘 Gradle (o “Moderno”)

🚀 **Lançado em 2007**, o Gradle ganhou grande destaque com o **Android (Google)** e se consolidou pelo foco em **performance** e **flexibilidade**.

### 🧠 Filosofia: *Scriptável e Incremental*

No Gradle, o **build é tratado como código**. As configurações e tarefas são definidas usando uma linguagem de script:

- **Groovy DSL**
- **Kotlin DSL**

Isso permite muito mais liberdade e personalização do processo de build.

### ⚡ Build Incremental

O Gradle utiliza build **incremental**:

- Se você altera apenas **uma classe**, ele recompila **somente o necessário**
- O Maven tende a recompilar tudo
- Em projetos grandes, isso torna o Gradle **significativamente mais rápido**

### 📄 O Arquivo: `build.gradle`

- Arquivo principal de configuração do projeto
- Sintaxe mais **enxuta e legível** que XML
- Fácil de escrever e manter

### 🗂️ Estrutura Básica (Groovy DSL)
````Groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.0'
}

group = 'com.exemplo'
version = '0.0.1-SNAPSHOT'

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
````

## ⚔️ Comparativo Prático: Maven vs Gradle

| Característica | Maven | Gradle |
|---------------|-------|--------|
| 📄 Arquivo de Configuração | `pom.xml` (XML) | `build.gradle` (Groovy / Kotlin) |
| 📖 Legibilidade | Verboso, mas padronizado e fácil de entender visualmente | Conciso, porém pode ficar complexo com muita lógica de script |
| ⚡ Performance | Mais lento em projetos grandes (recompila muito) | Mais rápido (builds incrementais e cache de build) |
| 📈 Curva de Aprendizado | Baixa — aprendeu um `pom.xml`, sabe usar todos | Média/Alta — flexível, mas pode gerar bagunça |
| 🏢 Uso no Mercado | Rei absoluto no backend enterprise (bancos, seguradoras) | Padrão no Android e crescente em microsserviços e startups |


**Cenário de Exemplo > Adicionando o Lombok**
Veja a diferença de sintaxe para adicionar a dependência que vimos no guia anterior.

- No Maven (pom.xml):
````XML
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
````

- No Gradle (build.gradle):
````Groovy
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
````

---

## 🧩 O Segredo: o *Wrapper* (`mvnw` e `gradlew`)

Ao criar um projeto Spring Boot, você vai notar arquivos como **`mvnw`** (Maven Wrapper) ou **`gradlew`** (Gradle Wrapper).

### ❓ Para que servem?

Eles permitem executar o projeto **sem precisar ter Maven ou Gradle instalados** na máquina.

O funcionamento é simples:
- O script verifica a versão correta da ferramenta
- Faz o download automaticamente (se necessário)
- Executa o comando usando essa versão

Tudo isso acontece de forma transparente para o desenvolvedor.

### ✅ Como usar (recomendado)

Em vez de usar os comandos globais:

- ❌ `mvn clean install`
- ❌ `gradle build`

Use o *wrapper* do projeto:

- ✔️ `./mvnw clean install`
- ✔️ `./gradlew build`

### 🔒 Por que isso é importante?

- Garante que **todo o time** use exatamente a mesma versão da ferramenta
- Evita erros de ambiente (“na minha máquina funciona”)
- Facilita builds em **CI/CD** e servidores de produção
