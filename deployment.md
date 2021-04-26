# Deployment

## Deploy on the server

### Back end

#### 1. The first way : java -jar

```
1. ssh omsz@omszstatusly.southeastasia.cloudapp.azure.com

2. cd /home/omsz/Statusly

3. git pull // opt, update code

4. mvn clean package

5. nohup java -jar target/Statusly-0.1.0.jar > statusly.log  2>&1 &
```

#### 2. The second way: hot deployment

```
1. ssh omsz@omszstatusly.southeastasia.cloudapp.azure.com

2. cd /home/omsz/Statusly

3. nohup mvn spring-boot:run  > statusly.log  2>&1 &
```

#### You should make sure that these files are correct.

##### 1. modify application.properties

```
aad.redirectUriSignin=https://weekly.omsz.io:8080/login/secure/aad
```

##### 2. modify  application.yml

```
password: microsoft123
```

##### 3. modify success.html

```
window.opener.postMessage(res, "https://weekly.omsz.io/signin");
```

> Because the back end service is running in the background, if you want to stop, you need kill the process.

### Front end

```
1. ssh omsz@omszstatusly.southeastasia.cloudapp.azure.com

2. cd Statusly_UI

3. npm run build
```

#### You should make sure that these files are correct.

##### 1. modify constants.js

```
export const API_ROOT = 'https://weekly.omsz.io:8080';
```

##### 2. modify  setupProxy.js

```
let API_ROOT = 'https://weekly.omsz.io:8080';
```



## Deploy locally

### UI deployment

1. install lastest npm

- on Ubuntu, try "sudo apt-get install curl" then "curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -", lastly do "sudo apt-get install nodejs"
- on Windows use the installer

2. pull UI code from liangkuo branch
3. run command "npm install"
4. run command "npm start"

### Backend local deployment

1. pull backend code from master branch
2. change the database url, username and password to your own in /Statusly/src/main/resources/application.yml
3. run spring boot starter

### Build local database

In mysql terminal:

1. ```  create database statusly  ```
2. import /database/statusly.sql