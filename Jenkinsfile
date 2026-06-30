pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/venkatsaiakkiraju-cloud/banking.git'
            }
        }

        stage('Build Customer Service') {
            steps {
                dir('customer-service') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Verify Build') {
            steps {
                sh 'ls -l customer-service/target'
            }
        }
    }

    post {
        success {
            echo 'Customer Service Build Completed Successfully!'
        }
        failure {
            echo 'Customer Service Build Failed!'
        }
    }
}
