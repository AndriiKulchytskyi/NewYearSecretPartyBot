
public class MessageHandler {

    public static String handle(Integer id,String name) {
        //TODO Which group.
        int min=0;
        int max=9;
        int i = (int) (Math.random() * ( max - min ));
        GlobalVars.usersWithNumbers.put(id,i);
        return "You receive number " + i;
    }

    public static String handleTypicalAnswer(Integer id,String name) {
        return name+", you already received number " + GlobalVars.usersWithNumbers.get(id) + ". You cannot change it.\nAsk for /help";
    }

    public static String handleHelp(){
        return "That bot was created in very narrow aims of group of people\nDate of birth: 10.12.18 ";
    }
}
