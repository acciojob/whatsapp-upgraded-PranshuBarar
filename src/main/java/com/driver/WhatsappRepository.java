package com.driver;


import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class WhatsappRepository {
    //here we will create our database (hashmaps)

    HashMap<Group, List<User>> groupUserMap;
    HashMap<Group, List<Message>> groupMessageMap;
    HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;


    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        User user = new User(name, mobile);
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){

        Group group = new Group();

        String name = "";

        if(users.size()==2){
            name = users.get(1).getName();
        }
        else if(users.size()>2){
            customGroupCount++;
            name = "Group " + customGroupCount;
        }

        group.setName(name);

        group.setNumberOfParticipants(users.size());

        //we are updating the adminMap over here
        adminMap.put(group,users.get(0));

        return group;
    }

    public int createMessage(String content){
        messageId++;
        Message message = new Message(messageId, content, new Date());
        return messageId;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        List<User> usersList = groupUserMap.get(group);

        if(!usersList.contains(sender)){
            throw new Exception("You are not allowed to send message");
        }

        groupMessageMap.get(group).add(message);

        int finNoOfMessages = groupMessageMap.get(group).size();

        senderMap.put(message,sender);

        return finNoOfMessages;
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(!adminMap.get(group).getMobile().equals(approver.getMobile())){
            throw  new Exception("Approver does not have rights");
        }
        List<User> userList = groupUserMap.get(group);
        if(!userList.contains(user)){
            throw new Exception("User is not a participant");
        }

        adminMap.put(group,user);

        return "SUCCESS";

    }

    public int removeUser(User user) {
        return 0;
    }

    public String findMessage(Date start, Date end, int K) {
        return null;
    }
}
