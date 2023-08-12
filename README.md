# OKare-Development-Kit
开发红石CPU的利器！  
这里是1.2版本  
最新版本是[2.0](https://github.com/Dylan-wg/OKare-Development-Kit/tree/2.0)

## 使用说明
### 使用环境
目前仅支持Minecraft 1.19，需要fabric 0.14.9，fabric api 0.58.0
### 模组指令
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
### 下载地址
+ 1.2版本：[百度网盘](https://pan.baidu.com/s/1IgxeAI2ur6S4uMVsoWQX7g) 提取码：kkdb
+ 2.0版本：[百度网盘](https://pan.baidu.com/s/1S6hBLywoxwjJImW8sDlk-w) 提取码：xcf4  
