
# Build requirements

* Java 11
* Node.js

## Quick Start
```
cd code
./gradlew distZip
cd build/distributions
unzip yanagishima-22.zip
cd yanagishima-22
vim config/application.yml
nohup bin/yanagishima-start.sh >y.log 2>&1 &
```
see http://localhost:8080/

# Stop
```
bin/yanagishima-shutdown.sh
```
