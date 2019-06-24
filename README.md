# AntuBlog 博客系统

#### 项目介绍

+ `AntuBlog` 是一款使用基于[JFinal](https://gitee.com/jfinal/jfinal)开发的轻量级Java博客系统。
+ 主题1预览：[pinghsu](http://pinghsu.wenhaofan.com)

## 特性

+ 全文检索
+ 全站pjax
+ 全站响应式
+ 内置文件系统
+ 支持Markdown
+ 对接百度推送接口
+ 支持metaweblog推送
+ 内嵌undertow，无需tomcat，极速部署
+ 资源占用少，150M左右内存即可流畅运行
+ 支持自定义页面，内置简历页面

## 技术选型

### 后端
+ 语言：JAVA
+ 核心框架：JFinal
+ 数据库：MySQL5.7
+ 缓存层：Ehcache
+ 全文检索：Lucene+ik分词

### 前端

#####      后台
+ 框架：Layui
+ 编辑器：Ueditor+EditorMD

#####      前台
 + 核心库：JQuery
 + 插件：instantclick，fastclick，headroom

## 界面预览

![antu1.png](http://qiniu.wenhaofan.com/admibimg.png)
![antu_022.png](http://qiniu.wenhaofan.com/v2adminimg2.png)
![antu_03.png](http://qiniu.wenhaofan.com/v2adminimg3.png)

## 在线预览

[预览地址：http://blog.autu.live](http://blog.autu.live "预览地址：http://blog.autu.live") </br>
**为方便预览未设置权限，请勿随意更改数据，谢谢合作**

## 开源协议

[MIT](LICENSE)
#### 使用说明

1. 使用Undertow运行 右键Application.java
2. 使用tomcat运行 项目打包为war包放入webapp目录下运行

#### 参与贡献

1. Fork 本项目
3. 提交代码
4. 新建 Pull Request
