# Sistema de Gerenciamento de Projetos e Tarefas

## 📋 Descrição
Sistema desenvolvido em Java para gerenciamento de projetos e tarefas, implementando conceitos de Programação Orientada a Objetos (POO) e persistência em arquivos de texto. O sistema oferece uma interface de linha de comando intuitiva com funcionalidades completas para organização pessoal e profissional.

## 🚀 Como Executar o Sistema

### ✅ Pré-requisitos
- **Java Development Kit (JDK)** versão 8 ou superior instalado
- **Windows, Linux ou macOS** com terminal/prompt de comando

### 📝 Verificar Instalação do Java
Antes de executar, verifique se o Java está instalado:
```bash
java -version
javac -version
```

### 🎯 Passo a Passo para Execução

#### **Passo 1: Navegue até o diretório do projeto**
```bash
# Abra o terminal/prompt e navegue até a pasta src do projeto
cd "C:\Users\Lailson\Desktop\WALBER CARECA\task-managar\src"
```

#### **Passo 2: Compile o projeto**
```bash
# Compilação simples (todos os arquivos são compilados automaticamente)
javac SistemaMain.java
```

#### **Passo 3: Execute o sistema**
```bash
# Execute o sistema
java SistemaMain
```

### 🚀 **Execução Rápida (Alternativa)**
```bash
# Navegue até a pasta do projeto e execute tudo de uma vez
cd "C:\Users\Lailson\Desktop\WALBER CARECA\task-managar\src"
javac SistemaMain.java && java SistemaMain
```

### 🎮 Primeiro Uso do Sistema

1. **Tela Inicial:** O sistema apresentará um menu com opções de login, cadastro ou sair
2. **Criar Conta:** Na primeira execução, escolha "Criar Nova Conta" e cadastre-se
3. **Dados de Exemplo:** O sistema criará automaticamente dados de exemplo
4. **Navegação:** Use os números do menu para navegar pelas funcionalidades

### 🔧 Solução de Problemas

**Erro: `javac: command not found`**
- Solução: Instale o Java JDK e configure a variável de ambiente PATH

**Erro: `Error: Could not find or load main class SistemaMain`**
- Solução: Certifique-se de estar no diretório `src` ao executar o comando

**Erro: Arquivos não encontrados**
- Solução: O sistema criará automaticamente os arquivos de dados na primeira execução

**Erro: `package does not exist`**
- Solução: Execute `javac SistemaMain.java` a partir do diretório `src`

### 👥 **Usuários Pré-cadastrados para Teste**

O sistema vem com usuários padrão para facilitar os testes:

- **Admin**
  - Email: `admin@sistema.com`
  - Senha: `123`


## 📁 Estrutura do Projeto

```
task-managar/
├── README.md                # Documentação do projeto
├── dados/                   # Arquivos TXT para persistência de dados
│   ├── usuarios.txt         # Dados dos usuários
│   ├── projetos.txt         # Informações dos projetos
│   ├── tarefas.txt          # Detalhes das tarefas
│   ├── categorias.txt       # Lista de categorias
│   └── tarefa_categoria.txt # Relacionamentos
└── src/                     # Código fonte principal
    ├── SistemaMain.java     # Classe principal do sistema
    ├── dao/                 # Classes de acesso a dados
    │   ├── CategoriaDAO.java
    │   ├── GerenciadorDados.java
    │   ├── ProjetoDAO.java
    │   ├── TarefaDAO.java
    │   └── UsuarioDAO.java
    ├── model/               # Classes do modelo de dados
    │   ├── Categoria.java
    │   ├── ItemGerenciavel.java
    │   ├── Projeto.java
    │   ├── Tarefa.java
    │   └── Usuario.java
    ├── util/                # Classes utilitárias
    │   ├── ArquivoUtil.java
    │   └── TelaUtil.java
    └── view/                # Interfaces de usuário (menus)
        ├── MenuAutenticacao.java
        └── MenuUsuario.java
```

