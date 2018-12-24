import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NewYearBot extends TelegramLongPollingBot {

    User from;

    public void onUpdateReceived(Update update) {

        //ToDo Database

        if (update.hasMessage() && update.getMessage().hasText()) {
            from = update.getMessage().getFrom();
            if (GlobalVars.mongoConnector.returnAllIds().contains(from.getId().toString())){//if already exist
                if(update.getMessage().getText().equals("/help")){
                    sendMsg(update.getMessage().getChatId(),MessageHandler.handleHelp());
                } else{
                    sendMsg(update.getMessage().getChatId(),MessageHandler.handleTypicalAnswer(from.getId(), from.getFirstName()));
               }
            } else {
                sendMsg(update.getMessage().getChatId(),MessageHandler.handle(from.getId(), from.getFirstName()));
            }
        }
    }


    public void sendMsg(Long chatId, String text){
        try {
            execute(new SendMessage()
                    .setChatId(chatId)
                    .setText(text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "NewYearPartyBot";
    }

    public String getBotToken() {
        return "702251300:AAEOCTVAtkWlBqKftrD_r9MZQKqKGntT_44";
    }


}
