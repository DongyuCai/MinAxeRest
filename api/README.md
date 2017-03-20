## 工程导入eclipse、myeclipse、idea
* 如果是eclipse 先执行 mvn clean eclipse:eclipse
* 然后执行 mvn clean package
* 默认是dev环境，使用的是src/main/resource/config/dev下的配置文件
* 如果需要使用其他环境配置文件，如生产环境，执行 mvn clean package -Pprod