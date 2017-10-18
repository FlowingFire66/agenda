package com.meeting.commands;

import com.meeting.utils.Login;
/**
 author: lubeiling
 Date: 2017/10/17
 **/

public class LogoutCommand implements CommandIn {

    @Override
    public void simpleHelp() {
        System.out.printf("%-20s","logout");
        System.out.println("用户登出:logout");
    }

    public boolean getOptions(String[] args) {
        return true;
    }


    @Override
    public void excute() {
        if(Login.isLogin()){
            Login.setUser(null);
            System.out.println("--------------------------------------");
            System.out.println("成功退出！");
        }else {
            System.out.println("--------------------------------------");
            System.out.println("请登陆再使用此操作！");
        }
    }

    public boolean checkParameters() {
        return true;
    }

}
