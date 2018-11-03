# AntuBlog 博客系统

#### 项目介绍

+ `AntuBlog` 是一款使用基于[JFinal](https://gitee.com/jfinal/jfinal)开发的轻量级Java博客系统。
+ 主题1预览：[pinghsu](http://pinghsu.wenhaofan.com)
+ 主题2预览：[morning](http://morning.wenhaofan.com)

## 特性

+ 全站响应式
+ 全站pjax
+ 支持Markdown
+ 整合多套前台主题， [pinghsu](https://github.com/chakhsu/pinghsu),[morning](http://www.yangqq.com/zaoan/)
+ 对接百度推送接口
+ 支持metaweblog推送
+ 内置文件系统
+ 全文检索

## 技术选型
### 后端
+ 语言：JAVA
+ 核心框架：JFinal
+ 数据库：mysql5.6
+ 缓存层：ehcache
+ 全文检索：lucene+ik分词
### 前端
 #####      后台
+ 核心库：jquery
+ 框架：layui+部分bootstrap
+ 编辑器：mditor+summernote
+ 插件：clipboard，context-menu，dropzone，font-awesome ，
jquery-multi-select，jquery-pjax，jquery-toggles，limonte-sweetalert2，
，multi-select，nprogress，select2，tagsinput，art-template toggles 
 #####      前台
 + 核心库：jquery
 + 插件：instantclick，fastclick，headroom

## 界面预览

![antu1.png](http://pd6htjig8.bkt.clouddn.com/v2adminimg.png)
![antu_022.png](http://pd6htjig8.bkt.clouddn.com/v2adminimg2.png)
![antu_03.png](http://pd6htjig8.bkt.clouddn.com/v2adminimg3.png)
更多预览可部署项目查看

## 开源协议

[MIT](LICENSE)
#### 使用说明

1. 使用jetty运行 右键Application.java
2. 使用tomcat运行 项目打包为war包放入webapp目录下运行

#### 参与贡献

1. Fork 本项目
3. 提交代码
4. 新建 Pull Request
