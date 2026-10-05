param([Parameter(Mandatory=$true)][string]$ModsDirectory)
$ErrorActionPreference='Stop'
$projectRoot=Split-Path -Parent $PSScriptRoot
$source=(Resolve-Path -LiteralPath $ModsDirectory).Path
$target=Join-Path $projectRoot 'libs'
[void](New-Item -ItemType Directory -Path $target -Force)
Add-Type -AssemblyName System.IO.Compression.FileSystem
function Copy-Required([string]$pattern,[string]$destinationName=''){
    $matches=@(Get-ChildItem -LiteralPath $source -File -Filter $pattern)
    if($matches.Count -ne 1){throw "Expected exactly one $pattern in $source"}
    if(-not $destinationName){$destinationName=$matches[0].Name}
    $destination=Join-Path $target $destinationName
    if(-not $matches[0].FullName.Equals($destination,[StringComparison]::OrdinalIgnoreCase)){
        Copy-Item -LiteralPath $matches[0].FullName -Destination $destination -Force
    }
    return $matches[0].FullName
}
$create=Copy-Required 'create-*6.0.10*.jar' 'create-6.0.10.jar'
[void](Copy-Required 'createbigcannons-*5.11.7*.jar' 'createbigcannons-5.11.7+mc.1.21.1.jar')
[void](Copy-Required 'ritchiesprojectilelib-*.jar')
function Extract-Nested([string]$archive,[string]$pattern){
    $zip=[IO.Compression.ZipFile]::OpenRead($archive)
    try{
        foreach($entry in $zip.Entries | Where-Object {$_.FullName -like $pattern}){
            $name=[IO.Path]::GetFileName($entry.FullName)
            [IO.Compression.ZipFileExtensions]::ExtractToFile($entry,(Join-Path $target $name),$true)
            Write-Output "Prepared: $name"
        }
    }finally{$zip.Dispose()}
}
Extract-Nested $create 'META-INF/jarjar/*.jar'
$bundled=@(Get-ChildItem -LiteralPath $source -File -Filter 'create-aeronautics-bundled-1.21.1-1.3.2.jar')
if($bundled.Count -eq 1){Extract-Nested $bundled[0].FullName 'META-INF/jarjar/*simulated*.jar'}
else{
    $simulated=@(Get-ChildItem -LiteralPath $source -File | Where-Object Name -Match 'simulated.*1\.3\.2.*\.jar$')
    if($simulated.Count -ne 1){throw 'Compile-time honey-glue API requires Simulated 1.3.2 or Aeronautics bundled 1.3.2.'}
    Copy-Item -LiteralPath $simulated[0].FullName -Destination (Join-Path $target $simulated[0].Name) -Force
}
foreach($file in Get-ChildItem -LiteralPath $source -File -Filter 'sable-*.jar'){
    Copy-Item -LiteralPath $file.FullName -Destination (Join-Path $target $file.Name) -Force
    Extract-Nested $file.FullName 'META-INF/jarjar/*.jar'
}
Write-Output 'Dependencies prepared locally. They will not be included in the addon JAR.'