## 🏗️ Funcionalidades do Sistema

### 📝 **Gerenciamento de Tarefas**
- Criar, editar e excluir tarefas
- Definir prioridades (Alta, Média, Baixa)
- Acompanhar status (Pendente, Em Progresso, Concluída)
- Organizar por categorias
- Filtrar e pesquisar tarefas

### 📊 **Gerenciamento de Projetos**
- Criar e organizar projetos
- Associar tarefas aos projetos
- Acompanhar progresso geral
- Definir datas de início e fim

### 🏷️ **Gerenciamento de Categorias**
- Criar categorias personalizadas
- Organizar tarefas por tipo
- Facilitar busca e filtros

### 📈 **Relatórios e Status**
- Visualizar estatísticas gerais
- Acompanhar produtividade
- Relatórios de progresso

## 🏛️ Arquitetura do Sistema - Classes Principais

### 👤 **Usuario**
- Gerencia autenticação e dados do usuário
- Relacionamento 1:N com ItemGerenciavel
- Atributos: id, nome, email, senha

### 📋 **ItemGerenciavel (Classe Abstrata)**
- Classe base para Projeto e Tarefa
- Atributos comuns: id, titulo, descricao, dataCriacao, status
- Implementa funcionalidades compartilhadas

### 📊 **Projeto**
- Herda de ItemGerenciavel
- Atributos específicos: dataInicio, dataFim
- Relacionamento 1:N com Tarefa
- Agrupa tarefas relacionadas

### ✅ **Tarefa**
- Herda de ItemGerenciavel
- Atributos específicos: dataConclusao, prioridade
- Relacionamento N:N com Categoria
- Unidade básica de trabalho

### 🏷️ **Categoria**
- Organização e classificação de tarefas
- Facilita busca e organização
- Permite agrupamento lógico

## 📦 Padrões de Arquitetura Utilizados

### **MVC (Model-View-Controller)**
- **Model:** Classes de entidade (Usuario, Projeto, Tarefa, Categoria)
- **View:** Interfaces de usuário (MenuAutenticacao, MenuUsuario)
- **Controller:** Lógica de negócio integrada

### **DAO (Data Access Object)**
- **GerenciadorDados:** Centralizador de operações
- **UsuarioDAO, ProjetoDAO, TarefaDAO, CategoriaDAO:** Acesso específico aos dados
- **ArquivoUtil:** Manipulação de arquivos de texto

## 💾 Persistência de Dados

O sistema utiliza arquivos de texto simples (TXT) para armazenamento:
- **Formato:** Campos separados por ponto e vírgula (;)
- **Codificação:** UTF-8 para suporte a caracteres especiais
- **Localização:** Diretório `dados/` na raiz do projeto
- **Backup automático:** Dados salvos a cada operação

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Java SE 8+
- **Paradigma:** Programação Orientada a Objetos (POO)
- **Persistência:** Arquivos de texto (TXT)
- **Interface:** Linha de comando (CLI)
- **Arquitetura:** MVC com DAO Pattern
- **Funcionalidades:** Limpeza de tela, navegação intuitiva

## � **Exemplo Prático de Uso**

### **Executando o Sistema:**
```bash
# 1. Navegue até a pasta src
cd "C:\Users\Lailson\Desktop\WALBER CARECA\task-managar\src"

# 2. Compile
javac SistemaMain.java

# 3. Execute
java SistemaMain
```

### **Primeiro Login:**
1. Escolha "1. Fazer Login"
2. Email: `fff@gmail.com`
3. Senha: `1010`

### **Testando Funcionalidades:**
1. **Criar uma tarefa:** Menu 1 → Opção 1
2. **Listar tarefas:** Menu 1 → Opção 2
3. **Criar um projeto:** Menu 2 → Opção 1
4. **Ver estatísticas:** Menu 4 → Opção 1

## �📄 Licença

Este projeto foi desenvolvido para fins educacionais e demonstração de conceitos de POO em Java.
