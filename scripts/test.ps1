$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$output = Join-Path $root 'build\test-classes'
New-Item -ItemType Directory -Force -Path $output | Out-Null

$sources = @(
    Get-ChildItem -LiteralPath (Join-Path $root 'src\main\java') -Recurse -File -Filter *.java
    Get-ChildItem -LiteralPath (Join-Path $root 'src\test\java') -Recurse -File -Filter *.java
) | ForEach-Object FullName

javac -encoding UTF-8 -d $output $sources
java -ea -cp $output br.com.eduardothomaz.algorithms.AlgorithmTest
