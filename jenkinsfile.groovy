pipeline {
    agent any

    environment {
        APP_ENV = "dev"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Pulling source code"
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Building the project"
                sh './gradlew build'   // or mvn clean install
            }
        }

        stage('Test') {
            steps {
                echo "Running tests"
                sh './gradlew test'    // or mvn test
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo "Packaging artifact"
                sh './gradlew assemble'   // or mvn package
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo "Deploying to ${APP_ENV}"
                sh 'echo "Deploy step goes here"'
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}