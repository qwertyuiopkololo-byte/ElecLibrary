-- SQL скрипт для создания таблицы жанров и добавления категорий
-- Выполните этот скрипт в SQL Editor вашего Supabase проекта

-- Создание таблицы жанров (если её нет)
CREATE TABLE IF NOT EXISTS genres (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Включить Row Level Security (RLS)
ALTER TABLE genres ENABLE ROW LEVEL SECURITY;

-- Политика безопасности для чтения (все могут читать)
DROP POLICY IF EXISTS "Anyone can read genres" ON genres;
CREATE POLICY "Anyone can read genres" ON genres FOR SELECT USING (true);

-- Функция для автоматического обновления updated_at
CREATE OR REPLACE FUNCTION update_genres_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Триггер для автоматического обновления updated_at
DROP TRIGGER IF EXISTS update_genres_updated_at ON genres;
CREATE TRIGGER update_genres_updated_at BEFORE UPDATE ON genres
    FOR EACH ROW EXECUTE FUNCTION update_genres_updated_at();

-- Вставка категорий/жанров
INSERT INTO genres (name, description) VALUES
    ('Фантастика', 'Научная фантастика и фэнтези'),
    ('Детектив', 'Детективные романы и триллеры'),
    ('Роман', 'Романтические произведения'),
    ('История', 'Исторические произведения'),
    ('Наука', 'Научно-популярная литература'),
    ('Классика', 'Классическая литература'),
    ('Приключения', 'Приключенческие романы'),
    ('Ужасы', 'Хоррор и мистика'),
    ('Поэзия', 'Поэтические произведения'),
    ('Биография', 'Биографические произведения')
ON CONFLICT (name) DO NOTHING;

