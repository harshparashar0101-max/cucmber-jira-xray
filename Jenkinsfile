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

        stage('Upload Cucumber JSON to Xray') {
            steps {
                withCredentials([
                    string(credentialsId: 'XRAY_CLIENT_ID', variable: 'XRAY_CLIENT_ID'),
                    string(credentialsId: 'XRAY_CLIENT_SECRET', variable: 'XRAY_CLIENT_SECRET')
                ]) {
                    powershell '''
                    Write-Host "Authenticating with Xray..."

                    $body = @{
                        client_id = $env:XRAY_CLIENT_ID
                        client_secret = $env:XRAY_CLIENT_SECRET
                    } | ConvertTo-Json

                    $token = Invoke-RestMethod `
                        -Uri "$env:XRAY_BASE_URL/api/v2/authenticate" `
                        -Method Post `
                        -Body $body `
                        -ContentType "application/json"

                    Write-Host "Uploading cucumber.json to existing Test Execution..."
                    Write-Host "Execution Key: $env:TEST_EXEC_KEY"
                    Write-Host "URL: $env:XRAY_BASE_URL/api/v2/import/execution/cucumber?testExecKey=$env:TEST_EXEC_KEY"

                    $response = Invoke-RestMethod `
                        -Uri "$env:XRAY_BASE_URL/api/v2/import/execution/cucumber?testExecKey=$env:TEST_EXEC_KEY" `
                        -Method Post `
                        -Headers @{ Authorization = "Bearer $token" } `
                        -InFile "target/cucumber.json" `
                        -ContentType "application/json"

                    Write-Host "Xray response:"
                    $response | ConvertTo-Json -Depth 10
                    '''
                }
            }
        }
    }
}