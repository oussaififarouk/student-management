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

        stage('Build, Test & SonarQube') {
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

                    chmod +x mvnw

                    echo "=== Java Version ==="
                    java -version

                    echo "=== Maven Verify + SonarQube ==="
                    ./mvnw clean verify sonar:sonar \
                      -Dsonar.projectKey=student-management \
                      -Dsonar.host.url=$SONAR_HOST_URL \
                      -Dsonar.token=$SONAR_TOKEN \
                      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                      -Dsonar.exclusions=Jenkinsfile,docker-compose.yml,application-local.properties,application-local.properties.example,.env,.env.example,**/src/test/resources/** \
                      -Dsonar.text.inclusions.exclusions=Jenkinsfile,docker-compose.yml,application-local.properties,application-local.properties.example,.env,.env.example,**/src/test/resources/**
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
                  -e SPRING_DATASOURCE_URL \
                  -e SPRING_DATASOURCE_USERNAME \
                  -e SPRING_DATASOURCE_PASSWORD \
                  -e SPRING_SECURITY_USER_NAME \
                  -e SPRING_SECURITY_USER_PASSWORD \
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
            echo 'Pipeline terminé avec succès'
        }

        failure {
            echo 'Pipeline échoué - vérifier les logs'
        }

        always {
            sh 'docker ps -a || true'
        }
    }
}
