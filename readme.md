##### 查询用户存活接口
get:/user/list

##### 向用户推送消息接口(username为空时，向所有人发送)
post:/message/sendToClient/{username}
data:{'message':'111'}
example: curl -X 'POST' -d "{'message':'111'}" http://localhost:8080/message/sendToClient

#### 模拟客户端向服务端发送(username为空时，所有人发送)
post:/message/sendToServer/{username}
data:{'message':'111'}
example: curl -X 'POST' -d "{'message':'111'}" http://localhost:8080/message/sendToServer

### 