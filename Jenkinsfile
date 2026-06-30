
pipeline {
    agent any

    tools {
        maven 'Maven'
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
    }

    post {
        success {
            echo 'Customer Service Build Successful!'
        }

        failure {
            echo 'Build Failed!'
        }
    }
}
