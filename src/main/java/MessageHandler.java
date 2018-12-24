
public class MessageHandler {

    public static String handle(Integer id, String name) {
        //TODO Which group.
        int min = 1;
        int max = 4;
        int number = 0;
        boolean numberIsNotFound = true;

        while (numberIsNotFound) {
            number = (int) ((Math.random() * (max - min)))+1;//generate
            if (GlobalVars.mongoConnector.returnGroupSize(number)<3) {//if group with that number has <3 people
                numberIsNotFound = false;//end while
            }
        }

        GlobalVars.mongoConnector.saveMessage(id,name,number);
        return "You receive number " + number;
    }

    public static String handleTypicalAnswer(Integer id, String name) {
        StringBuilder names = new StringBuilder();
        for (String key:GlobalVars.mongoConnector.returnNamesInGroup(GlobalVars.mongoConnector.returnGroupNumberByName(name))){
            names.append(key+",");
        }
        names.append("\n");
        return name + ", your number " + GlobalVars.mongoConnector.returnGroupNumberByName(name) + "\nIn your group:"
                +names
                +"You cannot change it.\nAsk for /help";
    }

    public static String handleHelp() {
        return "That bot was created in very narrow aims of group of people\nDate of birth: 10.12.18 ";
    }
}
