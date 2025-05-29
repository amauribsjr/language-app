# Language App

Aplicativo de aprendizado de idiomas desenvolvido em Java, utilizando JavaFX para a interface gráfica e SQLite para persistência de dados. Criado com fins educacionais e para compor portfólio.

## 🧠 Objetivo

Este projeto visa oferecer uma plataforma simples para usuários praticarem vocabulário e gramática de um novo idioma, com foco em usabilidade e estrutura modular. É ideal para quem está aprendendo Java, JavaFX e integração com bancos de dados locais.

## 🛠️ Tecnologias Utilizadas

- **Java 21 (17+)**
- **JavaFX** – Interface gráfica
- **SQLite** – Banco de dados local
- **Maven** – Gerenciamento de dependências
- **FXML** – Definição de layouts

## 📁 Estrutura do Projeto

```
language-app/
├── src/
│   └── main/
│       ├── java/
│       │   └── br/
│       │       └── com/
│       │           └── languageapp/
│       │               ├── Main.java
│       │               ├── controllers/
│       │               ├── models/
│       │               └── utils/
│       └── resources/
│           ├── fxml/
│           └── images/
├── pom.xml
└── README.md
```

## 🚀 Como Executar

1. **Pré-requisitos**:
   - Java 17 ou superior
   - Maven instalado
   - JavaFX configurado no classpath

2. **Clone o repositório**:

   ```bash
   git clone https://github.com/amauribsjr/language-app.git
   cd language-app
   ```

3. **Compile e execute**:

   ```bash
   mvn clean javafx:run
   ```

   > Certifique-se de que o JavaFX esteja corretamente configurado no seu ambiente.

## 🧩 Funcionalidades Planejadas

- Cadastro e autenticação de usuários
- Exercícios de vocabulário e gramática
- Feedback de desempenho
- Suporte a múltiplos idiomas
- Interface responsiva e amigável

## 📌 Status do Projeto

🟡 Em desenvolvimento inicial. Atualmente, o projeto contém a estrutura básica e configurações iniciais. Funcionalidades principais ainda estão em fase de implementação.

## 📄 Licença

Este projeto está licenciado sob a Licença MIT.
