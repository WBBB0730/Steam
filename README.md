# Steam 仿写

本项目为仿写 Steam 网页版，采用前后端分离模式，前端使用Vue3框架，后端使用SpringBoot框架

已完成功能：全局顶部导航栏、商城首页（部分）、注册/登录/注销、愿望单；

待完成功能：搜索、游戏详情、游戏评测、聊天、个人资料

## 技术细节

### 前端：

已完成：

* 使用 Vue Router 进行路由管理
* 使用 Vuex 进行状态管理（如登录状态、当前路由）
* 封装了 Axios 的 request 方法，配置了请求拦截器和响应拦截器
* 实现了愿望单的拖拽排序，以及优雅的拖拽动画
* 封装了开箱即用的 showModal 方法、轮播图组件
* 自定义 v-loading 指令用于播放加载动画，v-trunc 指令用于自动截断多行文本
* 自定义 v-lazy 指令，实现图片懒加载；封装 debounce 防抖函数，用于实时搜索

待完成：

* 使用 WebSocket 实现在线聊天
* 封装文本编辑器组件，用于撰写评测

### 后端

已完成：

* 使用 SpringBoot 处理 HTTP 请求
* 使用 SpringDataJPA + MySQL 进行数据库的建表以及增、删、改、查
* 使用 JJWT 库进行 Token 的生成和解析
* 使用全局异常处理器捕获并处理请求异常和服务器异常
* 使用 @Configuration 配置全局允许跨域
* 封装了通用响应体、常用状态码等

待完成：

* 使用全局拦截器进行身份校验
* 使用 SpringBootStarterWebSocket 实现 WebSocket 的接收、处理和发送
* 搭建图床，实现图片上传
