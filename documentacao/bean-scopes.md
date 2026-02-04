## 🧩 Guia de Escopos de Beans (Bean Scopes)

O Spring controla a criação dos objetos. O escopo é a regra que diz quando criar um novo.

--- 
### 1️⃣ Escopos Core 
> Funcionam em qualquer aplicação

São os dois principais, que existem mesmo que sua aplicação não seja Web.

### 🟡 Singleton (O Padrão Absoluto)

Se você não colocar nenhuma anotação @Scope, o Spring assume que é Singleton.

- ⚙️ **Comportamento**: O Spring cria apenas uma instância do objeto por container (ID). Sempre que você pedir esse Bean (seja em 10 Controllers diferentes), o Spring entregará exatamente a mesma instância.

- ⏳ **Ciclo de Vida**: Nasce quando a aplicação sobe, morre quando a aplicação desliga.

- ✅ **Quando usar**: Em 99% dos casos. Services, Repositories e Components que são Stateless (não guardam dados específicos de um usuário na memória da classe).

- ⚠️ **Perigo**: Nunca guarde dados de estado (ex: private String nomeUsuario) em um Singleton, pois todos os usuários vão sobrescrever e ler a mesma variável.


### 🔵 Prototype

Anotação: `@Scope("prototype")` ou `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`

- ⚙️ **Comportamento**: O Spring cria uma nova instância toda vez que o Bean for solicitado ou injetado. Se 3 classes injetarem esse Bean, haverá 3 instâncias diferentes na memória.

- ✅ **Quando usar**: Quando o objeto precisa guardar estado (Stateful) ou não é Thread-Safe.

- 🧹 **Nota Importante**: O Spring cria o Prototype, mas não gerencia a destruição dele. A limpeza de memória fica por conta do Garbage Collector do Java.

--- 

## 2️⃣ Escopos Web 
> Apenas em Spring Web/MVC

Estes só funcionam se você estiver rodando uma aplicação Web (com `spring-boot-starter-web`).

### 🟢 Request Scope

- 🏷️ **Anotação**: `@RequestScope`

- ⚙️ **Comportamento**: Cria uma instância para cada requisição HTTP única.

- ⏳ **Ciclo de Vida**: Nasce quando o servidor recebe o GET/POST e morre assim que a resposta é devolvida ao navegador.

- 💡 **Exemplo**: Informações de auditoria da requisição atual (IP, User-Agent, ID de rastreamento).

### 🟣 Session Scope

- 🏷️ **Anotação**: `@SessionScope`

- ⚙️ **Comportamento**: Cria uma instância por Sessão de Usuário (HTTP Session). O objeto sobrevive a múltiplas requisições do mesmo usuário, mas é isolado de outros usuários.

- 💡 **Exemplo Clássico**: Carrinho de Compras ou Dados do Usuário Logado. O que eu coloco no meu carrinho não aparece no seu.

### 🟠 Application Scope

- 🏷️ **Anotação**: `@ApplicationScope`

- ⚙️ **Comportamento**: Cria uma instância para todo o ServletContext.

- 🔎 **Diferença do Singleton**: O Singleton é por "Container Spring", o Application é por "Aprovisão Web". Na prática, em Spring Boot moderno, eles são quase idênticos, mas tecnicamente distintos em deploys antigos (WAR).

---

## 3️⃣ Como declarar

Você pode usar a anotação genérica `@Scope` ou as específicas (mais limpas).

```java
// Jeito genérico
@Component
@Scope("prototype")
public class TokenGerador { ... }

// Jeito específico (Recomendado para Web)
@Component
@SessionScope
public class CarrinhoCompras { ... }
````

---

## 4️⃣ O Grande Problema: Injeção de Escopos Diferentes (Proxy)

Aqui é onde a maioria dos desenvolvedores erra. Preste atenção neste cenário:

Você tem um `@Service` (Singleton) e injeta nele um Carrinho de Compras (`@SessionScope`).

- ⚠️ **O problema**: O Singleton é criado apenas uma vez, na inicialização. Ele vai injetar o Carrinho de Compras naquele momento. Resultado: Todos os usuários da aplicação vão compartilhar o mesmo carrinho de compras (o que foi criado quando o servidor subiu), violando a regra da sessão.

- 🛠️ **A Solução: Proxies**: Você precisa dizer ao Spring para injetar um "Proxy" (um intermediário inteligente), e não o objeto real. O Proxy sabe buscar a instância correta da sessão atual quando for chamado.

- ✅ **Como corrigir**: Se você usar as anotações modernas (`@SessionScope`, `@RequestScope`), o Spring Boot já configura o proxy automaticamente.

- ⚙️ **Configuração manual com anotação antiga**: Se usar a anotação antiga `@Scope`, precisa configurar manual:
```java
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS) // O segredo está aqui
public class CarrinhoCompras { ... }
````

---

## 📊 Resumo Visual

| Escopo | Criação | Vida Útil | Uso Ideal |
|---------|----------|------------|------------|
| Singleton | 1 por App | Todo o tempo de execução | Services, Repositories (Stateless). |
| Prototype | N por Injeção | Até perder referência | Beans com estado temporário. |
| Request | 1 por HTTP Request | Milissegundos (Request-Response) | Logs de request, dados de form. |
| Session | 1 por Usuário | Minutos/Horas (Sessão ativa) | Carrinho de compras, User Profile. |

---

## 🛒 Exemplo Prático de Uso (Carrinho de Compras)

Imagine um e-commerce simples.

- 🧩 **Java**

```java
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope // Cada usuário terá sua própria lista separada na memória
public class Carrinho {
    
    private List<String> itens = new ArrayList<>();

    public void adicionar(String item) {
        itens.add(item);
    }

    public List<String> getItens() {
        return itens;
    }
}
````
Se você tirar o `@SessionScope`, o primeiro usuário que adicionar um item fará com que todos os outros usuários vejam aquele item no carrinho deles (porque viraria Singleton).
