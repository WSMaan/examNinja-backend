pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID = "583187964056"
        AWS_REGION = "us-east-2"
        ECR_REPOSITORY_NAME = "examninja"
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        AWS_ACCESS_KEY_ID = 'AKIAYPSFWECMLX2AHZDE' // Replace with your AWS access key
        AWS_SECRET_ACCESS_KEY = 'Vx3vlenqcIQyAFMBwe4FbdSfRvqWYY42lb4Be/4a' // Replace with your AWS secret key
    }
    stages {
        stage('Clone Backend Repository') {
            steps {
                git branch: 'master', url: 'https://github.com/WSMaan/examNinja-backend.git', credentialsId: 'GIT_HUB'
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
                    // Authenticate with AWS ECR using the credentials
                    sh """
                        aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                        aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
                    """
                    
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
        success {
            script {
                echo "Backend build succeeded. Triggering RestAssured test job."
                // Trigger the RestAssured test pipeline (downstream)
                build job: 'RestAssuredTestJob', wait: false // Replace with your actual RestAssured test job name
            }
        }
        failure {
            echo "Backend build failed. RestAssured tests will not be triggered."
        }
        always {
            cleanWs()
        }
    }
}
