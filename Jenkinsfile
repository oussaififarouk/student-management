pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-21-openjdk-amd64"
        SONAR_SCANNER = tool 'SonarScanner'
        SONAR_HOST_URL = 'https://10.0.2.15:9000'
        PATH = "${JAVA_HOME}/bin:${SONAR_SCANNER}/bin:${env.PATH}"
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
                export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                export PATH=$JAVA_HOME/bin:$PATH

                chmod +x mvnw
                java -version
                ./mvnw clean package -DskipTests
                '''
            }
        }

      stage('SonarQube Analysis') {
    steps {
        withCredentials([string(credentialsId: 'sonar-token-student-management', variable: 'SONAR_TOKEN')]) {
            sh '''
            export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
            export PATH=$JAVA_HOME/bin:$PATH

            ./mvnw sonar:sonar \
            -Dsonar.projectKey=student-management \
            -Dsonar.host.url=$SONAR_HOST_URL \
            -Dsonar.token=$SONAR_TOKEN
            '''
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
                docker run -d -p 8081:8089 \
                  -e SPRING_DATASOURCE_URL \
                  -e SPRING_DATASOURCE_USERNAME \
                  -e SPRING_DATASOURCE_PASSWORD \
                  --name student-app student-management
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès'
        }

        failure {
            echo '❌ Pipeline échoué - vérifier les logs'
        }
    }
}
