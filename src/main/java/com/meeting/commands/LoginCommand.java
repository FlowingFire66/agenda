package com.meeting.commands;

import com.meeting.mapper.mapperimpl.UserMapperImpl;
import com.meeting.pojo.User;
import com.meeting.utils.Login;
import org.apache.commons.cli.*;

/**
 * User: FlowingFire
 * Date: 2017/10/15 0015
 * Time: 18:13
 * Description:
 */
public class LoginCommand implements CommandIn {
    String username;
    String password;
    private UserMapperImpl userMapper=new UserMapperImpl();

    public void simpleHelp() {
        System.out.printf("%-20s","login");
        System.out.println("用户登陆:login -u ly1 -p ly1");
    }


    public boolean getOptions(String[] args) {
        Options options = new Options();
        Option opt = new Option("h", "help", false, "Print help");
        opt.setRequired(false);
        options.addOption(opt);
        opt = new Option("u", "username", true, "username");
        opt.setRequired(true);
        options.addOption(opt);


        opt = new Option("p", "password", true, "password");
        opt.setRequired(true);
        options.addOption(opt);


        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLine commandLine = null;
        CommandLineParser parser = new PosixParser();

        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                // 打印使用帮助
                hf.printHelp("testApp", options, true);
            }

            // 打印opts的名称和值
            System.out.println("--------------------------------------");
            Option[] opts = commandLine.getOptions();
            if (opts != null) {
                for (Option opt1 : opts) {
                   /* String name = opt1.getLongOpt();
                    String value = commandLine.getOptionValue(name);
                    System.out.println(name + "=>" + value);*/
                    if ("username".equalsIgnoreCase(opt1.getLongOpt())) {
                        username = commandLine.getOptionValue(opt1.getLongOpt());
                    }
                    if ("password".equalsIgnoreCase(opt1.getLongOpt())) {
                        password = commandLine.getOptionValue(opt1.getLongOpt());
                    }
                }
            }
            return checkParameters();
        } catch (ParseException e) {
            hf.printHelp("login", options, true);
            return false;
        }

    }

    public void excute() {
        User user2 = userMapper.findUserByUserName(username);
         if(user2==null)
       {
           System.out.println("该用户不存在！");
       }
       else if(  user2.getPassword().equals(password) ){
            Login.setUser(user2);
            System.out.println("成功登陆");
        }
        else {
            System.out.println("密码错误！");
        }
    }

    public boolean checkParameters() {
        if("".equals(username) || username==null) return false;

        return true;
    }
}
