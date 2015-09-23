d:
cls
cd "d:\Repositories\source code\git\utility\common"
call mvn clean install -Dmaven.test.skip=true

cls
cd "d:\Repositories\source code\git\system\root\_pom"
call mvn clean install -Dmaven.test.skip=true

cls
cd "d:\Repositories\source code\git\ui\_pom"
call mvn clean install -Dmaven.test.skip=true

cls
cd "d:\Repositories\source code\git\system\company\_pom"
call mvn clean install -Dmaven.test.skip=true

cls
cd "d:\Repositories\source code\git\system\ricciepsilone\_pom"
call mvn clean install -Dmaven.test.skip=true