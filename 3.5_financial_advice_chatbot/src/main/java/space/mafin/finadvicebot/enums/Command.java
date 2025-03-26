package space.mafin.finadvicebot.enums;

public enum Command {

    start("This bot helps determine how much you should invest annually in savings at a real percentage of r " +
            "to ensure a constant level of consumption over some period of time." +
            "\nTo view the commands, press /help", false),
    help("Use the following commands to enter the required information: " +
            "\n/income - What is your average annual income (in UAH)? Example: 100000" +
            "\n/inv_start - At what age do you plan to start your own savings program? Example: 35" +
            "\n/inv_end - At what age do you plan to complete your own savings program? Example: 55" +
            "\n/inv_use - By what age do you plan to use your own savings fund? Example: 75" +
            "\n/rate - What nominal annual interest rate on savings do you expect to receive (percentage)? Example: 10" +
            "\n/deviation - What percentage reduction (%) of your defined average return r will be critical for you in deciding to review (terminate) your savings program? Example: 33", false),
    income("What is your average annual income (in UAH)? Example: 100000", true),
    inv_start("At what age do you plan to start your own savings program? Example: 35", true),
    inv_end("At what age do you plan to complete your own savings program? Example: 55", true),
    inv_use("By what age do you plan to use your own savings fund? Example: 75", true),
    rate("What nominal annual interest rate on savings do you expect to receive (percentage)? Example: 10", true),
    deviation("What percentage reduction (%) of your defined average return r will be critical for you in deciding to review (terminate) your savings program? Example: 33", true),
    generate("Generating a savings plan...", false),
    clear("Your data has been reset", false)

//    start("Цей бот допомагає визначити, скільки щорічно Ви маєте вкладати у заощадження під реальний відсоток r " +
//            "для забезпечення постійного рівня споживання упродовж визначеного проміжку часу. " +
//            "\nДля ознайомлення з командами натисніть /help", false),
//    help("Для вводу необхідної інформації скористайтеся наступними командами: " +
//            "\n/income - Який Ви маєте середній щорічний дохід (у гривнях)? Наприклад: 100000" +
//            "\n/inv_start - З якого віку Ви плануєте запровадити власну програму заощадження? Наприклад: 35" +
//            "\n/inv_end - В якому віці Ви плануєте завершити власну програму заощадження? Наприклад: 55" +
//            "\n/inv_use - До якого віку Ви плануєте використати власний фонд заощадження? Наприклад: 75" +
//            "\n/rate - Яку номінальну річну відсоткову ставку на заощадження Ви очікуєте отримувати (у відсотках)? Наприклад: 10" +
//            "\n/deviation - Яке зменшення у відсотках (%) від визначеної Вами середньої доходності r буде для Вас критичним у прийнятті рішення переглянути (припинити) програму заощаджень? Наприклад: 33", false),
//    income("Який Ви маєте середній щорічний дохід (у гривнях)? Наприклад: 100000", true),
//    inv_start("З якого віку Ви плануєте запровадити власну програму заощадження? Наприклад: 35", true),
//    inv_end("В якому віці Ви плануєте завершити власну програму заощадження? Наприклад: 55", true),
//    inv_use("До якого віку Ви плануєте використати власний фонд заощадження? Наприклад: 75", true),
//    rate("Яку номінальну річну відсоткову ставку на заощадження Ви очікуєте отримувати (у відсотках)? Наприклад: 10", true),
//    deviation("Яке зменшення у відсотках (%) від визначеної Вами середньої доходності r буде для Вас критичним у прийнятті рішення переглянути (припинити) програму заощаджень? Наприклад: 33", true),
//    generate("Генерація плану заощаджень...", false),
//    clear("Ваші дані були скинуті", false)

    ;

    private String message;
    private boolean isInputCommand;

    Command(String message, boolean isInputCommand) {
        this.message = message;
        this.isInputCommand = isInputCommand;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInputCommand() {
        return isInputCommand;
    }

    public void setInputCommand(boolean inputCommand) {
        isInputCommand = inputCommand;
    }
}
