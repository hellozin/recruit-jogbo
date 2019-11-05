# Recruit Jogbo

학과 내 취업 후기, 취업 팁을 공유하는 커뮤니티 Recruit Jogbo의 API 서버입니다.

## Project Run

```
git clone https://github.com/hellozin/recruit-jogbo.git
```

```
cd recruit-jogbo
```

recruit-jogbo/ 루트 디렉토리에 아래 3개의 env 파일을 생성하세요. 

rabbit-properties.env
```
RABBITMQ_DEFAULT_USER=YOUR_USERID
RABBITMQ_DEFAULT_PASS=YOUR_PASSWD
RABBITMQ_DEFAULT_VHOST=/local
```

recruit-jogbo-properties.env
```
SPRING_PROFILES_ACTIVE=local
JWT_TOKEN_CLIENT_SECRET=YOUR_CLIENT_SECRET
```

recruit-jogbo-rabbit-properties.env
```
SPRING_RABBITMQ_HOST=rabbit-mq
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=YOUR_USERID
SPRING_RABBITMQ_PASSWORD=YOUR_PASSWD
SPRING_RABBITMQ_VIRTUAL_HOST=/local
```