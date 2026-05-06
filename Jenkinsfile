pipeline {
    agent any

    parameters {
        choice(name: 'TEST_SUITE', choices: ['smoke', 'regression', 'both'], description: 'Select test suite to run')

        string(name: 'TEST_EXEC_KEY', defaultValue: 'AI-24', description: 'Xray Test Execution Key')
        string(name: 'SMOKE_TEST_KEY', defaultValue: 'AI-18', description: 'Smoke Test Key')
        string(name: 'REGRESSION_TEST_KEY', defaultValue: 'AI-22', description: 'Regression Test Key')
    }

    environment {
        XRAY_BASE_URL = 'https://eu.xray.cloud.getxray.app'
    }

    stages {

        stage('Validate Parameters') {
            steps {
                script {
                    if (!params.TEST_EXEC_KEY?.trim()) {
                        error "TEST_EXEC_KEY is missing"
                    }
                    if (!params.SMOKE_TEST_KEY?.trim()) {
                        error "SMOKE_TEST_KEY is missing"
                    }
                    if (!params.REGRESSION_TEST_KEY?.trim()) {
                        error "REGRESSION_TEST_KEY is missing"
                    }

                    echo "Selected Suite: ${params.TEST_SUITE}"
                    echo "Execution Key: ${params.TEST_EXEC_KEY}"
                }
            }
        }

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
                junit allowEmptyResults: true, testResults: 'target/cucumber.xml'
            }
        }

        stage('Create Xray JSON') {
            steps {
                powershell """
                \$tests = @()

                if ('${params.TEST_SUITE}' -eq 'smoke') {
                    \$tests += @{
                        testKey = '${params.SMOKE_TEST_KEY}'
                        status = 'PASSED'
                        comment = 'Smoke test executed from Jenkins'
                    }
                }

                if ('${params.TEST_SUITE}' -eq 'regression') {
                    \$tests += @{
                        testKey = '${params.REGRESSION_TEST_KEY}'
                        status = 'PASSED'
                        comment = 'Regression test executed from Jenkins'
                    }
                }

                if ('${params.TEST_SUITE}' -eq 'both') {
                    \$tests += @{
                        testKey = '${params.SMOKE_TEST_KEY}'
                        status = 'PASSED'
                        comment = 'Smoke test executed from Jenkins'
                    }

                    \$tests += @{
                        testKey = '${params.REGRESSION_TEST_KEY}'
                        status = 'PASSED'
                        comment = 'Regression test executed from Jenkins'
                    }
                }

                \$xrayResult = @{
                    testExecutionKey = '${params.TEST_EXEC_KEY}'
                    tests = \$tests
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
                    powershell '''
                    Write-Host "Authenticating with Xray..."

                    $authBody = @{
                        client_id = $env:XRAY_CLIENT_ID
                        client_secret = $env:XRAY_CLIENT_SECRET
                    } | ConvertTo-Json

                    $token = Invoke-RestMethod `
                        -Uri "$env:XRAY_BASE_URL/api/v2/authenticate" `
                        -Method Post `
                        -Body $authBody `
                        -ContentType "application/json"

                    Write-Host "Uploading results to Xray..."

                    $response = Invoke-RestMethod `
                        -Uri "$env:XRAY_BASE_URL/api/v2/import/execution" `
                        -Method Post `
                        -Headers @{ Authorization = "Bearer $token" } `
                        -InFile "target/xray-result.json" `
                        -ContentType "application/json"

                    Write-Host "Xray Response:"
                    $response | ConvertTo-Json -Depth 10
                    '''
                }
            }
        }
    }
        post {
            always {
            junit allowEmptyResults: true, testResults: 'target/cucumber.xml'

        cucumber buildStatus: 'UNSTABLE',
                 fileIncludePattern: '**/cucumber.json',
                 jsonReportDirectory: 'target'

        archiveArtifacts artifacts: 'target/*.json, target/*.xml, target/*.html',
                         fingerprint: true,
                         allowEmptyArchive: true
        }
    }
}