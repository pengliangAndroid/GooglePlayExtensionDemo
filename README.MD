# GooglePlayExtensionDemo
由于Google Play并不允许开发者上传超过100M的安装包，超过100M的应用需要对项目资源进行分包处理，将项目中的大资源文件从APK中单独抽取弄成压缩包。Google官方提供了Apk Expansion Files，支持开发者构建超过100M的安装包；开发者也可以将资源文件放在自己服务器上，在应用安装后进行初始化下载或延迟下载相关资源。

***项目中提供了Google Play下载的Obb文件检测，解压文件，多线程断点下载文件相关代码。***

**参考资料**
https://juejin.im/post/5ae3431bf265da0b8d41aa75

**官方文档**
https://developer.android.com/google/play/expansion-files
