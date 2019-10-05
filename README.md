# DayWordCount
项目旨在通过辅助服务监控微信文字输入，使用分词功能，统计单位时间段内说过词语的数量，可以统计出自己的口癖和一段时间内关心的内容。

这是一个几乎全栈的项目（没有网页前端），包括了app、业务逻辑层、数据服务层、算法服务模块（主要是对微信消息进行一些算法处理如分词）。整个系统是微服务架构，每个服务或模块都是一个单独的服务，通过docker提供调用。服务发现用的是etcd，基于etcd包装了一个简单的服务发现模块，在各个服务中引用，方便进行服务注册和发现。

## 各服务/模块地址
APP           https://github.com/Diode222/DayWordCount

业务逻辑层      https://github.com/Diode222/Odin

数据服务层      https://github.com/Diode222/Frigg

算法服务模块    (待实现)

服务发现模块    https://github.com/Diode222/etcd_service_discovery
