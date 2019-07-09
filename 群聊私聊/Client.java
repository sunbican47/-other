package cn.panqingshan.demo.dist.dubbo.gateway.群聊私聊.群聊私聊2;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Client{
    public static void main (String arg[]) throws IOException {
        String name = JOptionPane.showInputDialog("请输入要创建的名称：");
//      客户端进行连接服务端IP、Port
        Socket client = new Socket("192.168.2.131",8080);
        //创建数据报套接字
        DatagramSocket data =  new DatagramSocket(8000);
        System.out.println("你已连接服务端"+client.getPort());
        new Thread(){
            public void run(){
                try{
                    InputStream is = client.getInputStream();
                    int length = -1;
                    byte[] bytes = new byte[1024*64];
                    while((length = is.read(bytes)) != -1){
                        System.out.println(new String(bytes,0,length));
                    }
                }catch(Exception e){
                    System.out.println("异常："+e);
                }
            }
        }.start();
//      UDP 用户数据报协议
        new Thread(){
            public void run(){
                try{
                    while(true) {
                        byte[] bytes2 = new byte[1024 * 64];
                        DatagramPacket packet = new DatagramPacket(bytes2, bytes2.length);
                        data.receive(packet);
                        byte[] date2 = packet.getData();
                        int length2 = packet.getLength();
                        String ip = packet.getAddress().getHostAddress();
                        int port = packet.getPort();
                        InetAddress address = packet.getAddress();
                        System.out.printf("FROM:%s:%s 昵称：%s\n", ip, port, new String(date2, 0, length2));
                    }
                }catch(Exception e){
                    System.out.println("异常："+e);
                }
            }
        }.start();
        while(true){
            String choice = JOptionPane.showInputDialog("1.群聊      2.私聊 ");
            switch(choice){
                case "1":
                        try{
                            String string = JOptionPane.showInputDialog("请输入群聊内容：");
                            OutputStream outputStream = client.getOutputStream();
                            /*//根据本地IP发送
                            //InetAddress addr = InetAddress.getLocalHost();
                            //System.out.println(addr.getHostAddress());*/
                            //根据名称的发送
                            string=name+"【群发消息】："+string;
                            outputStream.write(string.getBytes());
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        break;
                case "2":
                    //指定接受人的IP
                        String receiveHost = JOptionPane.showInputDialog("请输入私聊对象IP：");
                        InetAddress byHost = InetAddress.getByName(receiveHost);
                    //指定接受人的端口
                        String receivePort = JOptionPane.showInputDialog("请输入私聊对象端口：");
                        int byPort=80;
                        try{
                            byPort=Integer.parseInt(receivePort);
                    //输入发送内容
                            String string2 = JOptionPane.showInputDialog("请输入私聊内容：");
                            byte[] bytes = (name + "【私发消息】：" + string2).getBytes();
                            /*//接受人的IP （本地）
                            //InetAddress loopbackAddress = InetAddress.getLoopbackAddress();*/
                            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, byHost, byPort);
                    //打包发送
                            data.send(packet);
                        }catch (Exception e){
                            System.out.println("异常："+e);
                        }
            }
        }
    }
}