pipeline {
    agent any

    parameters {
        choice(
            name: 'TEST_SUITE',
            choices: ['smoke', 'regression', 'both'],
            description: 'Select test suite to run'
        )

    }

    environment {
        XRAY_BASE_URL = 'https://eu.xray.cloud.getxray.app'
    }

    stages {

        stage('Clean Project') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('Run Tests By Tag') {
            steps {
                script {
                    if (params.TEST_SUITE == 'smoke') {
                        bat 'mvn test -Dcucumber.filter.tags="@smoke"'
                    } 
                    else if (params.TEST_SUITE == 'regression') {
                        bat 'mvn test -Dcucumber.filter.tags="@regression"'
                    } 
                    else if (params.TEST_SUITE == 'both') {
                        bat 'mvn test -Dcucumber.filter.tags="@smoke or @regression"'
                    }
                }
            }
        }

        stage('Publish Test Report') {
            steps {
                junit 'target/cucumber.xml'
            }
        }

        stage('Create Xray JSON') {
            steps {
                powershell """
                \$xrayResult = @{
                    testExecutionKey = '${params.TEST_EXEC_KEY}'
                    tests = @(
                        @{
                            testKey = '${params.TEST_KEY}'
                            status = 'PASSED'
                            comment = 'Executed from Jenkins with suite: ${params.TEST_SUITE}'
                        }
                    )
                } | ConvertTo-Json -Depth 5

                New-Item -ItemType Directory -Force -Path target | Out-Null
                \$xrayResult | Out-File -FilePath 'target/xray-result.json' -Encoding utf8

                Write-Host "Xray JSON Created:"
                Get-Content 'target/xray-result.json'
                """
            }
        }

        stage('Upload to Xray') {
            steps {
                withCredentials([
                    string(credentialsId: 'XRAY_CLIENT_ID', variable: 'XRAY_CLIENT_ID'),
                    string(credentialsId: 'XRAY_CLIENT_SECRET', variable: 'XRAY_CLIENT_SECRET')
                ]) {
                    powershell """
                    Write-Host "Authenticating with Xray..."

                    \$authBody = @{
                        client_id = '${XRAY_CLIENT_ID}'
                        client_secret = '${XRAY_CLIENT_SECRET}'
                    } | ConvertTo-Json

                    \$token = Invoke-RestMethod `
                        -Uri '${XRAY_BASE_URL}/api/v2/authenticate' `
                        -Method Post `
                        -Body \$authBody `
                        -ContentType 'application/json'

                    Write-Host "Uploading results to Xray..."

                    \$response = Invoke-RestMethod `
                        -Uri '${XRAY_BASE_URL}/api/v2/import/execution' `
                        -Method Post `
                        -Headers @{ Authorization = "Bearer \$token" } `
                        -InFile 'target/xray-result.json' `
                        -ContentType 'application/json'

                    Write-Host "Xray Response:"
                    \$response | ConvertTo-Json -Depth 10
                    """
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.json, target/*.xml, target/*.html', fingerprint: true, allowEmptyArchive: true
        }
    }
}