# Technology Stack
ID | Platform | Function |  Lnguage  | Build Status
 -------- | -------- | ------------ |  ------------ | ------------
 1  |   Android | [Framework](https://github.com/jiangshide/framework) | [Java](https://github.com/jiangshide/framework) [kotlin](https://github.com/jiangshide/kotlin_android) | [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-android.svg?branch=master)](https://github.com/jiangshide/framework)
 2  |   Ios | [Framework](https://github.com/jiangshide/ios) |	OC Swift	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/ios)
 3  |   Flutter | [Flutter](https://github.com/jiangshide/zd112_flutter) | [Dart](https://dart.dev/) [flutter](https://flutter.dev/) | [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/zd112_flutter)
 4  |   Web | [Backstage](https://github.com/jiangshide/backstage) | 	[Golang](https://github.com/jiangshide/backstage) [JS](https://github.com/jiangshide/backstage_js)	|	[![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/backstage)
 5  |   Api | [Interface](https://github.com/jiangshide/zd112_api) |	Golang	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/zd112_api)
 6  |   Spark | [Analysis](https://github.com/jiangshide/analysis) |	Scala	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/analysis)
 7  |   Block Chain | [Identification](https://github.com/jiangshide/idendification) |	Golang	| [![Build Status](https://travis-ci.org/Bilibili/ci-ijk-ffmpeg-ios.svg?branch=master)](https://github.com/jiangshide/idendification) 
 
# Base framework 
Platfor |	Module | Status	|	Open Level
 -------- | ------------ |  ------------ |  ------------ 
 Android | UI模块 | 基础完成	|	低(可定制)
 Android | 网络模块(http,socket) |	 基础完成		|	低(可定制)
 Android | 消息模块 | 	开发中	|	需定制
 Android | 事件分发模块 |	开发中	|	需定制
 Android | 安全控制与认证模块 |	开发中	|	需定制
 Android | 数据适配模块 |	基础完成	|	低(可定制)
 Android | 升级模块 |		基础完成	|	中(可定制)
 Android | 错误日志模块(java,native) | 	基础完成	|	低(可定制)
 Android | HTML5交互模块 |	基础完成	|	中(可定制)
 
## 项目目的
打造最简单与最全的移动客户端基础框架,兼容所有原生接口,让开发者使用最少的代码去最大化的实现项目需求,应变各种紧急项目快速迭代输出，让开发者只需要专注于业务的实现,客户端不限于设计模式,欢迎start
## 项目架构
   ![Image](https://github.com/jiangshide/framework/blob/master/img/android_frame.svg)
## 项目案例 [基于kotlin应用集成](https://github.com/jiangshide/Smallloan)
   ![image](https://github.com/jiangshide/framework/blob/master/img/video.gif)
### 1.UI模块(通用导航栏,通用加载刷新,通用对话实现,二维码相关,以及基础用到的view...)
#### 1.1 通用导航栏
##### 1.1.1 底部通用导航栏实现:可滑动
```Java
    public class MainActivity extends BaseActivity {
        @Override
        protected void initView(Bundle savedInstanceState) {
            setView(mNavigationBar.initView(getSupportFragmentManager(), new HomeFragment(), new CircleFragment(), new PulishFragment(), new ShopFragment(), new MineFragment()).initData(new int[]{R.mipmap.tab_home, R.mipmap.tab_circle, R.mipmap.ic_launcher_round, R.mipmap.tab_shop, R.mipmap.tab_mine}, new int[]{R.mipmap.tab_home_selected, R.mipmap.tab_circle_selected, R.mipmap.tab_publish_selected, R.mipmap.tab_shop_selected, R.mipmap.tab_mine_selected}, getResArrStr(R.array.tab_main_title), R.color.font_gray, R.color.colorPrimary)
                    .setBgColor(getResColor(R.color.app_bg)), this);
        }
        @Override
        protected void processLogic(Bundle savedInstanceState) {
            mNavigationBar.showView(mTabIndex);
        }
    }
```
##### 1.1.2 底部通用导航栏实现:不可滑动
```Java
    public class MainActivity extends BaseActivity { 
        @Override
        protected void initView(Bundle savedInstanceState) {
            setView(mNavigationBar.initView(new HomeFragment(), new CircleFragment(), new PulishFragment(), new ShopFragment(), new MineFragment()).initData(mTabIndex, new int[]{R.mipmap.tab_home, R.mipmap.tab_circle, R.mipmap.ic_launcher_round, R.mipmap.tab_shop, R.mipmap.tab_mine}, new int[]{R.mipmap.tab_home_selected, R.mipmap.tab_circle_selected, R.mipmap.tab_publish_selected, R.mipmap.tab_shop_selected, R.mipmap.tab_mine_selected}, getResArrStr(R.array.tab_main_title), R.color.font_gray, R.color.colorPrimary,this)
                    .setBgColor(getResColor(R.color.app_bg)), this);
        }
        @Override
        protected void processLogic(Bundle savedInstanceState) {
            changeTab(mTabIndex);
        }
    }
```
#### 1.2 通用加载刷新控制:只需要在设置view是添加一个bool等于true即可,如果不想有刷新就置为false
```Java
    setView(R.layout.home, this,true);
```
#### 1.3 通用对话框实现:在Activity与Fragment中调用
#### 1.3.1 直接传入字符串,有取消按钮
```Java
    loading("the dialog!");
```
#### 1.3.2 直接传入字符串,无取消按钮
```Java
    loading("the dialog!").setOnlySure();
```
#### 1.3.2 自己定义View实现:可以传res与view
```Java
    loading(R.layout.default_dialog, new DialogView.DialogViewListener() {
                        @Override
                        public void onView(View view) {
                            TextView dialogContent = view.findViewById(R.id.dialogContent);
                           CusButton dialogCancel = view.findViewById(R.id.dialogCancel);
                           CusButton dialogSure = view.findViewById(R.id.dialogSure);
                           //...
                        }
                    });
```
#### 1.4. 附加Toast实现
##### 1.4.1 字符串
```Java
    CusToast.fixTxt(getActivity(), "the toast!");
```
##### 1.4.1 自定义View:res与view
```Java
    CusToast.fixView(getActivity(), R.layout.toast);
```
#### 1.4 二维码实现
#### 1.5 通用手势密码实现
#### 1.6 通用滚轮实现
#### 1.7 高级按钮操作等
### 2.网络模块
### 3.消息模块
### 4.事件分发模块
### 5.安全控制与认证模块
### 6.数据适配模块
### 7.升级模块(普通更新,热更新)
#### 7.1 普通同更新~默认:只需要一段代码即可
    new UpdateView(this).init().setListener(this);
#### 7.2 普通更新～截取更新核查结果
```Java
    new UpdateView(this).init(new Callback() {
                @Override
                public void onSuccess(NetInfo info) throws IOException {
                    
                }
    
                @Override
                public void onFailure(NetInfo info) throws IOException {
    
                }
            });
```
#### 7.3 普通更新～截取更新文件下载请求过程
```Java
    new UpdateView(this).init().setProgressListener(new ProgressCallback(){
                @Override
                public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                    super.onProgressMain(percent, bytesWritten, contentLength, done);
                }
    
                @Override
                public void onResponseMain(String filePath, NetInfo info) {
                    super.onResponseMain(filePath, info);
                }
            });
```
#### 7.4 普通更新~同时截取更新核查与文件下载：自定义场景与界面展示
```Java
    new UpdateView(this).init(new Callback() {
                @Override
                public void onSuccess(NetInfo info) throws IOException {
    
                }
    
                @Override
                public void onFailure(NetInfo info) throws IOException {
    
                }
            }).setProgressListener(new ProgressCallback(){
                @Override
                public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
                    super.onProgressMain(percent, bytesWritten, contentLength, done);
                }
    
                @Override
                public void onResponseMain(String filePath, NetInfo info) {
                    super.onResponseMain(filePath, info);
                }
            });   
```
### 8.错误日志模块(java,native)
### 9.HTML5交互模块
### 10.分享模块

## 项目集成(非常优雅的集成,最大化灵活控制,只需要几个步骤)

1.在你的项目工程根目录所在的build.gradle中添加:maven { url "https://jitpack.io" }
```Gradle
        allprojects {
            repositories {
                google()
                jcenter()
                maven { url "https://jitpack.io" }//当前需要添加的
            }
        }
```        
2.在你项目工程app所在目录下build.gradle中添加:implementation 'com.github.jiangshide:framework:1.0.7'
```Gradle
        dependencies {
            implementation 'com.github.jiangshide:framework:1.0.7'//当前需要添加的
        }
```        
### 3.拷贝demo中gradle.properties文件覆盖自己项目中的gradle.properties,需要注意:
#### 3.1 如果打release包的话需要替换成自己的密钥文件与相关信息：
```Gradle
        KEY_FILE=demo
        KEY_ALIAS=jsd
        KEY_PASSWORD=jsddemo
        STORE_PASSWORD=jsddemo
``` 
#### 3.2 网络主机替换：需要替换成自己的IP或域名
```Gradle
    #the net config
    PRODUCTION=http://10.20.6.50:8091
    PREPRODUCTION=http://10.20.6.50:8091
    TEST=http://10.20.6.50:8091
    API=/api/zdb/
    UPDATE=update
```
#### 3.3 api协议配置,可替换成自己实际协议的名称
```Gradle
    #BaseData protocol
    CODE=code
    SUCCESS=success
    TIME=date
    MSG=msg
```
#### 3.4 可修改网络默认配置
##### the http params
```Gradle
    HTTP_CONNECT_TIME=30
    HTTP_READ_TIME=30
    HTTP_WRITE_TIME=30
    HTTP_MAX_CACHE_SIZE=10 * 1024 * 1024
##### the four type:1~FORCE_NETWORK;2~FORCE_CACHE;3~NETWORK_THEN_CACHE;4~CACHE_THEN_NETWORK
    HTTP_CACHE_TYPE=4
    HTTP_IS_GZIP=true
    HTTP_SHOW_LIFECYCLE_LOG=true
```    
# 服务宗旨:
### 一.面向中小企业及个人：
#### 1.提供专业的技术支持
#### 2.可做高度定制化需求

#项目案例:


# 友情合作:
   ![Image](https://raw.githubusercontent.com/jiangshide/framework/master/img/weixin.jpeg)
# 鼓励与支持:   
   ![Image](https://raw.githubusercontent.com/jiangshide/framework/master/img/play.png)
   
