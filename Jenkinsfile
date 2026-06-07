pipeline {
    agent any
    
    // 1. Déclaration de l'outil
    tools {
        sonarRunner 'SonarScanner' 
    }

    stages {
        // 2. Étape de récupération du code
        stage('Clone Git') {
            steps {
                git 'https://github.com/oussaififarouk/student-management.git'
            }
        }
        
        // 3. Étape d'analyse SonarQube
        stage('SonarQube Analysis') {
            steps {
                sh 'sonar-scanner -Dsonar.projectKey=student-management -Dsonar.projectName=student-management -Dsonar.sources=src -Dsonar.host.url=http://10.0.2.15:9000 -Dsonar.token=sqp_e40392a2fe8cce8880f2e5ea46337ead4c5c994f'
            }
        }
    }
}
