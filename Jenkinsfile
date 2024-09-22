pipeline {
    agent any
    environment {
        REGISTRY = "your-docker-registry-url"
        REGISTRY_CREDENTIALS = 'docker-hub-credentials-id'
        IMAGE_NAME = "backend-app"
    }
    stages {
        stage('Clone Backend Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/WSMaan/examNinja-backend.git'
            }
        }
        stage('Build Backend App') {
            steps {
                script {
                    // Building the backend application using Maven
                    sh 'mvn clean install'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Building the Docker image for the backend
                    sh 'docker build -t $REGISTRY/$IMAGE_NAME .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('', "$REGISTRY_CREDENTIALS") {
                        sh "docker push $REGISTRY/$IMAGE_NAME"
                    }
                }
            }
        }
        stage('Deploy to Docker') {
            steps {
                script {
                    // Deploy using docker-compose
                    sh 'docker-compose down'
                    sh 'docker-compose up -d'
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}