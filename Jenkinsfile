pipeline {
    agent any

    parameters {
        string(name: 'TEST_EXEC_KEY', defaultValue: 'AI-24', description: 'Xray Test Execution Key')
        string(name: 'TEST_KEY', defaultValue: 'AI-18', description: 'Xray Test Key')
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

        stage('Create Xray JSON') {
            steps {
                powershell '''
                $xrayResult = @{
                    testExecutionKey = $env:TEST_EXEC_KEY
                    tests = @(
                        @{
                            testKey = $env:TEST_KEY
                            status = "PASS"
                            comment = "Executed from Jenkins automation"
                        }
                    )
                } | ConvertTo-Json -Depth 5

                $xrayResult | Out-File -FilePath "target/xray-result.json" -Encoding utf8
                Get-Content "target/xray-result.json"
                '''
            }
        }

        stage('Upload Xray JSON') {
            steps {
                withCredentials([
                    string(credentialsId: 'XRAY_CLIENT_ID', variable: 'XRAY_CLIENT_ID'),
                    string(credentialsId: 'XRAY_CLIENT_SECRET', variable: 'XRAY_CLIENT_SECRET')
                ]) {
                    powershell '''
                    $body = @{
                        client_id = $env:XRAY_CLIENT_ID
                        client_secret = $env:XRAY_CLIENT_SECRET
                    } | ConvertTo-Json

                    $token = Invoke-RestMethod `
                        -Uri "$env:XRAY_BASE_URL/api/v2/authenticate" `
                        -Method Post `
                        -Body $body `
                        -ContentType "application/json"

                    $response = Invoke-RestMethod `
                        -Uri "$env:XRAY_BASE_URL/api/v2/import/execution" `
                        -Method Post `
                        -Headers @{ Authorization = "Bearer $token" } `
                        -InFile "target/xray-result.json" `
                        -ContentType "application/json"

                    $response | ConvertTo-Json -Depth 10
                    '''
                }
            }
        }
    }
}