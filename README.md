# Simulator
The third project for the [MO810](http://www.ic.unicamp.br/~esther/teaching/2017s1/mc959/index.html) class.

#Members:
* Mateus Coradini Santos - @[mateuscoradini](https://github.com/mateuscoradini)
* Luísa Cardoso Madeira - @[luwood](https://github.com/luwood)
* Guilherme Carreiro - @[karreiro](https://github.com/karreiro)

## Summary
This project implements a robot that searches for plants.

## Depencencies
- JDK 1.8
- Maven 3.X
- OpenCV 3
- V-REP 3.3.2

## Setup
Since the OpenCV does not have a remote Maven repository. You need to install it locally.

Execute the following command:
```
mvn install:install-file -Dfile=<OPENCV_JAR_PATH> -DgroupId=org.opencv -DartifactId=opencv -Dversion=3.2.0 -Dpackaging=jar
```
Note: It's important to replace the `<OPENCV_JAR_PATH>` by the `.jar` path, e.g., `/usr/local/opt/opencv3/share/OpenCV/java/opencv-320.jar`.

### Install the Fuzzylite library
```
cd /tmp
wget https://github.com/fuzzylite/jfuzzylite/archive/v6.0.zip
unzip v6.0.zip
cd jfuzzylite-6.0/jfuzzylite
mvn clean install
```

## Building
```
mvn clean install -DargLine="-Djava.library.path=<OPENCV_PATH>"
```
Note: It's important to replace the `<OPENCV_PATH>` by the opencv path, e.g., `/usr/local/opt/opencv3/share/OpenCV/java`.

## Running on Intellij
Go to `Run` > `Edit Configurations...` > `VM Options` and add:
```
-Djava.library.path=<OPENCV_PATH>:<VREP_PATH>
```
Note: It's important to replace the `<OPENCV_PATH>` and the `<VREP_PATH>` by the correct values, e.g., ` -Djava.library.path=/Users/karreiro/Projects/mo810/vrep/programming/remoteApiBindings/java/lib:/usr/local/opt/opencv3/share/OpenCV/java`

## License
This project is released under MIT License.

Check [LICENSE](https://github.com/mateuscoradini/VRepSimulator/blob/master/LICENSE.txt) file for more information.
