# Ускорь индексацию

## Запуск тестов

    ./gradlew clean build

Windows: `.\gradlew.bat clean build`

## Основное задание

**Файл с реализацией:** `src/main/kotlin/lab2/ProductSelection.kt`

**Как работает:** строю `HashMap` каталога по `id`, `HashSet` исключений и `HashSet` добавленных. Один проход по `recommendedIds` слева направо: пропускаю уже добавленные, исключённые, отсутствующие в каталоге и недоступные. Порядок — по первому появлению `id` в рекомендациях, уникальность — через `added`. Входы не меняются, возвращаются исходные `Product`.

**Сложность** (`p` — каталог, `r` — рекомендации, `e` — исключения):

- время: `O(p + e + r)`;
- доп. память: `O(p + e)`, память под результат — отдельно `O(k)`, где `k` — размер ответа.

**Тесты** (`src/test/kotlin/lab2/ProductSelectionTest.kt`):

| Что проверяется | Тест |
|---|---|
| Обычная подборка, порядок ≠ порядку каталога | `selects available products in recommendation order` |
| Повторы рекомендаций и исключений | `duplicate recommendations and exclusions are handled` |
| Недоступные, исключённые, неизвестные | `unavailable excluded and unknown products are skipped` |
| Пустые входы | `empty catalog recommendations or exclusions produce empty result` |
| Ни один не подходит | `returns empty when no product fits` |
| Входы не изменяются, вызовы независимы | `inputs are not modified and calls are independent` |
| Полные объекты `Product` | `result keeps original product objects with all fields` |

