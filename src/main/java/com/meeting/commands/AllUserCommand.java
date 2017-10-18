package com.meeting.commands;

import com.meeting.mapper.mapperimpl.UserMapperImpl;
import com.meeting.pojo.User;
import com.meeting.utils.Login;

import java.util.List;
/**
 author: lubeiling
 Date: 2017/10/17
 **/
public class AllUserCommand implements CommandIn {
    UserMapperImpl userMapperImpl =new UserMapperImpl();

    @Override
    public void simpleHelp() {
        System.out.printf("%-20s","alluser");
        System.out.println("查看所有用户的信息:alluser");
    }

    @Override
    public boolean getOptions(String[] args) {
        return true;
    }


    @Override
    public void excute() {
        if(Login.isLogin()){
            List<User> users= userMapperImpl.findAll();
            System.out.println("------用户列表---------");
            System.out.println("用户名       邮箱       电话号码 ");
            System.out.println();
            for (User user: users) {
                System.out.printf("%-12s",user.getUsername());
                System.out.printf("%-12s",user.getEmail());
                System.out.printf("%-12s",user.getPhone());
                System.out.println();
            }
        }
        else {
            System.out.println("--------------------------------------");
            System.out.println("请登陆再查询！");
        }
    }

    @Override
    public boolean checkParameters() {
        return true;

    }
}
