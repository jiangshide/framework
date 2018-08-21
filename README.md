# framework
this for android with framework

打造最简单与最全的android基础框架:包括基础ui展现,基础数据加载,应用升级,热更新实现,错误日志收集,定位,分享等,拿来即用,不需要繁琐的配置

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.jiangshide:framework:1.0.1'
	}
