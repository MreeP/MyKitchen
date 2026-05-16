1. `recipes`:

- `id` - pk
- `name`
- `imageResName`
- `timeMinutes`
- `servings`
- `rating`
- `category`
- `authorName`
- `likes`
- `tags`
- `isUserRecipe`

2. `ingredients`:

- `id` - pk
- `recipeId` - fk → `recipes`
- `name`
- `amount`

Rola: Składniki (1:N do `recipes`)

3. `steps`:

- `id` - pk
- `recipeId` - fk → `recipes`
- `stepNumber`
- `description`
- `timerSeconds?`
- `imageResName?`

Rola: Kroki przygotowania (1:N)

4. `favorites`:

- `id` - pk
- `recipeId` - fk unique → `recipes`
- `addedAt`

Rola: Polubione (1:1 do `recipes`)

5. `cooking_history`:

- `id` - pk
- `recipeId` - fk → `recipes`
- `cookedAt`

Rola: Historia gotowania (1:N)
