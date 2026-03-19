package simsgame.career;

import java.util.ArrayList;
import java.util.List;

/**
 * CareerFactory is a factory class responsible for creating Career instances based on specified types.
 * It provides a centralized way to manage different career types and their configurations.
 */
public class CareerFactory {

    // private CareerFactory() {
    // }

    // =====================================================
    // Core behaviour
    // =====================================================

    /**
     * Creates a Career instance based on the specified type and custom title.
     *
     * @param type The type of career to create.
     * @param customTitle An optional custom title for the career.
     * 
     * @return A Career instance of the specified type.
     */
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
                    )
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

    /**
     * Retrieves a list of available career types.
     *
     * @return A list of strings representing the names of available career types.
     */
    public static List<String> getCareerTypes() {
        List<String> types = new ArrayList<>();
        types.add("Shift Job");
        types.add("Fixed-Time Job");
        types.add("Commission Job");
        return types;
    }
}