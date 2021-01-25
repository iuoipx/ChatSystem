package com.iuoip.chat;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

/**
 * 聊天室客户端
 */
public class Client {
    public static void main(String[] args) {
        try {
            //创建客户端socket服务指定ip和端口
            Socket socket = new Socket("127.0.0.1", 8888);

            //首页功能
            homeFunction(socket);

            //系统主页功能
            systemFunction(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 首页功能
     *
     * @param socket socket对象
     */
    private static void homeFunction(Socket socket) throws IOException {
        //获取键盘输入
        Scanner scanner = new Scanner(System.in);

        //输入流与输出流
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        //菜单选项数字
        String clientMenuNo;

        endSign:
        while (true) {
            //首页页面
            Page.homePage();
            clientMenuNo = scanner.nextLine(); //获取键盘输入
            //将键盘输入的菜单选项信息发送给服务器端
            out.writeObject(clientMenuNo);
            //判断是否登录成功
            boolean flag = false;
            User user;
            switch (clientMenuNo) {
                case "1":
                    //TODO 首页注册功能
                    //注册页面
                    user = Page.registerUser();

                    //将用户信息发送给服务器端
                    out.writeObject(user);

                    try {
                        //接受服务器端反馈的结果信息
                        String registerMessage = (String) in.readObject();

                        System.out.println(registerMessage);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    //TODO 首页登录功能
                    //登录页面
                    user = Page.login();

                    //将用户信息发送给服务器
                    out.writeObject(user);

                    try {
                        //接受服务器端反馈的结果信息
                        String loginMessage = (String) in.readObject();

                        System.out.println(loginMessage);
                        if (loginMessage.contains("成功")) {
                            break endSign;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    //TODO 首页找回密码功能
                    user = Page.findPassword();
                    //将用户信息发送给服务器端
                    out.writeObject(user);

                    try {
                        //接受服务器端反馈的结果信息
                        String password = (String) in.readObject();

                        System.out.println(password);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    //TODO 首页退出功能
                    socket.close();
                    System.exit(0);
            }
        }
    }

    /**
     * 系统主页功能
     *
     * @param socket socket对象
     */
    private static void systemFunction(Socket socket) throws IOException {
        //获取键盘输入
        Scanner scanner = new Scanner(System.in);

        //输入流与输出流
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //接收消息线程
        new Thread(() -> {
            while (true) {
                try {
                    String message = in.readLine();
                    System.out.println(message);
                } catch (IOException e) {
                    System.out.println("服务器故障...");
                    System.exit(0);
                }
            }
        }).start();

        //发送消息线程
        new Thread(() -> {
            String clientMenuNo;
            endSign:
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //系统页面
                Page.systemPage();
                clientMenuNo = scanner.nextLine(); //获取键盘输入

                //将键盘输入的菜单选项信息发送给服务器端
                out.println(clientMenuNo);

                String message = null;
                switch (clientMenuNo) {
                    case "1":
                        break;
                    case "2":
                        //输入私聊对象的用户名并且发送给服务器端
                        System.out.print("输入私聊对象的用户名：");
                        String username = scanner.nextLine();
                        out.println(username);

                        //输入私聊内容并且发送给服务器端
                        System.out.print("对" + username + "私聊说：");
                        message = scanner.nextLine();
                        out.println(message);
                        break;
                    case "3":
                        System.out.print("对所有人说：");
                        message = scanner.nextLine();
                        out.println(message);
                        break;
                    case "4":
                        System.exit(0);
                    case "5":
                        //提示是否确定注销
                        System.out.println("确定注销帐号吗？(yes/no)");
                        String s = scanner.nextLine();

                        //将确定信息发送给服务器端
                        out.println(s);
                        if (s.equals("yes")) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.exit(0);
                        } else {
                            break;
                        }
                    case "6":
                        //获取用户新密码和旧密码的map集合并发送给服务器端
                        Map hashMap = Page.modifyPassword();
                        out.println(hashMap);
                        break;
                }
            }
        }).start();
    }
}

