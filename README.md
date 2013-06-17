spider
======

a simple distributed spider in Java. 

Java编写的一个简单分布式爬虫.

dependency:com.meiya.base.mipatch download from https://github.com/matuobasyouca/e-shark-k;

依赖com.meiya.base.mipatch 移步 https://github.com/matuobasyouca/e-shark-k;




start task server(port :  30010,30020,30030,30090):
java com.zhangwoo.spider.server.TaskCenter



start result server(port :  30040):
java com.zhangwoo.spider.server.ResultCenter



start client:
java com.zhangwoo.spider.client.process.SpiderProcess



client has 2 args:
java com.zhangwoo.spider.client.process.SpiderProcess TaskCenterIp ResultCenterIp
if TaskCenterIp==ResultCenterIp
java com.zhangwoo.spider.client.process.SpiderProcess CenterIp
