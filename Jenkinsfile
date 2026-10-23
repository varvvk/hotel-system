pipeline {
    agent any // Указание Jenkins выполнить Pipeline на любом доступном агенте

    stages {
        // Этап 1: Компиляция кода
        stage('Build') {
            steps {
                bat 'mvn compile'
            }
        }

        // Этап 2: Запуск тестов
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        // Этап 3: Упаковка проекта в JAR (без повторного запуска тестов)
        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        // ИНДИВИДУАЛЬНОЕ ЗАДАНИЕ (Вариант 7: Отель)
        // Проверка наличия созданного JAR-файла в папке target
        stage('Verify JAR') {
            steps {
                bat '''
                if exist target\\*.jar (
                    echo "УСПЕХ: JAR-файл успешно найден в директории target!"
                ) else (
                    echo "ОШИБКА: JAR-файл не создан!"
                    exit 1
                )
                '''
            }
        }
    }
}