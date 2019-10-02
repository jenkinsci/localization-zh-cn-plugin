# 简体中文插件
[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/localization-zh-cn.svg)](https://plugins.jenkins.io/localization-zh-cn)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/localization-zh-cn.svg?color=blue)](https://plugins.jenkins.io/localization-zh-cn)
[![Gitter](https://badges.gitter.im/jenkinsci/localization-zh-cn-plugin.svg)](https://gitter.im/jenkinsci/localization-zh-cn-plugin)


本插件包括 Jenkins 核心以及插件的中文本地化。查看 [JEP-216](https://github.com/jenkinsci/jep/blob/master/jep/216/README.adoc) 了解设计细节。

# 入门教程

这里有一些关于 [如何为 Jenkins 插件贡献本地化](https://jenkins.io/doc/developer/internationalization/) 的教程。

[jcli](https://github.com/jenkins-zh/jenkins-cli) 可以帮助你快速地把插件上传到你的 Jenkins 中。命令为：`jcli plugin upload`。

# 贡献

如果，你对本地化感兴趣，请首先查看 [中文本地化 SIG](https://jenkins.io/sigs/chinese-localization/)。

所有的中文字符都需要转为为 ASCII.这样难以阅读，你可以使用这个[在线工具](https://native2ascii.net/)进行快速地转化。

注意，每位贡献者都应该遵循[翻译规范](specification.md)。

# Actions

我们使用 [git-backup-actions](https://github.com/jenkins-zh/git-backup-actions/) 来备份当前仓库到 
[gitee](https://gitee.com/jenkins-zh/localization-zh-cn-plugin).
