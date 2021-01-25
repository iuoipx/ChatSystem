package com.iuoip.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 系统页面
 */
public class Page {
    private Page() {
    }

    /**
     * 首页页面
     */
    public static void homePage() {
        System.out.println("请选择菜单功能(数字表示)：");
        System.out.println("1.注册\t2.登录\t3.找回密码\t4.退出");
    }

    /**
     * 系统页面
     */
    public static void systemPage() {
        System.out.println("请选择菜单功能(数字表示)：");
        System.out.println("1.查看在线人员名单\t2.私聊(先输入菜单编号2，再输入私聊对象)\t3.群聊\t4.退出\t5.账号注销\t6.修改密码");
    }

    /**
     * 注册页面
     *
     * @return
     */
    public static User registerUser() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        String username;
        int count = 0;
        do {
            count++;
            if (count > 1) {
                System.out.print("请重新输入用户名：");
                username = scanner.nextLine();
            } else {
                System.out.print("请输入用户名：");
                username = scanner.nextLine();
            }
        } while (!Pattern.matches("^[a-zA-Z0-9]+$", username));

        String password;
        int count1 = 0;
        do {
            count1++;
            if (count1 > 1) {
                System.out.print("请重新输入用户密码：");
                password = scanner.nextLine();
            } else {
                System.out.print("请输入用户密码：");
                password = scanner.nextLine();
            }
        } while (!Pattern.matches("^[a-zA-Z0-9]+$", password));

        String email;
        int count2 = 0;
        do {
            count2++;
            if (count2 > 1) {
                System.out.print("请重新输入邮箱：");
                email = scanner.nextLine();
            } else {
                System.out.print("请输入邮箱：");
                email = scanner.nextLine();
            }
        } while (!Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", email));

        //封装用户信息
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }

    /**
     * 登录页面
     *
     * @return
     */
    public static User login() {
        Scanner scanner = new Scanner(System.in);

        User user = new User();
        String username;
        String password;

        int count = 0;
        do {
            count++;
            if (count > 1) {
                System.out.print("请重新输入用户名：");
                username = scanner.nextLine();
            } else {
                System.out.print("请输入用户名：");
                username = scanner.nextLine();
            }
        } while (!Pattern.matches("^[a-zA-Z0-9]+$", username));

        int count1 = 0;
        do {
            count1++;
            if (count1 > 1) {
                System.out.print("请重新输入密码：");
                password = scanner.nextLine();
            } else {
                System.out.print("请输入密码：");
                password = scanner.nextLine();
            }
        } while (!Pattern.matches("^[a-zA-Z0-9]+$", password));

        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    /**
     * 找回密码页面
     */
    public static User findPassword() {
        Scanner scanner = new Scanner(System.in);

        User user = new User();
        String username;
        String email;

        int count = 0;
        do {
            count++;
            if (count > 1) {
                System.out.print("请重新输入用户名：");
                username = scanner.nextLine();
            } else {
                System.out.print("请输入用户名：");
                username = scanner.nextLine();
            }
        } while (!Pattern.matches("^[a-zA-Z0-9]+$", username));

        int count1 = 0;
        do {
            count1++;
            if (count1 > 1) {
                System.out.print("请重新输入注册邮箱：");
                email = scanner.nextLine();
            } else {
                System.out.print("请输入注册邮箱：");
                email = scanner.nextLine();
            }
        } while (!Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", email));

        user.setUsername(username);
        user.setEmail(email);
        return user;
    }

    /**
     * 修改密码页面
     *
     * @return 新旧密码map集合
     */
    public static Map modifyPassword() {
        Scanner scanner = new Scanner(System.in);

        Map hashMap = new HashMap<String, String>();

        String password1;
        String password2;
        int count = 0;
        do {
            count++;
            if (count > 1) {
                System.out.print("请重新输入旧密码：");
                password1 = scanner.nextLine();

                System.out.print("请重新输入新密码：");
                password2 = scanner.nextLine();
            } else {
                System.out.print("请输入旧密码：");
                password1 = scanner.nextLine();

                System.out.print("请输入新密码：");
                password2 = scanner.nextLine();
            }
        } while (!Pattern.matches("^[a-zA-Z0-9]+$", password1) || !Pattern.matches("^[a-zA-Z0-9]+$", password2));

        //将新旧密码放入map集合
        hashMap.put("oldPassword", password1);
        hashMap.put("newPassword", password2);
        return hashMap;
    }
}
