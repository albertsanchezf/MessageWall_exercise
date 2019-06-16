package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageWall_and_RemoteLogin_Impl implements RemoteLogin, MessageWall {

    private List<Message> messages;
    private Hashtable<String,String> pwdTable = new Hashtable<String,String>();
    
    public MessageWall_and_RemoteLogin_Impl() throws NoSuchAlgorithmException
    {
        messages = new ArrayList<Message>();
        pwdTable.put("albert", encrypt("albertpwd"));
        pwdTable.put("wmad", encrypt("wmadpwd"));
        pwdTable.put("generic", encrypt("genericpwd"));
    }

    @Override
    public UserAccess connect(String usr, String passwd) 
    {
        UserAccess ua = null;
        try 
        {
            String hashedpwd = pwdTable.get(usr);
            if (hashedpwd.equals(encrypt(passwd)))
                ua = new UserAccess_Impl(this,usr);      
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            Logger.getLogger(MessageWall_and_RemoteLogin_Impl.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            return ua;
        }
    }

    @Override
    public void put(String user, String msg) 
    {
        Message message = new Message_Impl(user,msg);
        messages.add(message);
    }

    @Override
    public boolean delete(String user, int index) 
    {
        boolean removed = false;
        Message msg = messages.get(index);
        
        
        if (msg.getOwner() == user)
        {
            messages.remove(msg);
            removed = true;
        }
        
        return removed;
    }

    @Override
    public Message getLast() 
    {
        int size = messages.size();
        Message msg;
        
        if (size > 0)
            msg = messages.get(size-1);
        else
            msg = new Message_Impl("(No user)","(No message)");
                
        return msg;
    }

    @Override
    public int getNumber() 
    {
        return messages.size();
    }

    @Override
    public List<Message> getAllMessages() 
    {
        return messages;
    }
    
    public String encrypt(String str) throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        String encryptedString;
        
        messageDigest.update(str.getBytes());
        encryptedString = new String(messageDigest.digest());
        
        return encryptedString;
    }
}
