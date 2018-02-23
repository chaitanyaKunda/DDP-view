@echo OFF
SET DEVELOPMENT_HOME=%cd%
set workspace = "D:\Build Backup\UAT"
::Rem Get Day,Mth & Year from %Date%
::set Day=%Date:~0,2%
::set Mth=%Date:~3,2%
::set Yr=%Date:~6,4%
::REM Get Hour and Min from %Time%
::set Hour=%Time:~0,2%
::if "%hour:~0,1%" == " " set hour=0%hour:~1,1%
::set Min=%Time:~3,2%
::set sec=%Time:~6,2%
::cd /d %workspace%
::mkdir %DAY%-%Mth%-%Yr%-%Hour%-%Min%-%sec%
::move \\10.201.81.99\ddp\ddp.war %DAY%-%Mth%-%Yr%-%Hour%-%Min%-%sec%
::move \\10.201.81.100\ddp\core-ddp.war %BUILD%\%DAY%-%Mth%-%Yr%-%Hour%-%Min%-%sec%

cd /d %DEVELOPMENT_HOME%
cd..
cd data
call mvn clean
call mvn install -Dmaven.test.skip=true

cd %DEVELOPMENT_HOME%
cd..
cd core
call mvn clean 
call mvn install 

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
::cd %DEVELOPMENT_HOME%
::cd target\
::call copy ddp.war "\\10.201.81.99\ddp"
::call copy core-ddp.war "\\10.201.81.100\ddp"

pause