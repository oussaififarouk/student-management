pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/java-21-openjdk-amd64"
        SONAR_SCANNER = tool 'SonarScanner'
        SONAR_HOST_URL = 'http://10.0.2.15:9000'
        KUBECONFIG = '/var/lib/jenkins/.kube/config' // 🔄 AJOUTÉ : Chemin d'accès au cluster
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

        stage('Build & Test') {
            steps {
                sh '''
                export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                export PATH=$JAVA_HOME/bin:$PATH

                chmod +x mvnw

                echo "=== Java Version ==="
                java -version

                echo "=== Maven Verify ==="
                ./mvnw clean verify
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'sonarqube-token',
                        variable: 'SONAR_TOKEN'
                    )
                ]) {
                    sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    echo "=== SonarQube Analysis ==="
                    ./mvnw sonar:sonar \
                      -Dsonar.projectKey=student-management \
                      -Dsonar.host.url=$SONAR_HOST_URL \
                      -Dsonar.token=$SONAR_TOKEN \
                      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                      -Dsonar.qualitygate.wait=true \
                      -Dsonar.qualitygate.timeout=600 \
                      -Dsonar.exclusions=Jenkinsfile,docker-compose.yml,application-local.properties,.env,**/src/test/resources/** \
                      -Dsonar.text.inclusions.exclusions=Jenkinsfile,docker-compose.yml,application-local.properties,.env,**/src/test/resources/**
                    '''
                }
            }
        }

        stage('Docker Build (Minikube)') {
            steps {
                sh '''
                echo "=== Vérification du JAR ==="
                ls -lh target/

                echo "=== Construction de l'image Docker dans Minikube ==="
                # 🔄 MODIFIÉ : On bascule sur le contexte Docker de Minikube
                eval $(minikube docker-env)
                docker build -t student-management:latest .
                '''
            }
        }

        stage('Kubernetes Deploy') {
            steps {
                sh '''
                echo "=== Création du Namespace devops si nécessaire ==="
                kubectl create namespace devops --dry-run=client -o yaml | kubectl apply -f -

                echo "=== Déploiement des Manifests k8s ==="
                kubectl apply -f k8s/mysql-deployment.yaml
                kubectl apply -f k8s/sonarqube-deployment.yaml
                kubectl apply -f k8s/spring-deployment.yaml

                echo "=== Forcer la mise à jour de l'image de l'application ==="
                kubectl rollout restart deployment/studentmang-app -n devops
                '''
            }
        }

        stage('Kubernetes Status') {
            steps {
                sh '''
                echo "=== Vérification de l'état des Pods dans devops ==="
                kubectl get pods -n devops

                echo "=== Vérification des Services exposés ==="
                kubectl get svc -n devops
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès - Application déployée sur Kubernetes'
        }

        failure {
            echo 'Pipeline échoué - vérifier les logs'
        }

        always {
            sh '''
            echo "=== État final global ==="
            eval $(minikube docker-env)
            docker ps -a || true
            kubectl get deployments -n devops || true
            '''
        }
    }
}