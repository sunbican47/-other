package cn.panqingshan.demo.dist.dubbo.gateway.群聊私聊.群聊私聊2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class Server{
     public static void main(String arg[]) throws IOException {
         ServerSocket server = new ServerSocket(8080);
         System.out.println("服务端开启成功,端口为："+server.getLocalPort());
         LinkedList<Socket> clients = new LinkedList<>();
         while(true){
             new Thread(){
//                 服务端进行接受客户端套接字连接，没有则一直等待
                 Socket client = server.accept();
                 public void run(){
                     try{
//                        记录客户端套接字人员
                         clients.add(client);
//                         打开流进行读取
                        InputStream is = client.getInputStream();
                        int length = -1;
                        byte[] bytes = new byte[1024*10000];
                        while((length = is.read(bytes))!=-1){
                            System.out.println(client.getInetAddress());
                            System.out.println(client.getLocalAddress());
                            System.out.println(client.getPort());
                            System.out.println(client.getLocalSocketAddress());
                            System.out.println(client.getLocalPort());
                            System.out.println("来自客户端"+client.getInetAddress()+"的消息："+new String(bytes,0,length));
                           for(Socket s:clients){
                                if(s==client) continue;
                                OutputStream os = s.getOutputStream();
                                os.write(bytes,0,length);
                            }
                        }    
                     }catch(Exception e){
                             System.out.println("异常:"+e);
                         }
                     }
             }.start();
         }
     }
 }