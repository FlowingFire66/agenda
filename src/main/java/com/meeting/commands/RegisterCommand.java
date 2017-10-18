package com.meeting.commands;

import com.meeting.mapper.UserMapper;
import com.meeting.mapper.mapperimpl.UserMapperImpl;
import com.meeting.pojo.User;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
/**
 * User: FlowingFire
 * Date: 2017/10/15 0015
 * Time: 17:46
 * Description:
 */
public class RegisterCommand implements CommandIn {
    User user =new User();
    private UserMapper userMapper=new UserMapperImpl();
    public void simpleHelp() {
        System.out.printf("%-20s","register");
        System.out.println("用户注册::register -u ly4 -p ly4 -e 11233@qq.com -pn 11231223");

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
        opt = new Option("e", "email", true, "email");
        opt.setRequired(true);
        options.addOption(opt);
        opt = new Option("pn", "phone", true, "phone");
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
                hf.printHelp("register", options, true);
            }

            // 打印opts的名称和值
            System.out.println("--------------------------------------");
            Option[] opts = commandLine.getOptions();
            if (opts != null) {
                for (Option opt1 : opts) {
                    if("username".equalsIgnoreCase(opt1.getLongOpt())){
                        user.setUsername(commandLine.getOptionValue(opt1.getLongOpt()));
                    }
                    if("password".equalsIgnoreCase(opt1.getLongOpt())){
                        user.setPassword(commandLine.getOptionValue(opt1.getLongOpt()));
                    }
                    if("email".equalsIgnoreCase(opt1.getLongOpt())){
                        user.setEmail(commandLine.getOptionValue(opt1.getLongOpt()));
                    }
                    if("phone".equalsIgnoreCase(opt1.getLongOpt())){
                        user.setPhone(commandLine.getOptionValue(opt1.getLongOpt()));
                    }
                }
            }
           //参数合法性检查
           return checkParameters();
        }
        catch (ParseException e) {
            hf.printHelp("testApp", options, true);
            return false;
        }
    }

    public void excute() {
        User user1=userMapper.findUserByUserName(user.getUsername());
        if(user1==null){
            userMapper.addUser(user);
            System.out.println("注册成功");
        }
         else{
            System.out.println("该用户已注册");
        }

 

    }

    public boolean checkParameters() {

        if("".equals(user.getUsername()) || user.getUsername()==null) return false;

        return true;
    }
}
