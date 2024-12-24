package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);

        return app;
    }

    private void registerAccount(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4 ||
            accountService.getAllAccounts().stream().anyMatch(a -> a.getUsername().equals(account.getUsername()))) {
            ctx.status(400);
        } else {
            Account createdAccount = accountService.addAccount(account);
            ctx.json(createdAccount);
        }
    }

    private void loginAccount(Context ctx) {
        Account loginDetails = ctx.bodyAsClass(Account.class);
        Account account = accountService.loginAccount(loginDetails);
        if (account == null) {
            ctx.status(401);
        } else {
            ctx.json(account);
        }
    }

    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() ||
            message.getMessage_text().length() > 255 ||
            accountService.getAllAccounts().stream().noneMatch(a -> a.getAccount_id() == message.getPosted_by())) {
            ctx.status(400);
        } else {
            
            System.out.println(message.toString());
            Message createdMessage = messageService.addMessage(message);
            System.out.println(createdMessage.toString());
            ctx.json(createdMessage);
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(messageId);
        if (message == null) {
            ctx.status(200).json("");
        } else {
            ctx.json(message);
        }
    }

    private void deleteMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByID(messageId);
        if (deletedMessage == null) {
            ctx.status(200).json("");
        } else {
            ctx.json(deletedMessage);
        }
    }

    private void updateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message existingMessage = messageService.getMessageByID(messageId);
        Message newMessageData = ctx.bodyAsClass(Message.class);

        if (existingMessage == null || newMessageData.getMessage_text() == null ||
            newMessageData.getMessage_text().isBlank() || newMessageData.getMessage_text().length() > 255) {
            ctx.status(400);
        } else {
            existingMessage.setMessage_text(newMessageData.getMessage_text());
            Message updatedMessage = messageService.updateMessage(existingMessage);
            ctx.json(updatedMessage);
        }
    }

    private void getMessagesByAccountId(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessageByUser(accountId);
        ctx.json(messages);
    }
}
