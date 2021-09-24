## Spring batch Tutorial by Kotlin
Kotlin으로 구성한 Spring Batch Sample Code 이다.



### Job 실행
```shell
--spring.batch.job.names=json2DbJob,db2dbJob --spring.batch.job.enabled=true
```
- 복수개의 job 실행시 job 이름을 아래와 같이 comma separate 로 입력한다.
- application.yml 에서 spring.batch.job.enabled=false 설정했을 경우 true 입력을 해준다. 
