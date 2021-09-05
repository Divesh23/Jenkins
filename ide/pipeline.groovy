pipeline {
    agent any
    triggers { pollSCM('* * * * *') }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Divesh23/jgsu-spring-petclinic.git', branch: 'main'
            }            
        }
        stage('Build') {
            steps {
                sh './mvnw clean package'
                //sh 'true' // true
            }
        
            post {
                always {
                   junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
             //   }
           // changed{
                emailext attachLog: true, body: '"Please go to ${BUILD_URL} and verify the build"',
                recipientProviders: [requestor(), upstreamDevelopers()],
                subject: '"Job \'${JOB_NAME}\' (${BUILD_NUMBER})",',
                to:"test@jenkins"
            }
            }
        }
    }
}
