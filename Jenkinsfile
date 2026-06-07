pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-17-openjdk-amd64"
        PATH = "${JAVA_HOME}/bin:${env.PATH}"

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
                sh '''
                chmod +x mvnw
                java -version
                ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                    sonar-scanner \
                    -Dsonar.projectKey=student-management \
                    -Dsonar.sources=src \
                    -Dsonar.host.url=http://10.0.2.15:9000 \
                    -Dsonar.login=$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                ls -l target
                docker build -t student-management .
                '''
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
