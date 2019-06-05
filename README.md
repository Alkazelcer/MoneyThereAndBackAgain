# MoneyThereAndBackAgain
Simple restful service for money transfer

Repository has two projects:
1. Money transfer server;
2. simple client which creates several accounts during startup and sends requests every 3 seconds.

To lunch server:
1. Build with maven;
2. maven shade plugin creates fat jar that could be run with command java -jar xxx.jar [server config_file]. Arguments server and config_file could be ommited and it would lunch on localhost:8080

To lunch client:
1. Build with maven;
2. maven shade plugin creates fat jar that could be run with command java -jar xxx.jar. Client automatical sends http request to localhost:8080.

TODO:
1. Configurable client;
2. more configurations to server;
3. add proper logging with logback\log4j;
4. add models for Money(amount, currency, etc), account information, read\modify roles, etc;
5. currency exchange service;
6. TBD.
