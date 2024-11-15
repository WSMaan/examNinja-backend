pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID = "583187964056"
        AWS_REGION = "us-east-2"
        ECR_REPOSITORY_NAME = "examninja"
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        AWS_ACCESS_KEY_ID = ''  // Ensure credentials are set
        AWS_SECRET_ACCESS_KEY = ''
        BACKEND_DIR = 'backend'
        FAILURE_REASON = ''  // To capture failure reason
        S3_BUCKET_NAME = 'examninja'
        REPORTS_DIR = "target/site"  // Surefire reports directory
    }
    stages {
        stage('Clone Repositories') {
            steps {
                dir(BACKEND_DIR) {
                    git branch: 'master', url: 'https://github.com/WSMaan/examNinja-backend.git', credentialsId: 'GIT_HUB'
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir(BACKEND_DIR) {
                    sh 'mvn clean install'
                }
            }
            post {
                failure {
                    script {
                        env.FAILURE_REASON = 'backend'
                    }
                }
            }
        }

        stage('Generate Surefire Reports') {
            steps {
                dir(BACKEND_DIR) {
                    // Generate Surefire HTML reports
                    sh 'mvn surefire-report:report'
                }
            }
            post {
                failure {
                    script {
                        env.FAILURE_REASON = 'report-generation'
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                dir(BACKEND_DIR) {
                    sh 'docker build -t ${ECR_REGISTRY}/${ECR_REPOSITORY_NAME}:backend_latest .'
                }
            }
        }
        
        stage('Push Docker Images to ECR') {
            steps {
                sh 'aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}'
                sh 'docker push ${ECR_REGISTRY}/${ECR_REPOSITORY_NAME}:backend_latest'
            }
        }

        stage('Upload Reports to S3') {
            steps {
                dir(BACKEND_DIR) {
                    sh '''
                    if [ -d ${REPORTS_DIR} ]; then
                        echo "Uploading Surefire HTML reports to S3 bucket: ${S3_BUCKET_NAME}"
                        aws s3 sync ${REPORTS_DIR} s3://${S3_BUCKET_NAME}/jenkins-reports/backend --region ${AWS_REGION}
                    else
                        echo "No reports found to upload."
                    fi
                    '''
                }
            }
        }

        // Uncomment if deploying to EKS
        // stage('Deploy to EKS') {
        //     steps {
        //         sh 'aws eks --region ${AWS_REGION} update-kubeconfig --name examninja'
        //         dir(BACKEND_DIR) {
        //             sh 'kubectl apply -f k8s/backend-deployment.yaml'
        //         }
        //     }
        // }
    }

    post {
        always {
            cleanWs()
        }
        failure {
            script {
                echo "Pipeline failed due to failure in the ${env.FAILURE_REASON} stage."
                // slackSend(channel: '#exam-ninja', color: 'danger', message: "Pipeline failed due to failure in the ${env.FAILURE_REASON} stage. Check Jenkins for details.")
            }
        }
        success {
            // slackSend(channel: '#exam-ninja', color: 'good', message: 'Pipeline succeeded!')
            echo 'Pipeline succeeded!'
        }
    }
}
