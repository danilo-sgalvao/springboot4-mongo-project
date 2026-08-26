# Workshop Mongo — API REST de Blog

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-8.0-47A248?logo=mongodb&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue)

API REST de um blog simples — usuários, posts e comentários — construída com **Spring Boot 4** e **MongoDB**.
O objetivo do projeto é exercitar modelagem de dados em banco NoSQL orientado a documentos, onde as decisões
de **aninhar** (comentários dentro do post) ou **referenciar** (`@DBRef` dos posts no usuário) mudam
completamente o desenho da API.

> Projeto de estudo baseado no workshop de MongoDB do curso de Java do Nélio Alves, **migrado do Spring Boot 2
> para o Spring Boot 4 / Java 25** e estendido com infraestrutura de execução própria.

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 25 (LTS) |
| Framework | Spring Boot 4.1.0 (`spring-boot-starter-webmvc`) |
| Persistência | Spring Data MongoDB |
| Banco | MongoDB 8.0 |
| Build | Maven (wrapper incluso) |
| Infra local | Docker Compose (MongoDB + Mongo Express) |

---

## Como rodar

**Pré-requisitos:** Docker e JDK 25.

```bash
git clone git@github.com:danilo-sgalvao/springboot4-mongo-project.git
cd springboot4-mongo-project

docker compose up -d      # sobe MongoDB (27017) e Mongo Express (8081)
./mvnw spring-boot:run    # sobe a API (8080)
```

A API fica em **http://localhost:8080** e o Mongo Express (interface web do banco) em
**http://localhost:8081**.

Na inicialização, a classe [`Instantiation`](src/main/java/com/danilogalvao/workshopmongo/config/Instantiation.java)
limpa as coleções e recria a massa de teste (3 usuários, 2 posts, 3 comentários), então a API já sobe com dados
prontos para consulta.

Para derrubar tudo:

```bash
docker compose down        # mantém os dados no volume
docker compose down -v     # apaga também os dados
```

### Configuração

A URI do banco é lida da variável de ambiente `MONGODB_URI`, com fallback para a instância local:

```properties
spring.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/workshop_mongo}
```

Para apontar para um MongoDB Atlas ou outro servidor, basta exportar a variável:

```bash
export MONGODB_URI="mongodb+srv://usuario:senha@cluster.mongodb.net/workshop_mongo"
```

---

## Endpoints

### Usuários

| Método | Rota | Descrição | Resposta |
|---|---|---|---|
| `GET` | `/users` | Lista todos os usuários | `200` · `UserDTO[]` |
| `GET` | `/users/{id}` | Busca usuário por id | `200` · `UserDTO` · `404` se não existir |
| `POST` | `/users` | Cria um usuário | `201` + header `Location` |
| `PUT` | `/users/{id}` | Atualiza nome e e-mail | `204` |
| `DELETE` | `/users/{id}` | Remove um usuário | `204` |
| `GET` | `/users/{id}/posts` | Lista os posts do usuário | `200` · `Post[]` |

### Posts

| Método | Rota | Descrição | Resposta |
|---|---|---|---|
| `GET` | `/posts/{id}` | Busca post por id | `200` · `Post` · `404` se não existir |
| `GET` | `/posts/titlesearch?text=` | Busca posts cujo **título** contenha o texto | `200` · `Post[]` |
| `GET` | `/posts/fullsearch?text=&minDate=&maxDate=` | Busca o texto no título, no corpo **e nos comentários**, dentro de um intervalo de datas | `200` · `Post[]` |

O parâmetro `text` é opcional (default vazio = retorna tudo). As datas usam o formato `yyyy-MM-dd`; se omitidas
ou inválidas, assumem `1970-01-01` e a data atual.

### Exemplos

```bash
# listar usuários
curl http://localhost:8080/users

# criar usuário
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Joao Silva","email":"joao@gmail.com"}'

# posts de um usuário
curl http://localhost:8080/users/<id>/posts

# busca por título
curl "http://localhost:8080/posts/titlesearch?text=viagem"

# busca completa com intervalo de datas
curl "http://localhost:8080/posts/fullsearch?text=viagem&minDate=2018-01-01&maxDate=2018-12-31"
```

Resposta de `GET /posts/{id}`:

```json
{
  "id": "6621f0c9e4b0a1b2c3d4e5f6",
  "date": "2018-03-21T00:00:00",
  "title": "partiu viagem",
  "body": "vou viajar para sao paulo",
  "author": { "id": "6621f0c9e4b0a1b2c3d4e5f0", "name": "Maria Brown" },
  "comments": [
    {
      "text": "Boa viagem!",
      "date": "2018-03-21",
      "author": { "id": "6621f0c9e4b0a1b2c3d4e5f1", "name": "Alex Green" }
    }
  ]
}
```

### Erros

Recursos inexistentes retornam `404` com um corpo padronizado, produzido pelo
[`ResourceExceptionHandler`](src/main/java/com/danilogalvao/workshopmongo/resources/exception/ResourceExceptionHandler.java):

```json
{
  "timestamp": 1774521600000,
  "status": 404,
  "error": "Nao encontrado",
  "message": "Post not found",
  "path": "/posts/999"
}
```

---

## Modelagem

O ponto central do projeto é como as duas relações foram resolvidas de formas diferentes:

- **Post → autor e Post → comentários** são **objetos aninhados** (`AuthorDTO`, `CommentDTO` gravados dentro do
  documento do post). Comentários não existem sem o post e quase sempre são lidos junto com ele, então aninhar
  evita joins. Do autor só se guarda `id` e `name` — uma projeção, não a entidade inteira.
- **User → posts** é uma **referência** (`@DBRef(lazy = true)`). Um post é uma entidade própria, com id e
  endpoint próprios, e a lista pode crescer sem limite — aninhar estouraria o documento do usuário.

Essa escolha é o que permite a consulta `fullsearch` procurar dentro de `comments.text` em uma única query,
sem nenhum join.

## Estrutura

```
src/main/java/com/danilogalvao/workshopmongo/
├── config/         Instantiation — carga inicial da base
├── domain/         User, Post — entidades (@Document)
├── dto/            UserDTO, AuthorDTO, CommentDTO — projeções e objetos aninhados
├── repository/     UserRepository, PostRepository (@Query, query methods)
├── resources/       controllers REST
│   ├── exception/  ResourceExceptionHandler, StandardError
│   └── util/       URL — decode de parâmetros e parse de datas
└── services/       UserService, PostService — regras de negócio
    └── exception/  ObjectNotFoundException
```

## Roadmap

- [x] CRUD de usuários, posts com autor e comentários aninhados
- [x] Consultas com query methods e `@Query` (busca por título e busca completa com múltiplos critérios)
- [x] Tratamento de exceções com resposta padronizada
- [x] Ambiente local com Docker Compose
- [ ] Testes com Testcontainers, `@WebMvcTest` e Mockito
- [ ] Integração contínua com GitHub Actions
- [ ] Validação de entrada (Bean Validation) e ampliação do handler de exceções
- [ ] Paginação nas listagens
- [ ] Documentação interativa com OpenAPI / Swagger UI
- [ ] Deploy com Dockerfile + MongoDB Atlas

## Licença

[MIT](LICENSE)
