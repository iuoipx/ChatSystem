package com.iuoip.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 聊天系统服务端线程主类
 */
public class ChatServer {
    public static void main(String[] args) {
        try {
            //创建服务器端socket服务，并监听端口
            ServerSocket serverSocket = new ServerSocket(8888);

            while (true) {
                //获取socket连接对象
                Socket accept = serverSocket.accept();

                //创建线程并启动线程
                new Thread(new Server(accept)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
