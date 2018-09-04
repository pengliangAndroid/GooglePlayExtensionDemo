# GooglePlayExtensionDemo
***项目中提供了Google Play下载的Obb文件检测，解压文件，多线程断点下载文件相关代码。***

由于Google Play并不允许开发者上传超过100M的安装包，超过100M的应用需要对项目资源进行分包处理，将项目中的大资源文件从APK中单独抽取弄成压缩包。以下是2种处理方式，第一种方式是Google官方提供的Apk Expansion Files，将资源文件处理成obb扩展文件上传，在Google Play下载应用时会同时下载APK和obb拓展文件；第二种方式是开发者可以将资源文件放在自己服务器上，在应用安装后进行初始化下载或延迟下载相关资源。

**推荐使用第一种方式，需要注意以下情况：**

1、从Google Play下载和安装App有一定概率会下载到不包含obb文件的apk，或者obb文件被人为删除了，需要自己手动重新下载

2、更新应用版本后，拓展文件升级后，应用还是要自己处理升级拓展文件的覆盖解压。

---


**Demo应用下载地址**

https://play.google.com/store/apps/details?id=com.hdgame.extensiondmeo


**参考资料**

https://juejin.im/post/5ae3431bf265da0b8d41aa75

**官方文档**

https://developer.android.com/google/play/expansion-files
