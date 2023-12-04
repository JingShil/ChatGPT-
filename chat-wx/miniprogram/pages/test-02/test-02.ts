

Page({

  /**
   * 页面的初始数据
   */
  data: {
    time: 0,
    timeWidth: 80,
    audioSrc: ''
  },
  playAudio:function(){
    var that=this;
    wx.request({
      url: 'http://localhost:8083/azure/tts',
      method: "POST",
      header: {
        'content-type': 'application/json'
      },
      data: {
        "text": "200字长沙游记"
      },
      success: function(res) {
        that.setData({
          audioSrc:"data:audio/x-wav;base64," + res.data.data
        })
        const audio = wx.createAudioContext('myAudio')
        audio.play();
      },
      fail: function(err) {
        console.error('发送失败', err);
      }
    });
  },
  
  onPlay: function () {
    var that=this;
    wx.request({
      url: 'http://localhost:8083/azure/tts',
      method: "POST",
      header: {
        'content-type': 'application/json'
      },
      data: {
        "text": "你好"
      },
      success: function(res) {
        let innerAudioContext = wx.createInnerAudioContext();
        innerAudioContext.src = 'audio/file.wav';
        innerAudioContext.onPlay(() => {
          console.log(innerAudioContext.duration);
        })
        innerAudioContext.onTimeUpdate(() => {
          console.log(innerAudioContext.duration);
          that.setData({
            time:Math.round(innerAudioContext.duration),
            timeWidth:Math.round(innerAudioContext.duration)*10 + 80
          })
        })
        innerAudioContext.play();
       
      },
      fail: function(err) {
        console.error('发送失败', err);
      }
    });
    
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