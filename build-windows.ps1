Remove-Item -Path $PSScriptRoot\build -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory $PSScriptRoot\build
cd $PSScriptRoot\build
cmake -DCMAKE_CONFIGURATION_TYPES=Release -A x64 ../java
cmake --build . --config Release
Copy-Item ./Release/yoga.dll ../java-jvm/src/main/resources
New-Item -Force ../java-jvm/src/main/resources/yoga.dll.sha256 -Value ((Get-FileHash -Algorithm SHA256 ./Release/yoga.dll).Hash.ToLower())
