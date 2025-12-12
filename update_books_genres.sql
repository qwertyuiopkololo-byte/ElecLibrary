-- SQL скрипт для распределения книг по жанрам
-- Выполните этот скрипт в SQL Editor вашего Supabase проекта
-- Этот скрипт обновит существующие книги, привязав их к жанрам

-- Сначала убедитесь, что таблица genres существует и заполнена
-- Если нет, выполните сначала основную схему supabase_schema.sql

-- Обновление книг по жанрам
DO $$
DECLARE
    genre_fantasy UUID;
    genre_detective UUID;
    genre_roman UUID;
    genre_history UUID;
    genre_science UUID;
    genre_classic UUID;
    genre_adventure UUID;
    genre_horror UUID;
    genre_poetry UUID;
    genre_biography UUID;
BEGIN
    -- Получаем ID жанров
    SELECT id INTO genre_fantasy FROM genres WHERE name = 'Фантастика' LIMIT 1;
    SELECT id INTO genre_detective FROM genres WHERE name = 'Детектив' LIMIT 1;
    SELECT id INTO genre_roman FROM genres WHERE name = 'Роман' LIMIT 1;
    SELECT id INTO genre_history FROM genres WHERE name = 'История' LIMIT 1;
    SELECT id INTO genre_science FROM genres WHERE name = 'Наука' LIMIT 1;
    SELECT id INTO genre_classic FROM genres WHERE name = 'Классика' LIMIT 1;
    SELECT id INTO genre_adventure FROM genres WHERE name = 'Приключения' LIMIT 1;
    SELECT id INTO genre_horror FROM genres WHERE name = 'Ужасы' LIMIT 1;
    SELECT id INTO genre_poetry FROM genres WHERE name = 'Поэзия' LIMIT 1;
    SELECT id INTO genre_biography FROM genres WHERE name = 'Биография' LIMIT 1;
    
    -- Обновляем книги по названиям
    -- Классика
    UPDATE books SET genre_id = genre_classic WHERE title IN (
        'Хамелеон', 'Толстый и тонкий', 'Станционный смотритель', 'Нос', 'Шинель',
        'Муму', 'Белые ночи', 'Каштанка', 'Дубровский', 'Война и мир',
        'Преступление и наказание', 'Анна Каренина', 'Евгений Онегин', 'Отцы и дети',
        'Идиот', 'Братья Карамазовы', 'Обломов', 'Герой нашего времени',
        'Мёртвые души', 'Капитанская дочка', 'Гроза', 'Ревизор', 'Тарас Бульба',
        'Вишнёвый сад', 'Три сестры', 'Чайка'
    );
    
    -- Фантастика
    UPDATE books SET genre_id = genre_fantasy WHERE title IN (
        'Собачье сердце', 'Мастер и Маргарита', 'Роковые яйца'
    );
    
    -- Роман
    UPDATE books SET genre_id = genre_roman WHERE title IN (
        'Доктор Живаго', 'Лолита', 'Двенадцать стульев', 'Золотой телёнок',
        'Доктор Фаустус', 'Смерть в Венеции'
    );
    
    -- История
    UPDATE books SET genre_id = genre_history WHERE title IN (
        'Белая гвардия'
    );
    
    -- Биография
    UPDATE books SET genre_id = genre_biography WHERE title IN (
        'Один день Ивана Денисовича', 'Архипелаг ГУЛАГ'
    );
    
    -- Поэзия
    UPDATE books SET genre_id = genre_poetry WHERE title IN (
        'Двенадцать', 'Незнакомка', 'Стихи о прекрасной даме'
    );
    
    -- Детектив
    UPDATE books SET genre_id = genre_detective WHERE title IN (
        'Записки из подполья', 'Бесы', 'Игрок'
    );
END $$;


