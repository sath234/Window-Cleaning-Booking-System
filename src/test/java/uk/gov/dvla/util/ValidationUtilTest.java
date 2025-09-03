package uk.gov.dvla.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.gov.dvla.model.Booking;
import uk.gov.dvla.model.Customer;
import uk.gov.dvla.model.exception.DuplicateEntityException;
import uk.gov.dvla.model.exception.InvalidDateRangeException;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

public class ValidationUtilTest {

    static Stream<Arguments> duplicateObjectTestCases() {
        return Stream.of(
                Arguments.of(
                        Map.of(1, new Customer(1, "Nathan", 5)),
                        1,
                        "Customer",
                        "Duplicate Customer not allowed"
                ),
                Arguments.of(
                        Map.of(1, new Booking(1, 1, LocalDate.now())),
                        1,
                        "Booking",
                        "Duplicate Booking not allowed"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("duplicateObjectTestCases")
    public void checkDuplicateObjectInListThrowsException(Map<Integer, Object> map, int key, String objectName, String expectedMessage) {
        DuplicateEntityException exception = Assertions.assertThrows(DuplicateEntityException.class, () -> {
            ValidationUtil.checkDuplicateKeyInMap(map, key, objectName);
        });

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    static Stream<Arguments> uniqueObjectTestCases() {
        return Stream.of(
                Arguments.of(
                        Map.of(1, new Customer(1, "Nathan", 5)),
                        2,
                        "Customer"
                ),
                Arguments.of(
                        Map.of(1, new Booking(1, 1, LocalDate.now())),
                        2,
                        "Booking"
                ),
                Arguments.of(
                        Map.of(),
                        1,
                        "Test"

                )
        );
    }

    @ParameterizedTest
    @MethodSource("uniqueObjectTestCases")
    public void checkDuplicateObjectInListDoesNotThrow(Map<Integer, Object> map, int key, String objectName) {
        Assertions.assertDoesNotThrow(() -> {
            ValidationUtil.checkDuplicateKeyInMap(map, key, objectName);
        });
    }

    static Stream<Arguments> nullObjectTestCases() {
        return Stream.of(
                Arguments.of(null, "Customer", "Customer cannot be null"),
                Arguments.of(null, "Booking", "Booking cannot be null")
        );
    }

    @ParameterizedTest
    @MethodSource("nullObjectTestCases")
    public void checkObjectIsNotNullThrowsException(Object object, String objectType, String expectedMessage) {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () -> {
            ValidationUtil.checkObjectIsNotNull(object, objectType);
        });

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    static Stream<Arguments> validObjectTestCases() {
        return Stream.of(
                Arguments.of(new Customer(1, "Nathan", 5), "Customer"),
                Arguments.of(new Booking(1, 1, LocalDate.now()), "Booking")
        );
    }

    @ParameterizedTest
    @MethodSource("validObjectTestCases")
    public void checkObjectIsNotNullDoesNotThrow(Object object, String objectType) {
        Assertions.assertDoesNotThrow(() -> {
            ValidationUtil.checkObjectIsNotNull(object, objectType);
        });
    }

    @Test
    public void checkStartDateIsBeforeEndDate_InvalidDateRange_ThrowsException(){
        Assertions.assertThrows(InvalidDateRangeException.class, () -> {
            ValidationUtil.checkStartDateIsBeforeEndDate(LocalDate.of(2024, 6, 10), LocalDate.of(2024, 6, 5));
        });
    }

    @Test
    public void checkStartDateIsBeforeEndDate_ValidDateRange_DoesNotThrowsException(){
        Assertions.assertDoesNotThrow(() -> {
            ValidationUtil.checkStartDateIsBeforeEndDate(LocalDate.of(2024, 6, 10), LocalDate.of(2024, 6, 11));
        });
    }
}
