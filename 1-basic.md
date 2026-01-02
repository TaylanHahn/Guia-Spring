# Spring Boot | Básico ☕🌱
Foco em fundamentos, arquitetura de injeção de dependências e exposição REST básica.

## 1. Conceitos Fundamentais 💻

- `Inversion of Control (IoC) & Application Context`
  
  O Spring gerencia o ciclo de vida dos objetos *(Beans)*. Você não faz `new Objeto()`, você pede ao Spring uma instância gerenciada.

- `Dependency Injection (DI)`

   É o padrão de projeto onde as dependências de um objeto são fornecidas externamente *(pelo Spring)* em vez de o objeto criá-las.

- `Auto-configuration`

   O Spring Boot analisa o classpath *(bibliotecas adicionadas)* e configura a aplicação automaticamente *(ex: se vê o driver do MySQL, tenta configurar um DataSource)*.

- `Embedded Server`

   O **Tomcat** *(ou Jetty/Undertow)* vem embutido no **JAR**. A aplicação não é "deployada" em um servidor, ela é o servidor.

---

## 2. Anotações de Configuração e Boot ⚙️
> Contexto: Backend — Uso: Obrigatório/Core

### `@SpringBootApplication`
- 🧩 **Significado** ➜ A anotação mãe. É um atalho que agrupa ***@Configuration, @EnableAutoConfiguration e @ComponentScan***.
- 🧠 **Função** ➜ Marca a classe principal que inicia a aplicação.
- 🛠️ **Quando usar?** ➜ Apenas uma vez, na classe que contém o método main.
- ✅ **Boas Práticas** ➜ Mantenha essa classe no pacote raiz *(root package)* para que o ComponentScan encontre automaticamente todos os componentes nos subpacotes.

### `@Configuration`
- 🧩 **Significado** ➜ Indica que uma classe é uma fonte de definições de *Beans*.
- 🧠 **Função** ➜ Substitui os antigos arquivos *XML* do Spring. Permite configurar o sistema via código Java.
- 🛠️ **Quando usar?** ➜ Para configurar segurança, MVC, clientes HTTP externos ou instanciar classes de terceiros que não têm anotações do Spring.

### `@Bean`
- 🧩 **Significado** ➜ Indica que um método produz um objeto *(bean)* a ser gerenciado pelo Spring.
- 🧠 **Função** ➜ Ensina ao Spring como criar uma instância de uma classe.
- 🛠️ **Quando usar?** ➜ Geralmente dentro de classes ***@Configuration***. Essencial quando você precisa injetar uma classe de uma biblioteca externa que você não pode editar para colocar @Component.
  
---

## 3. Estereótipos (*Stereotypes*) e Injeção 💉
> Contexto: Backend — Uso: Muito Comum

O Spring usa estereótipos para identificar a responsabilidade de cada classe. Tecnicamente todos são ***@Component***, mas a semântica importa.

| Anotação     | Significado / Função                         | Quando usar                                                                 | Boas Práticas                                                                 |
|--------------|----------------------------------------------|-----------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| ***@Component***   | Componente genérico.                          | Quando a classe é gerenciada pelo Spring, mas não se encaixa em camadas específicas (ex: utilitário de data). | Evite se houver um estereótipo mais específico.                                |
| ***@Service***     | Camada de Regra de Negócio.                   | Em classes que contêm a lógica principal, validações e orquestração de chamadas. | Deve ser o local onde as transações de banco de dados acontecem (nível intermediário). |
| ***@Repository***  | Camada de Acesso a Dados (DAO).               | Em classes que acessam banco de dados ou fontes de dados externas.           | Traduz exceções específicas de persistência para exceções do Spring.          |
| ***@Controller***  | Controlador MVC tradicional.                 | Quando você serve páginas HTML (Thymeleaf, JSP) ou precisa de roteamento web clássico. | Para APIs REST, prefira `@RestController`.                                     |

### `@Autowired`
- 🧠 **Função** ⟶ Marca um ponto de injeção de dependência (o Spring buscará um Bean compatível e o inserirá ali).
- ❌ **Evite** ⟶ Injeção por atributo (Field Injection). Ex: `@Autowired private Service service;`. Dificulta testes unitários e esconde dependências.
- ✅ **Recomendado** ⟶ Injeção por Construtor. Garante que o objeto não seja criado em estado inválido e permite campos final (imutabilidade).

Exemplo da Melhor Prática (Construtor):
````java
@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    // @Autowired é opcional no construtor se for o único, mas ajuda na clareza
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }
}
````

---

## 4. Web e REST API (Camada de Entrada) 🔗
> Contexto: Web/Backend — Uso: Muito Comum

### `@RestController`
- 🧩 **Significado** ➜ Combinação de *@Controller + @ResponseBody*.
- 🧠 **Função** ➜ Garante que o retorno dos métodos seja serializado automaticamente para *JSON (ou XML)*, e não interpretado como nome de uma página HTML.
- 🛠️ **Quando usar** ➜ Criação de APIs RESTful.

### `@RequestMapping` *e suas variantes*
- 🧠 **Função** ➜ Mapeia requisições HTTP para métodos Java.
> Variantes Modernas (Use estas):
> - `@GetMapping`: Leitura de dados.
> - `@PostMapping`: Criação de recursos.
> - `@PutMapping`: Atualização completa.
> - `@PatchMapping`: Atualização parcial.
> - `@DeleteMapping`: Remoção.
- ✅ **Boas Práticas** ➜ Defina um caminho base na classe com @RequestMapping("/api/recurso") e caminhos específicos nos métodos.

### Manipulação de Dados da Requisição
A forma como você captura os dados que chegam do Frontend/Cliente.

| Anotação        | Onde busca o dado?                     | Exemplo de URL / JSON                                   | Quando usar                                  |
|-----------------|----------------------------------------|----------------------------------------------------------|----------------------------------------------|
| ***@PathVariable***  | Na própria URL (caminho).               | `/users/15` <br> _(15 é o dado)_                          | Identificação de recursos (IDs).             |
| ***@RequestParam***  | Nos parâmetros da query.                | `/users?status=ativo` <br> _(status é o dado)_            | Filtros, paginação e ordenação.              |
| ***@RequestBody***   | No corpo da requisição (JSON).          | `{ "nome": "Ana" }`                                      | Envio de objetos complexos (POST / PUT).     |

## 5. Configuração Externa ⚙️
> Contexto: DevOps/Backend — Uso: Comum

### `@Value`
- 🧠 **Função** ➜ Injeta valores definidos no arquivo application.properties ou application.yml.
- 📦 **Sintaxe** ➜ `@Value("${nome.propriedade}")`
- 🛠️ **Quando usar** ➜ Para URLs de APIs externas, chaves de API, flags de funcionalidades, caminhos de diretórios.
- ✅ **Boas Práticas** ➜ Se houver muitas propriedades relacionadas, prefira @ConfigurationProperties (veremos no nível Intermediário) para type-safety.

