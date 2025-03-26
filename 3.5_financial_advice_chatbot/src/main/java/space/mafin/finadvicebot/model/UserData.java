package space.mafin.finadvicebot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Getter
@Setter
@NoArgsConstructor
public class UserData {

    private static final NumberFormat FORMATTER = new DecimalFormat("#0.00");

    private Integer income;
    private Integer startSavings;
    private Integer endSavings;
    private Integer useSavings;
    private Integer annualRate;
    private Integer deviation;

    public boolean isAllFieldsFilled() {
        return this.income != null && this.startSavings != null && this.endSavings != null && this.useSavings != null
                && this.annualRate != null && this.deviation != null;
    }

    public String calculateResult() {
        double Y = this.income;
        double r = ((double) this.annualRate) / 100d;
        double h = this.deviation;
        double t1 = this.startSavings;
        double t2 = this.endSavings;
        double t3 = this.useSavings;

        double N1 = t2 - t1;
        double i = 0.063d;
        double rr = ((1d + r) / (1d + i)) - 1d;
        double FVA_S = (Math.pow(1d + rr, N1) - 1d) / rr;

        double N2 = t3 - t2;
        double PVA_C = (1d - (Math.pow(1d + rr, 0d - N2))) / (rr);

        double C = (FVA_S * Y) / (PVA_C + FVA_S);
        double S = Y - C;

        return String.format("To ensure a constant level of consumption from %s to %s years in the amount of %s UAH per year " +
                        "you need to save %s UAH annually at a nominal interest rate of %s%%.",
                this.endSavings, this.useSavings, FORMATTER.format(C), FORMATTER.format(S), this.annualRate);
//        return String.format("Для забезпечення постійного рівня споживання від %s до %s років у розмірі %s грн. на рік " +
//                        "Вам потрібно щороку заощаджувати %s грн. під номінальну процентну ставку %s%%.",
//                this.endSavings, this.useSavings, FORMATTER.format(C), FORMATTER.format(S), this.annualRate);
    }
}
