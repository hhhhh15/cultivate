# 🎯 Cultivate Study App
是一款学习陪伴与成长式应用，通过任务拆分、每日进度激励、虚拟角色互动，帮助用户在长期学习中保持动力。
项目核心理念：虚拟角色作为“伙伴 + 领导者”，陪伴用户持续学习、持续变强。

## 📚 功能简介

### ⭐ 1. 学习任务系统  
- 前端提交任务到 Spring Boot 后端  
- 后端自动拆分为可执行的「子任务」  
- 用户每日完成子任务积累经验值

### ⭐ 2. 虚拟角色互动
- 根据每日进度显示不同鼓励语  
- 经验值达到阈值可以获得道具  
- 可与虚拟角色互动（如喂食触发表情）  
- 未来扩展更多动作、动画、语音交互

### ⭐ 3. 语音对话模块（开发中）
基于 MQTT + ESP32 + ASRPRO 的语音指令链路：客户端 → MQTT → ESP32 → ASRPRO → 返回识别结果


### ⭐ 4. Live2D 模型渲染
- 使用 NDK + JNI + OpenGL ES 渲染虚拟角色  
- 支持表情、参数控制、动作切换  
- 后续将增加转头、语音联动

---

## 🛠 使用技术栈

### **Android**
- Kotlin / MVVM  
- ViewModel、LiveData、viewPaper2、DataBinding、Room、Navigation  
- Retrofit + OkHttp  
- OpenGL ES、JNI、NDK  
- Live2D Cubism SDK for Native  

### **后端**
- Spring Boot  
- MySQL  
- Flask（用于 AI 扩展）

### **硬件与通信**
- ESP32  
- ASRPRO 语音模块  
- MQTT 通信协议

---

## ❤ 具体页面展示
### **任务页面展示**
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/94f62918-95f7-4aab-a15a-73fa7a6ba946" />

### **虚拟人物展示**
![080cb6d075b608e18988ffff7cca23c](https://github.com/user-attachments/assets/4d49358e-9548-42ab-8b30-cb6b557f845e)


## 🚀 安装与运行


