# OKare-Development-Kit
开发红石电路的利器！  
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
### Monitor（监视器）功能
1.功能介绍  
+ Monitor功能是一种基于游戏时刻进行的监视功能。玩家可以创建端口和监视器，然后把端口加入监视器。监视器开始工作后将记录各个端口的变化，并且输出到一个记事本里。  

2.Port（端口）指令
+ 创建位置在OKDPOS上，名字为name，类型为type的端口```/odk port set <name> [<type>]```  
  其中端口类型有digital，analog，flag三类。digital端口只输出数字信号值，即0或1；analog端口只输出模拟信号值，即0-15；flag端口是监视器开始或结束的标志端口，该端口上的方块一旦变化就会输出true，否则为false。
+ 将名字为name的端口重置到ODKPOS上，并将类型重置为type```/odk port reset <name> [<type>]```  
  若不输入类型，则端口类型不变
+ 将名字为old name的端口重命名为new name```/odk port rename <old name> <new name>```
+ 移除名字为name的端口```/odk port remove <name>```  
如果想移出所有端口那就在name处输入all即可；与此同时，监视器中相应的端口也会被移除
+ 显示名字为name的端口的详细信息```/odk port showDetails <name>```
+ 获取当前名字为name的端口的值```/odk port get <name>```
+ 查看目前所有的端口```/odk port list```
+ 将自己传送到名字为name的端口处```/odk port tp <name>```

3.Monitor指令
+ 创建一个名字为name的监视器```/odk monitor create <name>```
+ 将名字为port name的端口加入名字为name的监视器中```/odk monitor add <name> <port name>```
+ 将名字为port name的端口从名字为name的监视器中移除```/odk monitor remove <name> <port name>```
+ 将名字为name的监视器删除```/odk monitor delete <name>```  
如果想删除所有监视器那就在name处输入all即可
+ 重置名字为name的监视器```/odk monitor reset <name>```
重置后该监视器的端口将被全部移除
+ 将名字为old name的监视器重命名为new name```/odk monitor rename <old name> <new name>```
+ 将名字为flag name的端口设置为名字为name的监视器的启动标志```/odk monitor setStartFlag <name> <flag name>```
+ 将名字为flag name的端口设置为名字为name的监视器的停止标志```/odk monitor setStopFlag <name> <flag name>```
+ 激活名字为name的监视器的标志```/odk monitor flagEnable <name>```
+ 抑制名字为name的监视器的标志```/odk monitor flagDisable <name>```
+ 使名字为name的监视器开始监视```/odk monitor start <name>```
+ 使名字为name的监视器停止监视```/odk monitor stop <name>```
+ 显示名字为name的监视器的详细信息```/odk monitor showDetails <name>```
+ 查看所有的监视器```/odk monitor list```
+ 将名字为name的监视器的监视数据保存到游戏文件夹下的config\odk\monitorData\filename.txt中```/odk monitor saveData <name> <filename>```
保存的默认文件名为name.txt

4.Monitor配置方法
+ 先用odk工具或```/odk pos```选择ODKPOS作为所需端口坐标，然后通过```/odk port set <name> [<type>]```把它设置为端口，从而完成端口的选择
+ 用```/odk monitor create <name>```创建监视器
+ 用```/odk monitor add <name> <port name>```将所需端口加入监视器中

5.Monitor启动基础方法
+ 配置完成后，用```/odk monitor start <name>```即可使监视器开始监视
+ 用```/odk monitor stop <name>```即可停止监视

6.Monitor的标志启动方法
+ 标志可以是digital，analog或flag类型的端口。Digital类型的标志仅在数字信号值变化时触发；analog类型的标志仅在模拟信号值变化时触发；flag类型的标志仅在方块变化时触发
+ 从设置的端口中选择一个端口作为开始标志，用```/odk monitor setStartFlag <name> <flag name>```设置监视器的开始标志，若作为开始标志的端口原先不在监视器中，则它会被加入监视器中，成为监视对象
+ 从设置的端口中选择一个端口作为停止标志，用```/odk monitor setStopFlag <name> <flag name>```设置监视器的停止标志，若作为停止标志的端口原先不在监视器中，则它会被加入监视器中，成为监视对象
+ 设置完成后，需要用```/odk monitor flagEnable <name>```激活标志，此时标志可以被触发，一旦触发，监视器将开始或停止
+ 如果不需要用标志了，可以用```/odk monitor flagDisable <name>```使监视器的标志被抑制，此时标志不会被触发

7.监视器数据的保存
+ 完成监视后，数据可以导出并以记事本的形式保存到游戏文件夹下的config\odk\monitorData中
+ 用```/odk monitor saveData <name> <filename>```保存，默认的文件名是监视器的名字，相同的文件会直接替换
+ 导出的文件包含各种信息，[点击](https://github.com/Dylan-wg/OKare-Development-Kit/blob/2.0/run/config/odk/monitorData/demo.txt)查看示例文件

8.更详细的使用教程
+ 日后会由[B站@Dylan-wg](https://space.bilibili.com/700277869)发布
### 下载地址
+ 1.2版本：[百度网盘](https://pan.baidu.com/s/1IgxeAI2ur6S4uMVsoWQX7g) 提取码：kkdb
+ 2.0版本：[百度网盘](https://pan.baidu.com/s/1SUAM4fnNlAaN9_gabvZ-fw) 提取码：cqke  
