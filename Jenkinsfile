pipeline {
    agent any

    tools {
        sonarRunner 'SonarScanner'
    }

    stages {
        stage('SonarQube Analysis') {
            steps {
                sh 'sonar-scanner -v'
            }
        }
    }
}
