pipeline {
    agent any

    environment {
        SONAR_SCANNER = tool 'SonarScanner'
        PATH = "${SONAR_SCANNER}/bin:${env.PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                git url: 'https://github.com/oussaififarouk/student-management.git', branch: 'main'
            }
        }

        stage('Build Maven') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh """
                sonar-scanner \
                -Dsonar.projectKey=student-management \
                -Dsonar.sources=src \
                -Dsonar.host.url=http://10.0.2.15:9000 \
                -Dsonar.login=YOUR_TOKEN
                """
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t student-management .'
            }
        }

        stage('Docker Run') {
            steps {
                sh '''
                docker rm -f student-app || true
                docker run -d -p 8081:8080 --name student-app student-management
                '''
            }
        }
    }
}
