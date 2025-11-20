```mermaid
  graph TB
  subgraph "Presentation Layer (MVI)"
  VM[ViewModel]
  UI[Jetpack Compose]
  STATE[UIState]
  EFFECT[UIEffect]
  INTENT[UIEvent]
  end
  subgraph "Domain Layer"
      UC[Use Cases]
      REPO_IF[Repository]
  end
  subgraph "Data Layer"
      REPO[Repository Impl]
      API[API Service]
      ROOM[(Room DB)]
  end

  UI -->|emit| INTENT
  INTENT -->|handle| VM
  VM --> UC
  UC --> REPO_IF
  REPO_IF -.implements.- REPO
  REPO --> API
  REPO --> ROOM
  VM -->|emit| STATE
  VM -->|emit| EFFECT
  STATE -->|recomposition trigger| UI
  EFFECT -->|handle| UI


  %% --------------------
  %% 클래스 정의 (배경색)
  %% --------------------
  classDef presentation fill:#E8F4FF,stroke:#4C92FF,stroke-width:1px,color:#000;
  classDef domain fill:#FFF6E5,stroke:#FFA500,stroke-width:1px,color:#000;
  classDef data fill:#F0FFE8,stroke:#4CAF50,stroke-width:1px,color:#000;

  %% --------------------
  %% 클래스 적용
  %% --------------------
  class VM,UI,STATE,EFFECT,INTENT presentation;
  class UC,REPO_IF domain;
  class REPO,API,ROOM data;
```
