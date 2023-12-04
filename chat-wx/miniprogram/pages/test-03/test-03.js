// pages/test-03/test-03.ts
var that = this;
Page({

  data:{
    
    
  },

  test:function(){
    var that=this;
    var length = 10; // 数组长度
    var min = 0; // 随机数最小值
    var max = 50; // 随机数最大值

    // 生成随机数数组
    var arr = [];
    for (var i = 0; i < length; i++) {
      var randomNumber = Math.floor(Math.random() * (max - min + 1)) + min;
      arr.push(randomNumber);
    }
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
        ctx.lineWidth = 5;
        // var time = new Date;
        // console.log(time.getSeconds());
        for (var i = 0; i < 10; i++) {
          
          ctx.beginPath();
          ctx.moveTo(5 + i * 14, 70);
          ctx.lineTo(5 + i * 14, 70 + arr[i] + 10);
          ctx.lineTo(5 + i * 14, 70 - arr[i] - 10);
          ctx.stroke();
        }
        // canvas.requestAnimationFrame(test);
    })
  },
  onPlay:function(){
    console.log("开始录音")
    wx.startRecord({
      success: function(res) {
        const tempFilePath = res.tempFilePath; // 录音文件的临时路径
        // 创建内部音频上下文对象
        const audioCtx = wx.createInnerAudioContext();
        // 设置音频源为录音文件
        audioCtx.src = tempFilePath;
        // 创建Analyser节点
        const analyser = audioCtx.createAnalyser();
        // 将Analyser节点连接到音频上下文
        audioCtx.connect(analyser);
        // 设置Analyser节点的参数
        analyser.fftSize = 2048; // 设置FFT大小
        const bufferLength = analyser.frequencyBinCount;
        const dataArray = new Uint8Array(bufferLength);
        // 实时获取频域数据
        function getFrequencyData() {
          analyser.getByteFrequencyData(dataArray);
          // 在这里处理频域数据，例如获取最大频率或进行可视化
          // dataArray中的数据表示不同频率的能量值
          // 可以根据需要进行处理和分析
          // 通过requestAnimationFrame实现实时更新
          requestAnimationFrame(getFrequencyData);
        }
        // 开始获取频域数据
        getFrequencyData();
        // 播放录音文件
        audioCtx.play();
      },
      fail: function(res) {
        console.error('录音失败：', res);
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
    // var that = this;
    // var i = 0;
   
    // setInterval(function() {
    //   // that.test();
      
    // }, 16.7);
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