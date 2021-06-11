
# Build requirements

* Java 11
* Node.js

## Quick Start
```
git clone https://github.com/yanagishima/yanagishima.git
cd yanagishima
git checkout -b [version] refs/tags/[version]
./gradlew distZip
cd build/distributions
unzip yanagishima-[version].zip
cd yanagishima-[version]
vim config/application.yml
nohup bin/yanagishima-start.sh >y.log 2>&1 &
```
see http://localhost:8080/

# Stop
```
bin/yanagishima-shutdown.sh
```
