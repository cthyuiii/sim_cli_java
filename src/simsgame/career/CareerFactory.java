package simsgame.career;

import java.util.ArrayList;
import java.util.List;

public class CareerFactory {

    private CareerFactory() {
    }

    public static Career createCareer(int type, String customTitle) {
        String title = (customTitle == null || customTitle.trim().isEmpty())
                ? "Unknown Job"
                : customTitle.trim();

        switch (type) {
            case 1:
                return new ShiftJob(
                    new CareerPromotion(
                        title,
                        new String[]{
                            "Junior " + title,
                            title,
                            "Senior " + title
                        },
                        new double[]{8, 12, 16},
                        new int[]{20, 40, Integer.MAX_VALUE}
                    ),
                    new int[]{4, 6, 8}
                );

            case 2:
                return new FixedTimeJob(
                    new CareerPromotion(
                        title,
                        new String[]{
                            "Junior " + title,
                            title,
                            "Senior " + title
                        },
                        new double[]{50, 80, 120},
                        new int[]{20, 40, Integer.MAX_VALUE}
                    ),
                    8
                );

            case 3:
                return new CommissionJob(
                    new CareerPromotion(
                        title,
                        new String[]{
                            "Beginner " + title,
                            title,
                            "Established " + title
                        },
                        new double[]{20, 35, 50},
                        new int[]{20, 40, Integer.MAX_VALUE}
                    ),
                    20,
                    5,
                    6
                );

            default:
                return null;
        }
    }

    public static List<String> getCareerTypes() {
        List<String> types = new ArrayList<>();
        types.add("Shift Job");
        types.add("Fixed-Time Job");
        types.add("Commission Job");
        return types;
    }
}