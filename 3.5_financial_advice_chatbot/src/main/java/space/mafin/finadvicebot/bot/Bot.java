package space.mafin.finadvicebot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import space.mafin.finadvicebot.enums.Command;
import space.mafin.finadvicebot.model.UserData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    private static final String INVALID_INPUT_MESSAGE = "Input data is incorrect. Please try again! " +
            "\nSelect /help command for help.";
//    private static final String INVALID_INPUT_MESSAGE = "Введені дані некоректні. Будь ласка, спробуйте ще раз! " +
//            "\nДля довідки виберіть команду /help";
    private static final int MIN_RATE = 1;
    private static final int MAX_RATE = 100;
    private static final List<String> COMMANDS = Arrays.stream(Command.values())
            .map(Command::name).collect(Collectors.toList());

    private static Map<Long, Command> userPreviousCommand = new HashMap<>();
    private static Map<Long, UserData> userDataMap = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        try {
            long userId = update.getMessage().getFrom().getId();
            String text = update.getMessage().getText();
            if (text == null || text.length() == 0) {
                System.out.println("Invalid text: " + text);
                sendMessage(INVALID_INPUT_MESSAGE, valueOf(update.getMessage().getChatId()));
            } else {
                System.out.println(String.format("User: %s \nText: %s", update.getMessage().getFrom().getUserName(), text));
                // Save to userPreviousCommand map in command
                if (isCommand(text)) {
                    Command command = Command.valueOf(clearCommand(text));
                    userPreviousCommand.put(userId, command);
                    if (command.equals(Command.clear)) {
                        userDataMap.remove(userId);
                        sendMessage(command.getMessage(), valueOf(update.getMessage().getChatId()));
                    } else if (command.equals(Command.generate)) {
                        // Check that all fields are filled
                        if (userDataMap.containsKey(userId) && userDataMap.get(userId).isAllFieldsFilled()) {
                            sendMessage(userDataMap.get(userId).calculateResult(), valueOf(update.getMessage().getChatId()));
                        } else {
                            sendMessage(generateEmptyFieldsMessage(userDataMap.containsKey(userId)
                                    ? userDataMap.get(userId) : new UserData()), valueOf(update.getMessage().getChatId()));
                        }
                    } else {
                        sendMessage(command.getMessage(), valueOf(update.getMessage().getChatId()));
                    }
                } else {
                    // If user do not has saved previous command than send /help message
                    if (!userPreviousCommand.containsKey(userId) || !userPreviousCommand.get(userId).isInputCommand()) {
                        sendMessage(Command.help.getMessage(), valueOf(update.getMessage().getChatId()));
                    } else {
                        // Validate input and save/update UserData
                        UserData userData = userDataMap.containsKey(userId) ? userDataMap.get(userId) : new UserData();
                        if (isValidInput(text, userPreviousCommand.get(userId), userData)) {
                            setUserDataField(userData, Integer.parseInt(text), userPreviousCommand.get(userId));
                            userDataMap.put(userId, userData);
                            sendMessage(generateDataSavedMessage(userData), valueOf(update.getMessage().getChatId()));
                        } else {
                            sendMessage(INVALID_INPUT_MESSAGE, valueOf(update.getMessage().getChatId()));
                        }
                    }
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String generateEmptyFieldsMessage(UserData userData) {
        StringBuilder result = new StringBuilder("First, you need to add the following data: ");
        if (userData.getIncome() == null)
            result.append("\n/income - Average annual income (in UAH)");
        if (userData.getStartSavings() == null)
            result.append("\n/inv_start - Age of start of the savings program");
        if (userData.getEndSavings() == null)
            result.append("\n/inv_end - Age of completion of the savings program");
        if (userData.getUseSavings() == null)
            result.append("\n/inv_use - Age till which you plan to use the savings fund");
        if (userData.getAnnualRate() == null)
            result.append("\n/rate - Nominal annual interest rate");
        if (userData.getDeviation() == null)
            result.append("\n/deviation - Critical decrease of the interest rate");
        return result.toString();
    }

    private String generateDataSavedMessage(UserData userData) {
        StringBuilder result = new StringBuilder("Data saved! Enter or correct other parameters");
        result.append("\n/income Average annual income (in UAH) - ").append((userData.getIncome() == null) ? "<b>Not yet filled</b>" : "<b>" + userData.getIncome() + "</b>");
        result.append("\n/inv_start Age of start of the savings program - ").append((userData.getStartSavings() == null) ? "<b>Not yet filled</b>" : "<b>" + userData.getStartSavings() + "</b>");
        result.append("\n/inv_end Age of completion of the savings program - ").append((userData.getEndSavings() == null) ? "<b>Not yet filled</b>" : "<b>" + userData.getEndSavings() + "</b>");
        result.append("\n/inv_use Age till which you plan to use the savings fund - ").append((userData.getUseSavings() == null) ? "<b>Not yet filled</b>" : "<b>" + userData.getUseSavings() + "</b>");
        result.append("\n/rate Nominal annual interest rate - ").append((userData.getAnnualRate() == null) ? "<b>Not yet filled</b>" : "<b>" + userData.getAnnualRate() + "</b>");
        result.append("\n/deviation Critical decrease of the interest rate - ").append((userData.getDeviation() == null) ? "<b>Not yet filled</b>" : "<b>" + userData.getDeviation() + "</b>");
        result.append("\nAfter entering all the data, you can generate your plan using the command /generate");
        return result.toString();
    }

//    private String generateEmptyFieldsMessage(UserData userData) {
//        StringBuilder result = new StringBuilder("Спочатку Ви повинні додати такі дані: ");
//        if (userData.getIncome() == null)
//            result.append("\n/income - Середній щорічний дохід (у гривнях)");
//        if (userData.getStartSavings() == null)
//            result.append("\n/inv_start - Вік старту програми заощадження");
//        if (userData.getEndSavings() == null)
//            result.append("\n/inv_end - Вік завершення програми заощадження");
//        if (userData.getUseSavings() == null)
//            result.append("\n/inv_use - Вік, до якого Ви плануєте використовувати фонд заощадження");
//        if (userData.getAnnualRate() == null)
//            result.append("\n/rate - Номінальна річна відсоткова ставка");
//        if (userData.getDeviation() == null)
//            result.append("\n/deviation - Критичне зменшення середньої доходності у відсотках");
//        return result.toString();
//    }

//    private String generateDataSavedMessage(UserData userData) {
//        StringBuilder result = new StringBuilder("Дані збережені! Введіть або виправте інші параметри");
//        result.append("\n/income Середній щорічний дохід (у гривнях) - ").append((userData.getIncome() == null) ? "<b>Дані ще не введено</b>" : "<b>" + userData.getIncome() + "</b>");
//        result.append("\n/inv_start Вік старту програми заощадження - ").append((userData.getStartSavings() == null) ? "<b>Дані ще не введено</b>" : "<b>" + userData.getStartSavings() + "</b>");
//        result.append("\n/inv_end Вік завершення програми заощадження - ").append((userData.getEndSavings() == null) ? "<b>Дані ще не введено</b>" : "<b>" + userData.getEndSavings() + "</b>");
//        result.append("\n/inv_use Вік, до якого Ви плануєте використовувати фонд заощадження - ").append((userData.getUseSavings() == null) ? "<b>Дані ще не введено</b>" : "<b>" + userData.getUseSavings() + "</b>");
//        result.append("\n/rate Номінальна річна відсоткова ставка - ").append((userData.getAnnualRate() == null) ? "<b>Дані ще не введено</b>" : "<b>" + userData.getAnnualRate() + "</b>");
//        result.append("\n/deviation Критичне зменшення середньої доходності у відсотках - ").append((userData.getDeviation() == null) ? "<b>Дані ще не введено</b>" : "<b>" + userData.getDeviation() + "</b>");
//        result.append("\nПісля введення усіх даних Ви можете згенерувати Ваш план за допомогою команди /generate");
//        return result.toString();
//    }

    private void setUserDataField(UserData userData, int i, Command command) {
        switch (command) {
            case income:
                userData.setIncome(i);
                break;
            case inv_start:
                userData.setStartSavings(i);
                break;
            case inv_end:
                userData.setEndSavings(i);
                break;
            case inv_use:
                userData.setUseSavings(i);
                break;
            case rate:
                userData.setAnnualRate(i);
                break;
            case deviation:
                userData.setDeviation(i);
                break;
        }
    }

    private boolean isValidInput(String text, Command command, UserData userData) {
        try {
            int input = Integer.parseInt(text);
            if (input <= 0) {
                return false;
            }
            if (command.equals(Command.rate) && (input < MIN_RATE || input > MAX_RATE)) {
                return false;
            } else if (command.equals(Command.deviation) && input > 100) {
                return false;
            } else if (command.equals(Command.inv_end)) {
                if (userData.getStartSavings() != null && input < userData.getStartSavings()) {
                    return false;
                } else if (userData.getUseSavings() != null && input > userData.getUseSavings()) {
                    return false;
                }
            } else if (command.equals(Command.inv_use)) {
                if (userData.getStartSavings() != null && input < userData.getStartSavings()) {
                    return false;
                }
                if (userData.getEndSavings() != null && input < userData.getEndSavings()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void sendMessage(String text, String chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(text);
        execute(sendMessage);
    }

    private boolean isCommand(String text) {
        if (text != null && text.startsWith("/")) {
            if (text.contains("@")) {
                if (!text.endsWith(botUsername)) {
                    return false;
                }
            }
            text = clearCommand(text);
            if (COMMANDS.contains(text)) {
                return true;
            }
        }
        return false;
    }

    private String clearCommand(String text) {
        if (text.contains("@")) {
            text = text.substring(0, text.indexOf("@"));
        }
        text = text.replaceAll("/", "");
        return text;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
