# Android framework,欢迎start与fork
this for android with framework
## 项目描述
此开源项目由本人主导持续开发(更希望有更多人的人参与进来),项目主要包括移动客户端([android](https://github.com/jiangshide/framework),[ios](https://github.com/jiangshide/ios),)与服务端([后台管理](https://github.com/jiangshide/backstage),[API](https://github.com/jiangshide/zd112_api)),以及基础数据分析(基于[spark](https://github.com/jiangshide/spark)输出)
当前项目分支为android开发提供基础框架,其基础框架主要包括基础UI模块(通用导航栏,通用加载刷新,通用对话实现,二维码相关,以及基础用到的view展示,更多请在view中查询),网络模块,消息模块,事件分发模块,安全控制与认证,数据适配模块,升级模块(热更新,普通更新),错误日志模块,HTML5交互模块,分享模块等,更多可看源码,或者我会给出相应的案例在demo中一一展示
## 项目目的
打造最简单与最全的客户端移动基础框架,兼容所有原生接口(本身是基于原生开发:同步原生接口兼容实现),让开发者使用最少的代码去最大化的实现项目需求,应变各种紧急项目快速输出
## 项目架构
## 项目案例
### 1.UI模块
#### 1.1 通用导航栏

#### 1.2 通用加载刷新控制
#### 1.3 通用对话框实现
#### 1.4 二维码实现
#### 1.5 通用手势密码实现
#### 1.6 通用滚轮实现
#### 1.7 高级按钮操作等
### 2.网络模块
### 3.消息模块
### 4.事件分发模块
### 5.安全控制与认证模块
### 6.数据适配模块
### 7.升级模块
### 8.错误日志模块
### 9.HTML5交互模块
### 10.分享模块

## 项目集成(非常优雅的集成,最大化灵活控制,只需要几个步骤)

1.在你的项目工程根目录所在的build.gradle中添加:maven { url "https://jitpack.io" }

        allprojects {
            repositories {
                google()
                jcenter()
                maven { url "https://jitpack.io" }//当前需要添加的
            }
        }
        
2.在你项目工程app所在目录下build.gradle中添加:implementation 'com.github.jiangshide:framework:1.0.1'

        dependencies {
            implementation fileTree(dir: 'libs', include: ['*.jar'])
            implementation 'com.android.support:appcompat-v7:28.0.0-alpha3'
            implementation 'com.android.support.constraint:constraint-layout:1.1.2'
            testImplementation 'junit:junit:4.12'
            androidTestImplementation 'com.android.support.test:runner:1.0.2'
            androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
            implementation 'com.github.jiangshide:framework:1.0.1'//当前需要添加的
        }
        
### 3.拷贝demo中gradle.properties文件覆盖自己项目中的gradle.properties,需要注意:
#### 3.1 如果打release包的话需要替换成自己的密钥文件与相关信息：
        KEY_FILE=demo
        KEY_ALIAS=jsd
        KEY_PASSWORD=jsddemo
        STORE_PASSWORD=jsddemo
#### 3.2 网络主机替换：需要替换成自己的IP或域名
    #the net config
    PRODUCTION=http://10.20.6.50:8091
    PREPRODUCTION=http://10.20.6.50:8091
    TEST=http://10.20.6.50:8091
    API=/api/zdb/
    UPDATE=update
#### 3.3 api协议配置,可替换成自己实际协议的名称
    #BaseData protocol
    CODE=code
    SUCCESS=success
    TIME=date
    MSG=msg
#### 3.4 可修改网络默认配置
##### the http params
    HTTP_CONNECT_TIME=30
    HTTP_READ_TIME=30
    HTTP_WRITE_TIME=30
    HTTP_MAX_CACHE_SIZE=10 * 1024 * 1024
##### the four type:1~FORCE_NETWORK;2~FORCE_CACHE;3~NETWORK_THEN_CACHE;4~CACHE_THEN_NETWORK
    HTTP_CACHE_TYPE=4
    HTTP_IS_GZIP=true
    HTTP_SHOW_LIFECYCLE_LOG=true
    
# 互动:请打赏点吧！
   ![Image text](https://raw.githubusercontent.com/jiangshide/framework/master/img/aliplay.png) ![Image text](https://github.com/jiangshide/framework/blob/master/img/weixinplay.png?raw=true)

<img src="https://raw.githubusercontent.com/jiangshide/framework/master/img/aliplay.png" width="300" height="300" alt="我是缩小后的图"></img>