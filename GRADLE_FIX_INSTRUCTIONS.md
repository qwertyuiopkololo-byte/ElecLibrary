# Инструкция по исправлению ошибки Gradle

## Проблема
Ошибка: `Plugin [id: 'com.android.application'] was not found`

## Решение

Вам нужно создать два файла в **корне проекта** (на уровень выше папки `app`):

### 1. Создайте файл `build.gradle` в корне проекта

Создайте файл `build.gradle` в корне проекта (там же, где находится папка `app`) со следующим содержимым:

```gradle
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.0'
    }
}

plugins {
    id 'com.android.application' version '8.1.0' apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

### 2. Создайте файл `settings.gradle` в корне проекта

Создайте файл `settings.gradle` в корне проекта со следующим содержимым:

```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ElectronicLibrary"
include ':app'
```

### 3. Структура проекта должна быть такой:

```
ВашПроект/
├── build.gradle          ← Создайте этот файл
├── settings.gradle       ← Создайте этот файл
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── app/
    ├── build.gradle      ← Этот файл уже существует
    └── src/
        └── ...
```

### 4. После создания файлов:

1. В Android Studio: **File > Sync Project with Gradle Files**
2. Или закройте и откройте проект заново
3. Gradle должен автоматически синхронизироваться

### Альтернативный способ (через Android Studio):

1. Откройте Android Studio
2. **File > New > New Project**
3. Выберите **Empty Activity**
4. Скопируйте содержимое папки `app` из текущего проекта в новый проект
5. Android Studio автоматически создаст правильную структуру Gradle файлов

### Если проблема сохраняется:

1. Убедитесь, что у вас установлен Android Gradle Plugin
2. Проверьте версию Gradle в `gradle/wrapper/gradle-wrapper.properties`
3. Должна быть версия: `distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip` или выше

### Проверка версии Gradle:

Откройте `gradle/wrapper/gradle-wrapper.properties` и убедитесь, что там указана версия Gradle 8.0 или выше:

```
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.0-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

Если файла `gradle-wrapper.properties` нет, создайте папку `gradle/wrapper/` и создайте этот файл с содержимым выше.

