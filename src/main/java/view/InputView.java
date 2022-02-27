package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class InputView {

    private static final String QUESTION_MONEY_INPUT = "구매금액을 입력해 주세요.";
    private static final String QUESTION_WINNING_NUMBERS = "지난 주 당첨 번호를 입력해 주세요.";
    private static final String QUESTION_BONUS_NUMBER = "보너스 볼을 입력해 주세요.";
    private static final String NUMBER_DELIMITER = ", ";
    private static final String NONE_NUMERIC_ERROR = "[ERROR] 숫자만 입력이 가능합니다.";

    private static final Scanner scanner = new Scanner(System.in);

    public static String askMoneyInput() {
        System.out.println(QUESTION_MONEY_INPUT);
        return scanner.nextLine();
    }

    public static List<Integer> askWinningNumbers() {
        System.out.println(QUESTION_WINNING_NUMBERS);
        List<String> numbers = Arrays.asList(scanner.nextLine().split(NUMBER_DELIMITER));
        return validateAndCovertNumbersToInteger(numbers);
    }

    public static int askBonusNumber() {
        System.out.println(QUESTION_BONUS_NUMBER);
        return validateAndConvertInteger(scanner.nextLine());
    }

    private static List<Integer> validateAndCovertNumbersToInteger(List<String> numbers) {
        return numbers.stream()
                .map(InputView::validateAndConvertInteger)
                .collect(Collectors.toList());
    }

    private static int validateAndConvertInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(NONE_NUMERIC_ERROR);
        }
    }
}
