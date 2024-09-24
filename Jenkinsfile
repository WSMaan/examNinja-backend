pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID = '583187964056'
        AWS_REGION = 'us-east-2'
        ECR_REPOSITORY_URI = '583187964056.dkr.ecr.us-east-2.amazonaws.com/examninja'  // Added ECR repository URI
        IMAGE_TAG = "backend_latest"  // Updated image tag for backend
        REPO_URL = "${ECR_REPOSITORY_URI}:${IMAGE_TAG}"
        GIT_REPO_URL = 'https://github.com/WSMaan/examNinja-backend.git'
        REGISTRY_CREDENTIALS = 'aws-ecr-credentials-id'
    }

    stages {
        stage('Clone Backend Repository') {
            steps {
                // Clone the backend repository
                git branch: 'master', url: GIT_REPO_URL
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
                    sh "docker build -t $REPO_URL ."
                }
            }
        }

        stage('Push Docker Image to ECR') {
            steps {
                script {
                    // Authenticate with AWS ECR and push the Docker image
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws_key']]) {
                        sh '''
                            aws ecr get-login-password --region $AWS_REGION | \
                            docker login --username AWS --password-stdin $ECR_REPOSITORY_URI
                            docker push $REPO_URL
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
    }
}
