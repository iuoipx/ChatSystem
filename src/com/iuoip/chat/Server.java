package com.iuoip.chat;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天系统服务端线程
 */
public class Server implements Runnable {
    private Socket socket;
    private static ConcurrentHashMap<Socket, String> hashMap = new ConcurrentHashMap<>();

    public Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            //首页功能
            homeFunction(socket);

            //系统主页功能
            systemFunction(socket);
        } catch (SocketException e) {
            if (hashMap.get(socket) != null) {
                System.out.println(hashMap.remove(socket) + "异常下线...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 服务器端首页功能
     *
     * @param socket socket流对象
     */
    private void homeFunction(Socket socket) throws IOException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

        //菜单选项
        String serverMenuNo = null;
        User user;
        DBOperator dbOperator = new DBOperator();

        endSign:
        while (true) {
            try {
                //获取客户端发送的菜单选项信息
                serverMenuNo = (String) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (serverMenuNo != null) {
                switch (serverMenuNo) {
                    case "1":
                        //TODO 注册功能
                        try {
                            //获取客户端发送过来的数据
                            user = (User) in.readObject();

                            //将用户数据交给数据库处理
                            int count = dbOperator.addUser(user);
                            String registerMessage = count == 1 ? ">>>:注册成功" : "用户名已存在，注册失败";

                            //将用户注册结果返回给客户端
                            out.writeObject(registerMessage);

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "2":
                        //TODO 登录功能
                        try {
                            user = (User) in.readObject();
                            String username = user.getUsername();

                            //判断该用户是否在线
                            if (hashMap.contains(username)) {
                                out.writeObject("用户已在线...");
                                break;
                            }

                            String LoginMessage = "";
                            if (dbOperator.findUser(user) == 0) {
                                LoginMessage = ">>> 密码错误，请重新输入";
                            } else if (dbOperator.findUser(user) == 1) {
                                LoginMessage = ">>> 用户" + user.getUsername() + "登录成功";
                                out.writeObject(LoginMessage);
                                //用户登录成功将用户名和socket放入map里
                                hashMap.put(socket, user.getUsername());
                                System.out.println("用户" + user.getUsername() + "已上线...");
                                break endSign;
                            } else {
                                LoginMessage = ">>> 该用户名不存在";
                            }
                            //将用户注册结果返回给客户端
                            out.writeObject(LoginMessage);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "3":
                        //TODO 找回密码功能
                        try {
                            user = (User) in.readObject();

                            //将用户数据交给数据库处理
                            String password = dbOperator.findPassword(user);
                            password = password == null || password.equals("")
                                    ? "用户名或邮箱不匹配"
                                    : "您的密码是: " + password;

                            out.writeObject(password);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "4":
                        //TODO 退出功能
                        Thread thread = Thread.currentThread();
                        thread.interrupt();
                        break endSign;
                }
            }
        }
    }

    /**
     * 服务器端系统主页功能
     *
     * @param socket socket流对象
     */
    private void systemFunction(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //菜单选项
        String serverMenuNo = null;
        //数据库操作对象
        DBOperator dbOperator = new DBOperator();

        endSign:
        while (true) {
            //获取客户端发送的菜单选项信息
            serverMenuNo = in.readLine();
            if (serverMenuNo != null) {

                String currentUsername = null;
                Set<Map.Entry<Socket, String>> entries = null;
                entries = hashMap.entrySet();
                for (Map.Entry<Socket, String> entry : entries) {
                    //获取当前socket连接对应的用户名
                    if (entry.getKey().equals(socket)) {
                        currentUsername = entry.getValue();
                    }
                }

                //聊天内容
                String chatMessage = null;

                switch (serverMenuNo) {
                    case "1":
                        //获取所有在线用户的用户名集合
                        Collection<String> values = hashMap.values();
                        List<String> onlineUsers = new ArrayList(values);
                        String str = "";

                        Iterator<String> iterator = onlineUsers.iterator();
                        while (iterator.hasNext()) {
                            //将所有在线的用户名拼接成一个字符串
                            str += iterator.next() + " ";
                        }
//                        String str = hashMap.values().toString();
                        //将在线用户名的字符串发给客户端
                        out.println(str);
                        break;
                    case "2":
                        //获取私聊对象
                        String username = in.readLine();
                        //获取私聊内容
                        chatMessage = in.readLine();
                        entries = hashMap.entrySet();
                        Socket socket1 = null;
                        for (Map.Entry<Socket, String> entry : entries) {
                            if (entry.getValue().equals(username)) {
                                //获取私聊对象的socket
                                socket1 = entry.getKey();
                                break;
                            }
                        }
                        if (socket1 == null) {
                            out.println("该用户不在线...");
                            break;
                        }
                        if (currentUsername != null) {
                            //向私聊对象发送消息
                            new PrintStream(socket1.getOutputStream(), true)
                                    .println(currentUsername + "对你私聊说：" + chatMessage);
                        }
                        break;
                    case "3":
                        //获取客户端发送的聊天内容
                        chatMessage = in.readLine();
                        entries = hashMap.entrySet();
                        Socket socket2 = null;
                        for (Map.Entry<Socket, String> entry : entries) {
                            socket2 = entry.getKey();

                            //如果是发送该群聊消息的用户，则跳过
                            if (socket2.equals(socket)) {
                                continue;
                            }

                            //向所有客户端发送聊天消息
                            if (currentUsername != null) {
                                new PrintStream(socket2.getOutputStream(), true)
                                        .println(currentUsername + "对所有人说：" + chatMessage);
                            }
                        }
                        break;
                    case "4":
                        //用户退出操作将该用户socket和username从map集合中移除
                        System.out.println(hashMap.remove(socket) + "已下线...");
                        break endSign;
                    case "5":
                        String s = in.readLine();
                        if (s.equals("yes")) {
                            //将用户信息交给数据库处理
                            int i = dbOperator.deleteUser(currentUsername);
                            if (i == 1) {
                                System.out.println(hashMap.remove(socket) + "已下线...");
                            }
                            String deleteUserMessage = i == 1
                                    ? "用户信息删除成功..."
                                    : "用户信息删除失败";
                            //把注销结果返回给客户端
                            out.println(deleteUserMessage);
                            break endSign;
                        } else {
                            break;
                        }
                    case "6":
                        //获取客户端发送的map集合
                        String users = in.readLine();
                        String checkMessage = "";

                        //处理客户端发送的map对象字符串
                        users = users.substring(1, users.length() - 1);
                        String[] strs = users.split(",");
                        Map<String, String> map = new HashMap();
                        for (String string : strs) {
                            String key = string.trim().split("=")[0];
                            String value = string.trim().split("=")[1];
                            map.put(key, value);
                        }
                        //先检测该用户信息是否正确
                        if (dbOperator.findUser(new User(currentUsername, map.get("oldPassword"))) == 1) {
                            //然后执行修改用户密码操作
                            int count = dbOperator.changePassword(new User(currentUsername, map.get("newPassword")));
                            if (count == 1)
                                checkMessage = "修改用户密码成功";
                        } else {
                            checkMessage = "修改用户密码失败";
                        }
                        //向客户端反馈修改密码结果
                        out.println(checkMessage);
                }
            }
        }
    }

}
