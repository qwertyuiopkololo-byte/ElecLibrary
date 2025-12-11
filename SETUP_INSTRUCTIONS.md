# Инструкции по настройке проекта

## Шаг 1: Создание проекта Supabase

1. Перейдите на https://supabase.com
2. Создайте новый проект
3. Дождитесь завершения инициализации проекта
4. Перейдите в Settings > API
5. Скопируйте:
   - Project URL
   - anon/public key

## Шаг 2: Настройка базы данных

1. В Supabase Dashboard перейдите в SQL Editor
2. Откройте файл `supabase_schema.sql`
3. Скопируйте весь SQL код
4. Вставьте в SQL Editor и выполните (Run)

## Шаг 3: Настройка приложения

1. Откройте файл `app/src/main/java/com/electroniclibrary/data/supabase/SupabaseConfig.java`
2. Замените значения:
   ```java
   public static final String SUPABASE_URL = "https://ваш-проект.supabase.co";
   public static final String SUPABASE_ANON_KEY = "ваш-anon-key";
   ```

## Шаг 4: Настройка Android Studio

1. Откройте проект в Android Studio
2. Дождитесь синхронизации Gradle
3. Если есть ошибки, выполните:
   - File > Invalidate Caches / Restart
   - Build > Clean Project
   - Build > Rebuild Project

## Шаг 5: Запуск приложения

1. Подключите Android устройство или запустите эмулятор
2. Нажмите Run (Shift+F10)
3. Приложение должно запуститься

## Важные замечания

### Проблемы с Supabase Java Client

Supabase Java Client использует Kotlin корутины. В коде используется `runBlocking` для вызова Kotlin функций из Java. Это нормально для Android приложений.

Если возникают проблемы с компиляцией:
1. Убедитесь, что версии зависимостей совместимы
2. Проверьте, что используется правильная версия Supabase клиента
3. Возможно, потребуется добавить Kotlin стандартную библиотеку

### Альтернативный подход

Если Supabase Java Client вызывает проблемы, можно использовать:
- Retrofit для REST API вызовов
- Supabase REST API напрямую
- Или использовать Kotlin для работы с Supabase

### Настройка аутентификации

В текущей реализации используется упрощенная аутентификация. Для production рекомендуется:
1. Настроить Supabase Auth правильно
2. Использовать email вместо username@library.local
3. Настроить правильные политики безопасности

## Тестирование

1. Зарегистрируйте нового пользователя
2. Войдите в систему
3. Проверьте просмотр каталога
4. Добавьте книгу в избранное
5. Попробуйте поиск

## Следующие шаги

1. Добавьте реальные книги в базу данных через Supabase Dashboard
2. Загрузите обложки книг в Supabase Storage
3. Загрузите файлы книг (PDF, EPUB) в Storage
4. Настройте правильные URL для cover_url и file_url

## Поддержка

При возникновении проблем:
1. Проверьте логи в Logcat
2. Убедитесь, что Supabase проект активен
3. Проверьте правильность URL и ключей
4. Проверьте политики безопасности в Supabase

