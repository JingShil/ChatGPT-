// pages/test/test.ts
// const recorderManager = wx.getRecorderManager();
var timer; // 计时器变量
var startTime; // 初始时间变量
Page({

  /**
   * 页面的初始数据
   */
  data: {
    startStyle: "",
    voiceStyle: "",
    cancelStyle: "",
    confirmStyle:"",
    isStart: false,
    isPause: false,
    recorderManager : wx.getRecorderManager(),
    frequency: 0,
    line:"",
    time:0
  },
  startTimer: function() {
    var that=this
    function updateTimer() {
      if(!that.data.isStart){
        startTime=new Date().getTime();
      }
      else if(!that.data.isPause){
        var currentTime = new Date().getTime();
        var elapsedTime = currentTime - startTime;
      
        // 将毫秒转换为分钟和秒
        var seconds = Math.floor((elapsedTime % 60000) / 1000);
      
        // 更新页面上的计时器显示
        this.setData({
          time: seconds
        });
        console.log(seconds)
      }else {
        startTime+=1000;
      }
    }
    timer = setInterval(updateTimer.bind(this), 1000);
  },
  stopTimer: function() {
    clearInterval(timer);
  },
  
  startVoice:function(){
    var that=this;
    this.setData({
      startStyle:"background-color: white;height:120px;",
      voiceStyle:" background-color: cadetblue;",
      cancelStyle:"background-color: rgb(161, 167, 161);"
    })
    if(this.data.isStart==false){
      console.log('开始录音');
      that.setData({
        isStart:true
      })
      this.data.recorderManager.onStart(() => {
        console.log('开始录音');
      });
      startTime=new Date().getTime();
      that.startTimer()
      this.data.recorderManager.start({
        duration: 60000, // 录音时长，单位为毫秒，默认为60000（1分钟）
        sampleRate: 44100, // 采样率，有效值为 8000/16000/44100，默认为44100
        numberOfChannels: 1, // 录音通道数，有效值为 1/2，默认为1
        encodeBitRate: 96000, // 编码码率，有效值为8000/16000/24000/48000/64000/96000/128000，默认为96000
        format: 'mp3', // 音频格式，有效值为aac/mp3，默认为mp3
        frameSize: 10, // 每帧大小，单位为KB，默认为50
      });
    
      
     
      
    }else{
      if(that.data.isPause){
        that.setData({
          isPause:false
        })
        console.log('继续录音');
        this.data.recorderManager.resume();
      }
    }
    
  },
  stopVoice:function(){
    this.setData({
      startStyle:"background-color: rgb(97, 89, 89);",
      voiceStyle:" background-color: rgb(252, 2, 2);",
      cancelStyle:"background-color: white;"
    })
    if(this.data.isStart){
      if(this.data.isPause==false){
        this.setData({
          isPause:true
        })
        console.log('停止录音');
        this.data.recorderManager.pause();
      }
    }
  },
  endVoice:function(){
    var that=this
    this.setData({
      startStyle:"background-color: rgb(97, 89, 89);",
      voiceStyle:" background-color: rgb(252, 2, 2);",
      cancelStyle: "background-color: rgb(161, 167, 161);"  
    })
    if(this.data.isStart){
      console.log("结束录音")
      if (this.data.isPause) {
        that.data.recorderManager.onStop(() => {
          console.log('取消录音');
          
          wx.showToast({
            title: '录音取消',
            icon: 'error',
            duration: 2000    //提示的延迟时间
          });
        });
      }else {
        that.stopTimer();
        that.data.recorderManager.onStop((res) => {
          console.log('停止录音', res.tempFilePath);
          wx.uploadFile({
            url: 'http://localhost:8083/voice/text/upload', // 上传服务器的URL
            filePath: res.tempFilePath, // 录音文件的临时路径
            name: 'audio', // 服务器接收文件的字段名
            success: function(res) {
              console.log('上传成功', res.data);
              // 在这里可以处理上传成功后的逻辑
            },
            fail: function(res) {
              console.log('上传失败', res.errMsg);
              // 在这里可以处理上传失败后的逻辑
            }
          });
        });
      }
      that.setData({
        isStart:false,
        isPause: false
      })
      that.data.recorderManager.stop();
      
    }
  },
  moveVoice:function(e){
    var that = this;
    // 获取手指触摸的位置
    var touchY = e.touches[0].clientY;
    // 获取元素的位置和尺寸信息
    var rect= wx.createSelectorQuery().select('.start').boundingClientRect();
    rect.exec(function(res) {
      var height = res[0].height;
      // 判断手指是否超过边框
      if (touchY < wx.getSystemInfoSync().windowHeight-height) {
        that.stopVoice();
      }else {
        that.startVoice();
      }
    });
  },
  
  test:function(arr){
    // console.log(arr);
    wx.createSelectorQuery()
    .select('#myCanvas') // 在 WXML 中填入的 id
    .fields({ node: true, size: true })
    .exec((res) => {
        // Canvas 对象
        const canvas = res[0].node
        // 渲染上下文
        const ctx = canvas.getContext('2d')

        // Canvas 画布的实际绘制宽高
        const width = res[0].width
        const height = res[0].height

        // 初始化画布大小
        const dpr = wx.getWindowInfo().pixelRatio
        canvas.width = width * dpr
        canvas.height = height * dpr
        ctx.scale(dpr, dpr)
        // 清空画布
        ctx.clearRect(0, 0, width, height)
        ctx.lineCap = "round";
        ctx.lineWidth = 2;
        // var time = new Date;
        // console.log(time.getSeconds());
        for (var i = 0; i < 100; i++) {
          
          ctx.beginPath();
          ctx.moveTo(5 + i*5, 100);
          ctx.lineTo(5 + i*5, 100 + arr[i]/10 + 10);
          ctx.lineTo(5 + i*5, 100 - arr[i]/10 - 10);
          ctx.stroke();
        }
        // canvas.requestAnimationFrame(test);
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})