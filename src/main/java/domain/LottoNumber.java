package domain;

import util.Validator;

public class LottoNumber implements Comparable<LottoNumber> {

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 45;
    private static final String LOTTO_NUMBER_OUT_OF_RANGE_EXCEPTION
            = "[ERROR] 로또 번호는 1이상 45이하여야 합니다.";
    private final int number;

    public LottoNumber(int number) {
        validate(number);
        this.number = number;
    }
    
    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(LottoNumber other) {
        Validator.checkArgumentIsNull(other);
        return this.number - other.number;
    }

    private void validate(int number) {
        if (number < MIN_VALUE || number > MAX_VALUE) {
            throw new IllegalArgumentException(LOTTO_NUMBER_OUT_OF_RANGE_EXCEPTION);
        }
    }
}
