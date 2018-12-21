
public class MessageHandler {

    public static String handle(Integer id, String name) {
        //TODO Which group.
        int min = 1;
        int max = 4;
        int number = 0;
        boolean numberIsNotFound = true;
        while (numberIsNotFound) {
            number = (int) ((Math.random() * (max - min)))+1;
            if (!GlobalVars.usersWithNumbers.containsValue(number)) {
                numberIsNotFound = false;
            }
        }
        GlobalVars.usersWithNumbers.put(id, number);
        GlobalVars.usersNamesWithNumbers.put(name, number);
        return "You receive number " + number;
    }

    public static String handleTypicalAnswer(Integer id, String name) {
        StringBuilder names = new StringBuilder();
        for (String key:GlobalVars.usersNamesWithNumbers.keySet()){
            names.append(key+",");
        }
        names.append("\n");
        return name + ", your number " + GlobalVars.usersWithNumbers.get(id) + "\nIn your group:"
                +names
                +"You cannot change it.\nAsk for /help";
    }

    public static String handleHelp() {
        return "That bot was created in very narrow aims of group of people\nDate of birth: 10.12.18 ";
    }
}
