# OKare-Development-Kit
开发红石CPU的利器！  
这里是最新的2.0版本  

## 使用说明
### 使用环境
目前仅支持Minecraft 1.19，需要fabric 0.14.9，fabric api 0.58.0
### 模组基础指令
1.显示模组信息：```/odk ```  
2.得到odk工具：```/odk tool ```   
3.显示ODKPOS：```/odk showPos```    
4.将玩家坐标设定为ODKPOS：```/odk pos```    
5.从ODKPOS开始向下给ROM写入大小为8bits的bits：```/odk writeROM <bits> ```   
6.从ODKPOS开始向下给RAM写入大小为8bits的bits：```/odk writeRAM <bits> ```   
7.从ODKPOS开始向下清除8bits数据：```/odk clear```  
### odk工具
1.odk工具是铁锹，可以用指令获得  
2.当玩家主手持工具时
+ 对着充能或未充能的中继器按右键，中继器将变为未充能或充能，即写入RAM
+ 对着白色玻璃或观察者按右键，其将变为观察者或白色玻璃，即写入ROM
+ 对着方块按左键，该方块的坐标将被选为ODKPOS，而方块不会被破坏
+ 在潜行状态下使用会实现原版铁锹的某些功能
### Monitor功能
1.功能介绍  
+ Monitor是一种基于游戏时刻进行的监视功能。玩家可以创建端口和监视器，然后把端口加入监视器。监视器开始工作后将记录各个端口的变化，并且输出到一个记事本里。  

2.Port指令
+ 创建位置在OKDPOS上，名字为name，类型为type的端口```/odk port set <name> [<type>]```  
  其中端口类型有digital，analog，flag三类
+ 将名字为name的端口重置到ODKPOS上，并将类型重置为type```/odk port reset <name> [<type>]```  
  若不输入类型，则端口类型不变
+ 将名字为old name的端口重命名为new name```/odk port rename <old name> <new name>```
### 下载地址
+ 1.2版本：[百度网盘](https://pan.baidu.com/s/1IgxeAI2ur6S4uMVsoWQX7g) 提取码：kkdb
+ 2.0版本：[百度网盘](https://pan.baidu.com/s/1-Xw53dvXZDwR0pzEPB8FvQ) 提取码：0862  
