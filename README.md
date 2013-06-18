spider
======

a simple distributed spider in Java. 

Java编写的一个简单分布式爬虫.支持模板插件化，以xml形式或java编码形式编写。

dependency:com.meiya.base.mipatch download from https://github.com/matuobasyouca/e-shark-k;

依赖com.meiya.base.mipatch 移步 https://github.com/matuobasyouca/e-shark-k;




第一步：
安装java环境与mysql数据库（任务、日志、结果存储）

第二步：
编译代码

第三步：
修改config下的config.ini文件
user、pwd、url、driver分别为数据库连接相关字段

saveOneTable=0表示会根据域名不同存储在不同的表中（tieba.baidu.com）
saveOneTable=1表示全部存储在conversation表中。

第四步：
可以启动服务端以及客户端了。
服务端：
java com.zhangwoo.spider.server.TaskCenter 任务中心
java com.zhangwoo.spider.server.ResultCenter 结果中心

客户端：
java com.zhangwoo.spider.client.process.SpiderProcess
客户端可以传入两个参数  java com.zhangwoo.spider.client.process.SpiderProcess TaskCenterIp ResultCenterIp
如果TaskCenterIp和ResultCenterIp相同，只需要传一个就可以了。

第五步：
从服务端访问 http://localhost:30090/spider/可以查看状态

添加任务：
http://localhost:30090/spider/managerTask.jsp 填写任务名称和任务地址就能够添加一个任务。
只要有插件支持就可以解析并返回结果入库。

