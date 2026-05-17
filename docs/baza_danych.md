# Schemat bazy danych

```mermaid
erDiagram
    categories {
        int id PK
        string name
        string imageUri
    }

    recipes {
        int id PK
        string name
        int servings
        float rating
        string authorName
        int time
        string imageUri
        int category_id FK
    }

    ingredients {
        int id PK
        int recipeId FK
        string name
        string amount
        string unit
    }

    steps {
        int id PK
        int recipeId FK
        int stepNumber
        string description
        int timerSeconds
        string imageUri
    }

    favorites {
        int id PK
        int recipeId FK
        datetime addedAt
    }

    cooking_history {
        int id PK
        int recipeId FK
        datetime cookedAt
    }

    categories ||--o{ recipes : "has"
    recipes ||--o{ ingredients : "has"
    recipes ||--o{ steps : "has"
    recipes ||--|| favorites : "favorited by"
    recipes ||--o{ cooking_history : "cooked in"
```

1. `categories`:

- `id` - pk
- `name`
- `imageUri`

2. `recipes`:

- `id` - pk
- `name`
- `servings`
- `rating`
- `authorName`
- `time`
- `imageUri`
- `category_id`

3. `ingredients`:

- `id` - pk
- `recipeId` - fk → `recipes`
- `name`
- `amount`
- `unit`

4. `steps`:

- `id` - pk
- `recipeId` - fk → `recipes`
- `stepNumber`
- `description`
- `timerSeconds?`
- `imageUri?`

Rola: Kroki przygotowania (1:N)

5. `favorites`:

- `id` - pk
- `recipeId` - fk unique → `recipes`
- `addedAt`

Rola: Polubione (1:1 do `recipes`)

6. `cooking_history`:

- `id` - pk
- `recipeId` - fk → `recipes`
- `cookedAt`

Rola: Historia gotowania (1:N)
