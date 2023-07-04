@echo off

pushd %~dp0\..\..\..\..\Projects\Personal\GradleBuildTimeAnalyzer\GradleBuildTimeAnalyzer
call gradlew fatJar sourcesJar --console=plain
popd
