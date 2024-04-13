package myGUI;

import javax.swing.*;

public class PostBase extends Post{
    String userName;
    public PostBase(){}
    public PostBase(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
}
