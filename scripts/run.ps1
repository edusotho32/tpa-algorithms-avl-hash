$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$output = Join-Path $root 'build\classes'
New-Item -ItemType Directory -Force -Path $output | Out-Null

$sources = Get-ChildItem -LiteralPath (Join-Path $root 'src\main\java') -Recurse -File -Filter *.java | ForEach-Object FullName
javac -encoding UTF-8 -d $output $sources
java -cp $output br.com.eduardothomaz.algorithms.Demo
