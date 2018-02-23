@echo OFF
SET DEVELOPMENT_HOME=%cd%

:::make sure git client is installed in the local machine.
cd /d %DEVELOPMENT_HOME%
cd..
cd data
call git reset --hard origin/master
call git pull 
echo 'GIT data pull completed...'
call timeout 45 > NUL
cd %DEVELOPMENT_HOME%
cd..
cd core
call git reset --hard origin/master
call git pull
echo 'GIT core pull completed...'
call timeout 45 > NUL
cd %DEVELOPMENT_HOME%
cd..
cd view
call git reset --hard origin/master
call git pull
echo 'GIT view pull completed...'
call timeout 45 > NUL
cd /d %DEVELOPMENT_HOME%
cd..
cd data
call mvn clean
call mvn install -Dmaven.test.skip=true
call timeout 30 > NUL
cd %DEVELOPMENT_HOME%
cd..
cd core
call mvn clean 
call mvn install 
call timeout 30 > NUL
cd %DEVELOPMENT_HOME%
cd..
cd core\target\
call mvn install:install-file -DgroupId=com.agility.ddp.core -DartifactId=core-base -Dversion=9.3.0 -Dfile=core-base-9.3.0.jar -Dpackaging=jar -Dgeneration=true
call mvn install:install-file -DgroupId=com.agility.ddp.core -DartifactId=core-ddp -Dversion=9.3.0 -Dfile=core-ddp-9.3.0.jar -Dpackaging=jar -Dgeneration=true

cd %DEVELOPMENT_HOME%
cd..
cd view
call mvn clean 
call mvn install 
call timeout 30 > NUL 
set workspace="D:\BuildBackup\UAT"
set DDPConfigLoc99="DDPConfig-99"
set DDPConfigLoc100="DDPConfig-100"
set Day=%Date:~4,2%
set Mth=%Date:~7,2%
set Yr=%Date:~10,4%
::REM Get Hour and Min from %Time%
set Hour=%Time:~0,2%
::if "%hour:~0,1%" == " " set hour=0%hour:~1,1%
set Min=%Time:~3,2%
set sec=%Time:~6,2%
cd /d %workspace%
mkdir %DAY%-%Mth%-%Yr%-%Hour%-%Min%-%sec%
call robocopy \\10.201.81.99\ddp %DAY%-%Mth%-%Yr%-%Hour%-%Min%-%sec% /move ddp.war
call robocopy \\10.201.81.100\ddp %DAY%-%Mth%-%Yr%-%Hour%-%Min%-%sec% /move core-ddp.war

cd /d %workspace%\%DAY%-%Mth%-%Yr%-%Hour%-%Min%-%sec%
mkdir %DDPConfigLoc99%
call robocopy \\10.201.81.99\ddp\DDPConfig %DDPConfigLoc99% /move *.properties
mkdir %DDPConfigLoc100%
call robocopy \\10.201.81.100\ddp\DDPConfig %DDPConfigLoc100% /move *.properties

cd %DEVELOPMENT_HOME%
cd target\
call robocopy %DEVELOPMENT_HOME%\target "\\10.201.81.99\ddp" ddp.war
call robocopy %DEVELOPMENT_HOME%\target "\\10.201.81.100\ddp" core-ddp.war

cd %DEVELOPMENT_HOME%
cd..
cd core\src\main\resources
call robocopy . "\\10.201.81.99\ddp\DDPConfig" custom.properties
call robocopy . "\\10.201.81.99\ddp\DDPConfig" ddp.properties
call robocopy . "\\10.201.81.99\ddp\DDPConfig" export.properties
call robocopy . "\\10.201.81.99\ddp\DDPConfig" mail.properties
call robocopy . "\\10.201.81.99\ddp\DDPConfig" multiaedmail.properties
call robocopy . "\\10.201.81.100\ddp\DDPConfig" custom.properties
call robocopy . "\\10.201.81.100\ddp\DDPConfig" ddp.properties
call robocopy . "\\10.201.81.100\ddp\DDPConfig" export.properties
call robocopy . "\\10.201.81.100\ddp\DDPConfig" mail.properties
call robocopy . "\\10.201.81.100\ddp\DDPConfig" multiaedmail.properties

cd %DEVELOPMENT_HOME%
cd..
cd view\src\main\resources
call robocopy . "\\10.201.81.99\ddp\DDPConfig" common.properties
call robocopy . "\\10.201.81.100\ddp\DDPConfig" common.properties
echo :::::::::::::::::::: Successfully completed UAT build process::::::::::::::::::::::::::::
exit