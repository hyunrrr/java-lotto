package lotto;

import lotto.domain.*;
import lotto.view.InputView;
import lotto.view.OutputView;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        PurchasingPrice purchasingPrice = new PurchasingPrice(InputView.inputPurchasingPrice());
        int lottoTicketCounts = purchasingPrice.calculatePurchasableTicketCounts();
        OutputView.printLottoCountMessage(lottoTicketCounts);
        LottoTickets lottoTickets = LottoTickets.generateLottoTickets(lottoTicketCounts, new RandomLottoNumberGenerator());
        OutputView.printLottoTicketNumbers(lottoTickets);
        List<Integer> winningTicketNumbers = InputView.inputWinningTicketNumbers();
        int bonusBallNumber = InputView.inputBonusBallNumber();
        WinningLottoTicket winningLottoTicket = WinningLottoTicket.of(winningTicketNumbers, bonusBallNumber);
        LottoStatistics lottoStatistics = lottoTickets.getStatistics(winningLottoTicket);
        OutputView.printLottoResult(lottoStatistics, purchasingPrice);
    }
}
