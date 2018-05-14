#  Spring boot session setup with Redis example

### Local Redis docker container can be started
```bash
docker run --name my-redis -d -p 6379:6379 redis
```

- [Index page](http://localhost:8080) should display value in session
- Session info is available [in this actuator endpoint](http://localhost:8080/sessions)

### See appliation.properties for redis host and port. 
### Provision CF service, and make sure that manifest.yml is pointing to a proper file.
### Continues load can be applied as
```bash
while true; do sleep 1; curl -I -X GET http://redis-sessions.cfapps.pez.pivotal.io//; echo -e '\n\n\n\n'$(date);done
```
### Verify that keys are indeed loaded to redis
```bash
Alexs-MBP:redis-sessions $ redis-cli -h 127.0.0.1 -p 6379
127.0.0.1:6379>config set notify-keyspace-events Egx
127.0.0.1:6379>keys "spring:session:*"
1) "spring:session:sessions:expires:fc17b0a2-5af4-4304-9214-23a772d9bb37"
2) "spring:session:sessions:fc17b0a2-5af4-4304-9214-23a772d9bb37"
3) "spring:session:expirations:1479756360000"
```

### To clear all data in redis
```bash
127.0.0.1:6379>flushall
```
