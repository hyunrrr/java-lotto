package domain;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import util.Validator;

public class LottoGame {

    private static final int LOTTO_PRICE = 1000;
    private static final int EMPTY = 0;
    private static final int NO_YIELD = 0;
    private static final int INCREASE_COUNT = 1;
    private static final int BASE_COUNT = 0;
    private static final int START_INDEX = 0;
    private static final long BASE_LONG_SUM = 0L;
    private static final String LOTTO_NUMBER_DUPLICATED_EXCEPTION = "[ERROR] 로또번호와 보너스번호는 중복일 수 없습니다.";
    private static final int LOTTO_NUMBER_DIGIT = 6;
    private static final String LOTTO_NUMBER_DIGIT_EXCEPTION = "[ERROR] 로또는 6자리의 숫자로 이루어져 있습니다.";

    private Lotto winningLotto;
    private LottoNumber bonusNumber;
    private final Lottos lottos;

    public LottoGame(Money money) {
        lottos = buyLotto(money);
    }

    public LottoGame(List<Lotto> lottos) {
        this.lottos = new Lottos(lottos);
    }

    public static LottoGame startLottoGame(Money money) {
        return new LottoGame(money);
    }

    private Lottos buyLotto(Money money) {
        int lottoAmount = money.money() / LOTTO_PRICE;
        return Lottos.buyLottos(lottoAmount);
    }

    public void enterWinningLottoNumbersAndBonusNumber(List<Integer> notVerifiedWinningLottoNumbers
            , int notVerifiedBonusNumber) {
        Validator.checkArgumentIsNull(notVerifiedWinningLottoNumbers);
        validateLottoInput(notVerifiedWinningLottoNumbers, notVerifiedBonusNumber);
        Set<LottoNumber> winningLottoNumbers = notVerifiedWinningLottoNumbers.stream()
                .map(LottoNumber::new)
                .collect(Collectors.toSet());
        this.winningLotto = new Lotto(winningLottoNumbers);
        this.bonusNumber = new LottoNumber(notVerifiedBonusNumber);
    }

    public Map<Rewards, Integer> produceResults() {
        List<Rewards> ranks = convertLottoResultsToRanks();
        Map<Rewards, Integer> results = new EnumMap<>(Rewards.class);
        ranks.forEach(rank -> results.put(rank, results.getOrDefault(rank, BASE_COUNT) + INCREASE_COUNT));
        return results;
    }

    public float calculateYield() {
        if (lottos.numberOfLottery() == EMPTY) {
            return NO_YIELD;
        }
        List<Rewards> ranks = convertLottoResultsToRanks();
        long prizeSum = ranks.stream()
                .map(Rewards::getPrize)
                .reduce(BASE_LONG_SUM, Long::sum);
        return (float) prizeSum / lottos.numberOfLottery() / LOTTO_PRICE;
    }

    public Lottos getLottos() {
        return lottos;
    }

    private void validateLottoInput(List<Integer> notVerifiedWinningLottoNumbers
            , int notVerifiedBonusNumber) {
        validateLength(notVerifiedWinningLottoNumbers);
        validateNoDuplication(notVerifiedWinningLottoNumbers, notVerifiedBonusNumber);
    }

    private void validateLength(List<Integer> target) {
        if (target.isEmpty() || target.size() != LOTTO_NUMBER_DIGIT) {
            throw new IllegalArgumentException(LOTTO_NUMBER_DIGIT_EXCEPTION);
        }
    }

    private void validateNoDuplication(List<Integer> targets, int additionalTarget) {
        targets.add(additionalTarget);
        Set<Integer> noDuplicatedTargets = new HashSet<>(targets);
        if (noDuplicatedTargets.size() != targets.size()) {
            throw new IllegalArgumentException(LOTTO_NUMBER_DUPLICATED_EXCEPTION);
        }
    }

    private List<Rewards> convertLottoResultsToRanks() {
        List<Integer> matchCounts = lottos.compareAllLottosWithWinningLotto(winningLotto);
        List<Boolean> bonusNumberContains = lottos.compareAllLottosWithBonusNumber(bonusNumber);
        return IntStream.range(START_INDEX, matchCounts.size())
                .boxed()
                .map(index -> LottoRewardLogic.convertToRank(matchCounts.get(index), bonusNumberContains.get(index)))
                .collect(Collectors.toList());
    }
}
