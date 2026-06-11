pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-21-openjdk-amd64"
        SONAR_SCANNER = tool 'SonarScanner'
        SONAR_HOST_URL = 'http://10.0.2.15:9000'
        PATH = "${JAVA_HOME}/bin:${SONAR_SCANNER}/bin:${env.PATH}"
    }

    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/oussaififarouk/student-management.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh '''
                export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                export PATH=$JAVA_HOME/bin:$PATH

                chmod +x mvnw

                echo "=== Java Version ==="
                java -version

                echo "=== Maven Build ==="
                ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'sonar-token-student-management',
                        variable: 'SONAR_TOKEN'
                    )
                ]) {
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
                echo "=== Vérification du JAR ==="
                ls -lh target/

                docker build -t student-management:latest .
                '''
            }
        }

        stage('Docker Run') {
            steps {
                sh '''
                docker rm -f student-app || true

                docker run -d \
                  --name student-app \
                  -p 8081:8089 \
                  -e SPRING_DATASOURCE_URL="$SPRING_DATASOURCE_URL" \
                  -e SPRING_DATASOURCE_USERNAME="$SPRING_DATASOURCE_USERNAME" \
                  -e SPRING_DATASOURCE_PASSWORD="$SPRING_DATASOURCE_PASSWORD" \
                  student-management:latest
                '''
            }
        }

        stage('Docker Status') {
            steps {
                sh '''
                echo "=== Conteneurs Docker ==="
                docker ps
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

        always {
            sh 'docker ps -a || true'
        }
    }
}
