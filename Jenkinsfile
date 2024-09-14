pipeline {
    agent any
    tools {
        maven 'Maven 3.6.3' // Ensure Maven is configured in Jenkins
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
    post {
        success {
            build job: 'testing_examninja', wait: true
        }
    }
}
