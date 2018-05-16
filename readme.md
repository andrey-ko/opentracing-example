## OpenTracing example 

### requirements:
- jdk 10
- docker

### running example:
```bash
# start jaeger docker conatiner
./gradlew :runJaegerContainer  

# install app
./gradlew :installDist

# run app
./.out/install/opentracing-example/bin/opentracing-example

# open link 'http://localhost:16686' in browser

# remove jaeger docker conatiner 
./gradlew :stopJaegerContainer
```