# **Order Application**

Uma aplicação construída com **Java 17** e **Spring Boot** que gerencia pedidos e produtos. O projeto utiliza um banco de dados **H2** em memória para armazenar informações de pedidos e produtos.

---

## **Funcionalidades**

- **Pedidos**
    - Criar um pedido com itens (produtos e quantidades).
    - Consultar pedido por ID.
    - Consultar pedidos com filtros dinâmicos (status, datas, produtos).
    - Verificar e evitar duplicação de pedidos.

- **Produtos**
    - Criar um produto.
    - Atualizar informações de um produto.
    - Remover um produto.
    - Listar todos os produtos disponíveis.
    - Consultar produto por ID.

---

## **Tecnologias Utilizadas**

- **Java 17**
- **Spring Boot 3.4**
    - Spring Data JPA
    - Spring Web
- **Banco de Dados**
    - H2 (em memória)
- **Swagger**
    - Documentação interativa da API.
- **Lombok**
    - Simplificação de código com geração automática de getters, setters e construtores.
- **MapStruct**
    - Conversão entre entidades e DTOs.

---

## **Configuração do Banco de Dados H2**

O banco de dados H2 está configurado para rodar em memória e pode ser acessado via console web.

### **Credenciais**
- **URL do Banco:** `jdbc:h2:mem:orderdb`
- **Usuário:** `sa`
- **Senha:** *(vazia)*

### **Acesso ao Console H2**
- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Driver Class:** `org.h2.Driver`
- **JDBC URL:** `jdbc:h2:mem:orderdb`
- **User Name:** `sa`

---

## **Endpoints Disponíveis**

### **Pedidos**

| Método | Endpoint                | Descrição                          |
|--------|--------------------------|------------------------------------|
| POST   | `/pedidos/criar-pedido/` | Criar um novo pedido.              |
| GET    | `/pedidos/{id}`          | Consultar um pedido por ID.        |
| GET    | `/pedidos/filter/`       | Consultar pedidos com filtros.     |

### **Produtos**

| Método | Endpoint                  | Descrição                          |
|--------|----------------------------|------------------------------------|
| POST   | `/produtos/criar-produto/` | Criar um novo produto.             |
| POST   | `/produtos/atualizar-produto/` | Atualizar informações do produto. |
| DELETE | `/produtos/{id}`           | Remover um produto por ID.         |
| GET    | `/produtos/{id}`           | Consultar um produto por ID.       |
| GET    | `/produtos/all/`           | Listar todos os produtos.          |

---

## **Como Executar o Projeto**

1. **Pré-requisitos:**
    - Java 17 instalado.
    - Maven instalado.

2. **Clone o Repositório:**
   ```bash
   git clone https://github.com/seu-usuario/order.git
   cd order
