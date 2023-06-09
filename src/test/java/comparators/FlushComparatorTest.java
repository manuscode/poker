package comparators;

import model.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static comparators.ComparisonResult.*;
import static model.CardSuit.*;
import static model.CardValue.*;
import static model.Hand.createHand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlushComparatorTest {

    private final HighCardComparator highCardComparator = mock(HighCardComparator.class);

    private final FlushComparator testee = new FlushComparator(highCardComparator);

    @Test
    void compare_shouldReturnNoMatch_whenNoHandHasFlush() {
        var firstHand = createHand(
                new Card(CLUBS, ACE),
                new Card(SPADES, TEN),
                new Card(DIAMONDS, JACK),
                new Card(HEARTS, SEVEN),
                new Card(HEARTS, JACK)
        );
        var secondHand = createHand(
                new Card(DIAMONDS, TWO),
                new Card(SPADES, JACK),
                new Card(HEARTS, SIX),
                new Card(DIAMONDS, EIGHT),
                new Card(SPADES, QUEEN)
        );

        var result = testee.compare(firstHand, secondHand);

        assertThat(result).isEqualTo(NO_MATCH);
    }

    @Test
    void compare_shouldReturnWinForFirstHand_whenOnlyFirstHandIsFlush() {
        var firstHand = createHand(
                new Card(CLUBS, ACE),
                new Card(CLUBS, TEN),
                new Card(CLUBS, JACK),
                new Card(CLUBS, SEVEN),
                new Card(CLUBS, JACK)
        );
        var secondHand = createHand(
                new Card(DIAMONDS, TWO),
                new Card(SPADES, JACK),
                new Card(HEARTS, SIX),
                new Card(DIAMONDS, EIGHT),
                new Card(SPADES, QUEEN)
        );

        var result = testee.compare(firstHand, secondHand);

        assertThat(result).isEqualTo(FIRST_HAND_WIN);
    }

    @Test
    void compare_shouldReturnWinForSecondHand_whenOnlySecondHandIsFlush() {
        var firstHand = createHand(
                new Card(DIAMONDS, TWO),
                new Card(SPADES, JACK),
                new Card(HEARTS, SIX),
                new Card(DIAMONDS, EIGHT),
                new Card(SPADES, QUEEN)
        );
        var secondHand = createHand(
                new Card(CLUBS, ACE),
                new Card(CLUBS, TEN),
                new Card(CLUBS, JACK),
                new Card(CLUBS, SEVEN),
                new Card(CLUBS, JACK)
        );

        var result = testee.compare(firstHand, secondHand);

        assertThat(result).isEqualTo(SECOND_HAND_WIN);
    }

    @ParameterizedTest
    @EnumSource(value = ComparisonResult.class)
    void compare_shouldReturnWinnerOfHighCard_whenBothHandsAreFlush(ComparisonResult winnerOfHighCard) {
        var firstHand = createHand(
                new Card(CLUBS, ACE),
                new Card(CLUBS, JACK),
                new Card(CLUBS, SIX),
                new Card(CLUBS, EIGHT),
                new Card(CLUBS, QUEEN)
        );
        var secondHand = createHand(
                new Card(SPADES, TWO),
                new Card(SPADES, TEN),
                new Card(SPADES, JACK),
                new Card(SPADES, SEVEN),
                new Card(SPADES, JACK)
        );

        when(highCardComparator.compare(firstHand, secondHand)).thenReturn(winnerOfHighCard);

        var result = testee.compare(firstHand, secondHand);

        assertThat(result).isEqualTo(winnerOfHighCard);
    }
}