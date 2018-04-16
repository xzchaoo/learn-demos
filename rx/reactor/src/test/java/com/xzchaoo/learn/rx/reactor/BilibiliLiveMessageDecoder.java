package com.xzchaoo.learn.rx.reactor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by Administrator on 2017/6/6.
 */
public class BilibiliLiveMessageDecoder extends ByteToMessageDecoder {
  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    //7 进入房间初始化
    //2 客户端心跳
    //3 服务端心跳
    //5 消息
    //{"data":{"uid":6477357,"uname":"酒は杯にして傾城","isadmin":0,"svip":1},"cmd":"WELCOME","roomid":23058}
    //{"data":{"top_list":[],"newTitle":0,"capsule":{"normal":{"change":0,"progress":{"max":10000,"now":3100},"coin":53},"colorful":{"change":0,"progress":{"max":5000,"now":0},"coin":0}},"eventScore":0,"uname":"姬少尘","newMedal":0,"giftName":"铜锣烧","remain":1,"num":1,"notice_msg":[],"rnd":"1496714594","beatId":0,"rcost":54693424,"giftType":3,"title":"","super":0,"uid":9230761,"giftId":71,"price":2000,"medal":1,"action":"赠送","timestamp":1496725087,"eventNum":1},"cmd":"SEND_GIFT"}
    //{"msg":"恭喜本次弹幕抽奖获得小米Max 2的观众，大家都记得来发弹幕哟！","styleType":1,"cmd":"SYS_MSG","rep":1,"url":"http://live.bilibili.com/545342"}
    //{"cmd":"DANMU_MSG","info":[[0,1,25,16777215,1496725131,"1496724394",0,"1b0b9148",0],"发错； 了",[32351379,"友情帐号",0,0,0,10000,1],[1,"米粉","小米公司",545342,6406234],[26,0,5805790,">50000"],[],0,0]}
    //5 {"msg":"【草莓味坏坏】:?在直播间:?【246】:?赠送 小电视一个，请前往抽奖","styleType":2,"real_roomid":26057,"tv_id":"21627","rnd":1496725843,"cmd":"SYS_MSG","rep":1,"url":"http://live.bilibili.com/246","roomid":246}

    BilibiliLiveMessage msg = new BilibiliLiveMessage();
    int length = in.readInt();
    msg.setH1(in.readShort());
    msg.setH2(in.readShort());
    msg.setType(in.readInt());
    msg.setH3(in.readInt());
    if (msg.getType() == 3) {
      msg.setOnlineUserCount(in.readInt());
    } else {
      int contentLength = length - 16;
      System.out.println(msg.getType() + " " + contentLength);
      if (contentLength > 0) {
        byte[] bytes = new byte[contentLength];
        in.readBytes(bytes);
        msg.setBytes(bytes);
        if (msg.getType() == 5) {
          String dataStr = new String(bytes, StandardCharsets.UTF_8);
          JSONObject data = JSON.parseObject(dataStr);
          msg.setData(data);
        }
      }
    }
    out.add(msg);
  }
}
