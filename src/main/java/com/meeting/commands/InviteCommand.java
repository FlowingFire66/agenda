package com.meeting.commands;

import com.meeting.mapper.AgendaMapper;
import com.meeting.mapper.UserMapper;
import com.meeting.mapper.mapperimpl.AgendaMapperImpl;
import com.meeting.mapper.mapperimpl.UserMapperImpl;
import com.meeting.pojo.Agenda;
import com.meeting.pojo.User;
import com.meeting.utils.AgendaUtil;
import com.meeting.utils.Login;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zhouyangjian
 * Date: 2017/10/19 0019
 * Time: 11:20
 * Description:邀请用户参加会议
 */
public class InviteCommand implements CommandIn {
    Agenda agenda = new Agenda();
    String[] participators=null;
    private AgendaMapper agendaMapper=new AgendaMapperImpl();
    private UserMapper userMapper=new UserMapperImpl();

    @Override
    public void simpleHelp() {
        System.out.printf("%-20s","invite");
        System.out.println("邀请一些用户参会:invite -p ly1 ly2 -t 人大");
    }

    @Override
    public boolean getOptions(String[] args) {

        Options options = new Options();
        Option opt = new Option("h", "help", false, "Print help");
        opt.setRequired(false);
        options.addOption(opt);




        opt = new Option("p", "participators", true, "participators最多接受10个人");
        opt.setRequired(true);
        opt.setArgs(10);
        options.addOption(opt);

        opt = new Option("t", "title", true, "指定邀请参加的会议");
        opt.setRequired(true);
        opt.setArgs(10);
        options.addOption(opt);

        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(110);
        CommandLine commandLine = null;
        CommandLineParser parser = new PosixParser();
        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                // 打印使用帮助
                hf.printHelp("invite", options, true);
            }

            // 打印opts的名称和值
            System.out.println("--------------------------------------");
            Option[] opts = commandLine.getOptions();

            if (opts != null) {
                for (Option opt1 : opts) {
                   /* String name = opt1.getLongOpt();
                    String value = commandLine.getOptionValue(name);
                    System.out.println(name + "=>" + value);*/
                    if("title".equalsIgnoreCase(opt1.getLongOpt())){
                        agenda.setTitle(commandLine.getOptionValue(opt1.getLongOpt()));
                    }

                    if("participators".equalsIgnoreCase(opt1.getLongOpt())){
                        participators=opt1.getValues();

                        // user.setPhone(commandLine.getOptionValue(opt1.getLongOpt()));
                    }
                }
            }
            //将获得的参数传到UserContorllor去注册。
            return checkParameters();

        }
        catch (ParseException e) {
            hf.printHelp("testApp", options, true);
            return false;
        }

    }

    @Override
    public void excute() {
        agenda=agendaMapper.findAgendaByTittle(agenda.getTitle());
        if(Login.isLogin()) {
            if(agenda==null) System.out.println("没这个会议");
            if(agenda.getInitiatorName().equals(Login.getUser().getUsername())) {
                boolean flag =inviteUser(participators, agenda);
                if (flag) {
                    System.out.println("邀请全部成功");
                } else {
                    System.out.println("邀请没全部成功");
                }
            }else {
                System.out.println("这不是您发起的会议！不能添加用户");
            }
        }else {
            System.out.println("请登录！");
        }
    }

    private boolean inviteUser(String[] users, Agenda agenda) {
        List<User> userList=new ArrayList<User>();
        boolean flag=true;
        List<String> usernames= agenda.getParticipators();
        logger.debug("获取用户信息");
        for (String username:
                users) {

            User user =userMapper.findUserByUserName(username);
            if (user!=null){
                userList.add(user);
            }

        }

        logger.debug("判断是否能参加");
        for (User user:
                userList) {
            if(!AgendaUtil.isAttend(user,agenda)){
                System.out.println(user.getUsername()+": 不能参加会议");
                flag=false;
            }else {
                logger.debug("更新会议对象信息");
                usernames.add(user.getUsername());
                logger.debug("更新用户信息到磁盘");
                List<Agenda> agendaList =user.getAttendgendas();
                if(agendaList==null){
                    agendaList=new ArrayList<Agenda>();
                    user.setAttendgendas(agendaList);
                }
                agendaList.add(agenda);
                user.setAttendgendas(agendaList);
                userMapper.addUser(user);
                System.out.println(user.getUsername()+": 成功邀请");
            }
        }


        logger.debug("更新会议对象信息");
        agenda.setParticipators(usernames);
        logger.debug("更新会议信息到磁盘");
        agendaMapper.add(agenda);

        logger.debug("更新创建人信息");
        String username = Login.getUser().getUsername();
        User userByUserName = userMapper.findUserByUserName(username);
        List<Agenda> createdgendas = userByUserName.getCreatedgendas();
        if(createdgendas==null){
            createdgendas=new ArrayList<Agenda>();
            userByUserName.setCreatedgendas(createdgendas);
        }
        createdgendas.remove(agenda);
        createdgendas.add(agenda);
        userMapper.addUser(userByUserName);


        return flag;
    }

    @Override
    public boolean checkParameters() {
        for (String s:
             participators) {
            if ("".equals(s)||s==null) return false;
        }
        return true;
    }
}
