
# bootstrap_wrapper.ps1
# Purpose: Download Gradle 8.7 portable and generate Gradle Wrapper files if missing.
# Usage:
#   Set-ExecutionPolicy -Scope Process Bypass -Force
#   .\bootstrap_wrapper.ps1
#   .\gradlew.bat assembleDebug

$ErrorActionPreference = "Stop"

$gradleVersion = "8.7"
$distUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
$tempDir = Join-Path $env:TEMP "gradle_bootstrap_$gradleVersion"
$zipPath = Join-Path $tempDir "gradle-$gradleVersion-bin.zip"

if (Test-Path ".\gradlew.bat") {
  Write-Host "gradlew.bat already exists. Skipping bootstrap."
  exit 0
}

if (!(Test-Path $tempDir)) { New-Item -ItemType Directory -Path $tempDir | Out-Null }

Write-Host "Downloading Gradle $gradleVersion ..."
Invoke-WebRequest -Uri $distUrl -OutFile $zipPath

Write-Host "Extracting..."
$extractDir = Join-Path $tempDir "gradle-$gradleVersion"
if (Test-Path $extractDir) { Remove-Item -Recurse -Force $extractDir }
Expand-Archive -Path $zipPath -DestinationPath $tempDir -Force

$gradleBat = Join-Path $extractDir "bin\gradle.bat"
if (!(Test-Path $gradleBat)) {
  $maybe = Get-ChildItem -Path $tempDir -Directory | Where-Object { $_.Name -like "gradle-*" } | Select-Object -First 1
  if ($maybe) { $gradleBat = Join-Path $maybe.FullName "bin\gradle.bat" }
}
if (!(Test-Path $gradleBat)) { throw "gradle.bat not found after extraction." }

Write-Host "Running 'gradle wrapper --gradle-version $gradleVersion' in $(Get-Location)"
& $gradleBat wrapper --gradle-version $gradleVersion

Write-Host "Done. Wrapper files should exist now (gradlew, gradlew.bat, gradle/wrapper/*)."
