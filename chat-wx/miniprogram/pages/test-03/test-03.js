
Page({
  data: {
    deviceName:"SIBYL Pro1"
  },

  onLoad() {
    var that=this;
    wx.openBluetoothAdapter({
      // mode:"central",
      success: function(res) {
        that.getConnectedBluetoothDevices();
        console.log("蓝牙适配器初始化成功");
      },
      fail: function(res) {
        console.log("蓝牙适配器初始化失败");
      }
    });
  },

  getConnectedBluetoothDevices:function(){
    var that=this
    wx.getConnectedBluetoothDevices({
      success: function(res) {
        console.log(res);
        var devices = res.devices;
        // 遍历设备列表，找到目标设备
        for (var i = 0; i < devices.length; i++) {
          var device = devices[i];
          console.log(device)
          if (device.name === that.data.deviceName) {
            // 连接到目标设备
            wx.createBLEConnection({
              deviceId: device.deviceId,
              success: function(res) {
                console.log("成功连接到目标设备");
              },
              fail: function(res) {
                console.log("连接目标设备失败");
              }
            });
            break;
          }
        }
      },
      fail: function(res) {
        console.log("获取已连接蓝牙设备列表失败");
      }
    });
  },
  writeBLECharacteristicValue:function(){
    wx.writeBLECharacteristicValue({
      deviceId: "目标设备的deviceId",
      serviceId: "目标设备的serviceId",
      characteristicId: "目标设备的characteristicId",
      value: ArrayBuffer.from("要发送的消息"),
      success: function(res) {
        console.log("消息发送成功");
      },
      fail: function(res) {
        console.log("消息发送失败");
      }
    });
  }
 


  
});
