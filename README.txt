程序实现类似微信语音界面：
  MainActivity
      -ListView：实现语音记录的显示
      -自定义Button：实现“按住说话按钮”的功能
            - DialogManager：该类实现按住按钮时候弹出的提示，例如：时间过短，上滑取消等
            - AudioMananger:该类实现语音的录制，存储
            
      - MediaManager:该类实现点击ListView中的语音时候的播放
      - RecorderAdapter：该类实现listView中的数据的管理与显示效果
