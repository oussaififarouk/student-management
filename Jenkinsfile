pipeline {
    agent any

    environment {
        SONAR_SCANNER = tool 'SonarScanner'
        PATH = "${SONAR_SCANNER}/bin:${env.PATH}"
    }

    stages {
        stage('SonarQube') {
            steps {
                sh 'sonar-scanner -v'
            }
        }
    }
}
