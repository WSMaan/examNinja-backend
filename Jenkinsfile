pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID = "583187964056"
        AWS_REGION = "us-east-2"
        ECR_REPOSITORY_NAME = "examninja"
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        ECR_CREDENTIALS = 'aws_key'
        MAVEN_HOME = '/usr/bin/mvn' // Set your Maven installation path
        PATH = "${MAVEN_HOME}:${env.PATH}"
    }
    stages {
        stage('Clone Backend Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/WSMaan/examNinja-backend.git', credentialsId: 'git_hub'
            }
        }
        stage('Print Environment') {
            steps {
                script {
                    sh 'echo $PATH'
                    sh 'mvn -version' // Test if Maven is recognized
                }
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
                    // Build the Docker image for the backend
                    sh 'docker build -t $ECR_REGISTRY/$ECR_REPOSITORY_NAME:backend_latest .'
                }
            }
        }
        stage('Push Docker Image to ECR') {
            steps {
                script {
                    // Authenticate with AWS ECR
                    sh "aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY"

                    // Push the Docker image to ECR
                    sh "docker push $ECR_REGISTRY/$ECR_REPOSITORY_NAME:backend_latest"
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
