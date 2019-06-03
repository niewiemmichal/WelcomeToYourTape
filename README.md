# Welcome To Your Tape

Have you ever wondered why our academic world is so cruel and full of injustice? Have you ever wanted to spit at this one guy, who constantly thinks, that his subject is the most important thing on you road to great career? Or maybe you wanted to appreciate this other guy, who actually knows how to share knowledge in an interesting and pleasent way?

If so, then this service is your answer!

Give feedback, share opinions, rate subjects and show them why your life has became worse than ever before.

![hannah](https://media1.tenor.com/images/67823ac0e306976132d83ad632624957/tenor.gif)

## How to run
Two options are available to run the application. 

### Running with Docker
To run the application with Docker run following commands in project root directory. Running with Docker requires MySQL database named "welcometoyourtape".

```
./gradlew build
```
```
docker build -t wtyt-application .
```
```
docker run -p 8080:8080 wtyt-application \
  -e DB_NAME="welcometoyourtape" \
  -e DB_USERNAME="<username>" \
  -e DB_PASSWORD="<password>" \
  -e DB_URI="<uri with port>"
```


### Running with Wildfly
To run the application with Wildfly first build the war, change its name to `app.war` and then copy it to Wildfly deployment folder. Application requires `java:/jdbc/datasources/welcometoyourtapeDS` datasource.
```
./gradlew build
```




