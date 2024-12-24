package Service;

import java.util.List;

import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message) {
        return this.messageDAO.insertMessage(message);
    }
    
    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id) {
        return this.messageDAO.getMessageByID(message_id);
    }
    
    public Message deleteMessageByID(int message_id) {
        
        Message retMsg = this.messageDAO.getMessageByID(message_id);
        this.messageDAO.deleteMessageByID(message_id);
        return retMsg;
    }

    public Message updateMessage(Message message) {
        return this.messageDAO.updateMessage(message);
    }

    public List<Message> getMessageByUser(int account_id) {
        return this.messageDAO.getMessageByUser(account_id);
    }

}
