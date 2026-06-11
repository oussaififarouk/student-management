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
                      -Dsonar.qualitygate.wait=true \
                      -Dsonar.exclusions=Jenkinsfile,docker-compose.yml,application-local.properties,.env,**/src/test/resources/** \
                      -Dsonar.text.inclusions.exclusions=Jenkinsfile,docker-compose.yml,application-local.properties,.env,**/src/test/resources/**
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

                ENV_FILE="${APP_CONFIG_FILE:-}"
                TEMP_ENV=false

                if [ -z "$ENV_FILE" ] || [ ! -f "$ENV_FILE" ]; then
                  ENV_FILE="$(mktemp)"
                  TEMP_ENV=true

                  {
                    echo "SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL:-jdbc:mysql://172.17.0.1:3306/studentdb?createDatabaseIfNotExist=true&serverTimezone=UTC}"
                    echo "SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME:-root}"
                    echo "SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD:-}"
                    echo "SPRING_SECURITY_USER_NAME=${SPRING_SECURITY_USER_NAME:-admin}"
                    echo "SPRING_SECURITY_USER_PASSWORD=${SPRING_SECURITY_USER_PASSWORD:-admin}"
                  } > "$ENV_FILE"
                fi

                docker run -d \
                  --name student-app \
                  -p 8081:8089 \
                  --env-file "$ENV_FILE" \
                  student-management:latest

                if [ "$TEMP_ENV" = true ]; then
                  rm -f "$ENV_FILE"
                fi
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
