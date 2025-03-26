package space.mafin.finadvicebot;

import space.mafin.finadvicebot.model.UserData;

public class UserDataTest {

    public static void main(String[] args) {
        UserData userData = new UserData();
        userData.setIncome(100_000);
        userData.setStartSavings(35);
        userData.setEndSavings(60);
        userData.setUseSavings(80);
        userData.setAnnualRate(10);
        userData.setDeviation(30);
        String result = userData.calculateResult();
        System.out.println(result);
    }

}
