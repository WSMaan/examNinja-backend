pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID = "583187964056"
        AWS_REGION = "us-east-2"
        ECR_REPOSITORY_NAME = "examninja"
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        AWS_ACCESS_KEY_ID = 'AKIAYPSFWECMFUDEPHEI' // Replace with your AWS access key
        AWS_SECRET_ACCESS_KEY = 'P5HvKjEb5yjDBx+zI/3P7eb25TspKNFD9WIqTitV' // Replace with your AWS secret key
        BACKEND_DIR = 'backend'
        FAILURE_REASON = ''
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
        stage('Deploy to EKS') {
            steps {
                // Ensure kubectl is configured for your EKS cluster
                sh 'aws eks --region ${AWS_REGION} update-kubeconfig --name examninja' // Change 'my-cluster' to your cluster name
                // Apply Kubernetes deployment files
                dir(BACKEND_DIR) {
                    sh 'kubectl apply -f k8s/backend-deployment.yaml' // Ensure your backend deployment file is correctly defined
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
