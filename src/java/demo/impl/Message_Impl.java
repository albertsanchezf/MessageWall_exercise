package demo.impl;

import demo.spec.Message;

public class Message_Impl implements Message, java.io.Serializable{
    
	private String user, message;
        
    public Message_Impl(String user, String message)
    {
        this.user = user;
        this.message = message;
    }

    @Override
    public String getContent() 
    {
        return message;
    }

    @Override
    public String getOwner() 
    {
        return user;
    }

	
}

