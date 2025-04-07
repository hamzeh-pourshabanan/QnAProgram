# QA Console Application
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A command-line question-answer system with persistent storage, following clean **hexagonal architecture** principles.

## Features

- 💾 **Persistent storage** using SQLite
- 🔍 **Exact-match question retrieval**
- ➕ **Multi-answer support** per question
- 🛡️ **Input validation** with clear error messages
- 📦 **Self-contained** with no external dependencies

## Quick Start

### Prerequisites
- Java 17+
- Gradle 7.5+

### Installation
```bash
git clone https://github.com/yourusername/qa-console-app.git
cd qa-console-app
```

### Running the Application
```bash
./gradlew run
```

### Usage
### Adding Questions
```text
<question>? "answer1" "answer2"...

Example:
What is Java? "A programming language" "A platform"
```

### Asking Questions
```text
Enter your question:
> How to exit Vim?
:q!
:wq
```

## Architecture
```mermaid
graph TD
    CLI[Console Input] --> Adapter
    Adapter --> |Parsed Input| Service[QA Service]
    Service --> |Domain Objects| Repository
    Repository --> SQLite[(SQLite Database)]
```

### Key Components

- **Domain**: `Question`, `Answer` value objects

- **Ports**: `InputPort`, `QuestionRepository` interfaces

- **Adapters**: `ConsoleInputAdapter`, `SQLiteQuestionRepository`

- **Core**: `QAService` business logic

### Technology Choices
| Component | Library       | Version      | Rationale                 |
| --------- | ------------- | ------------ | ------------------------- |
| Database  | SQLite JDBC   | 3.49.1       | Lightweight, zero-config  |
| Testing   | JUnit+Mockito | 5.9.1/5.17.0 | Modern Java testing stack |

## 📦 Dependencies

### Runtime
- [SQLite JDBC](https://github.com/xerial/sqlite-jdbc) 3.49.1 - Database driver

### Testing
- [JUnit 5](https://junit.org/junit5/) 5.9.1 - Test framework
- [Mockito](https://site.mockito.org/) 5.17.0 - Mocking library
- [AssertJ](https://assertj.github.io/doc/) 3.27.3 - Fluent assertions

## Development

### Building
```bash
./gradlew build
```

### Testing
```bash
./gradlew test
```

## Configuration

Edit `src/main/java/qa/infrastructure/DatabaseConfig.java`:
```java
//change to inMemory() for testing
DatabaseConfig config = DatabaseConfig.fileBased("qa.db");
```

## FAQ
### Where is data stored?
By default in `./qa.db` (SQLite file).

### How to handle special characters?
Answers support any characters except unclosed quotes.

## License
Distributed under the MIT License. See `LICENSE` for more information.
