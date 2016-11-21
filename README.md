#  Spring boot session setup with Redis example

### See appliation.properties for redis host and port. 
### Provision CF service, and make sure that manifest.yml is pointing to a proper file.
### Continues load can be applied as
```java
while true; do sleep 1; curl -I -X GET http://redis-sessions.cfapps.pez.pivotal.io//; echo -e '\n\n\n\n'$(date);done
```
### Verify that keys are indeed loaded to redis
```bash
Alexs-MBP:redis-sessions $ redis-cli -h 192.168.99.100 -p 6379
192.168.99.100:6379> keys "spring:session:*"
1) "spring:session:sessions:expires:fc17b0a2-5af4-4304-9214-23a772d9bb37"
2) "spring:session:sessions:fc17b0a2-5af4-4304-9214-23a772d9bb37"
3) "spring:session:expirations:1479756360000"
```
### Local Redis docker container
Can be started 
```
docker run --name my-redis -d -p 6379:6379 redis
```