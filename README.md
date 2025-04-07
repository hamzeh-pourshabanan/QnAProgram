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

Edit `src/main/resources/application.conf`:
```hocon
database {
  url = "jdbc:sqlite:qa.db" # Change to :memory: for tests
}
```

## FAQ
### Where is data stored?
By default in `./qa.db` (SQLite file).

### How to handle special characters?
Answers support any characters except unclosed quotes.

## License
Distributed under the MIT License. See `LICENSE` for more information.
