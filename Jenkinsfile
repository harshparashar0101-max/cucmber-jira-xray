pipeline {
    agent any

    parameters {
        string(name: 'TEST_EXEC_KEY', defaultValue: 'AI-24', description: 'Xray Test Execution Key')
    }

    environment {
        XRAY_BASE_URL = 'https://eu.xray.cloud.getxray.app'
    }

    stages {
        stage('Run Tests') {
            steps {
                bat 'call mvn clean test'
            }
        }

        stage('Upload Result to Xray') {
            steps {
                withCredentials([
                    string(credentialsId: 'XRAY_CLIENT_ID', variable: 'XRAY_CLIENT_ID'),
                    string(credentialsId: 'XRAY_CLIENT_SECRET', variable: 'XRAY_CLIENT_SECRET')
                ]) {
                      powershell """
                      \$body = @{
                        client_id = '${XRAY_CLIENT_ID}'
                          client_secret = '${XRAY_CLIENT_SECRET}'
                        } | ConvertTo-Json

                     \$token = Invoke-RestMethod `
                         -Uri '${XRAY_BASE_URL}/api/v2/authenticate' `
                         -Method Post `
                         -Body \$body `
                         -ContentType 'application/json'

                      Invoke-RestMethod `
                         -Uri '${XRAY_BASE_URL}/api/v2/import/execution/junit?testExecKey=${params.TEST_EXEC_KEY}' `
                         -Method Post `
                         -Headers @{ Authorization = "Bearer \$token" } `
                         -InFile 'target/cucumber.xml' `
                          -ContentType 'application/xml'

                          Write-Host "Xray updated successfully: ${params.TEST_EXEC_KEY}"
                          """
                }
            }
        }
    }
}