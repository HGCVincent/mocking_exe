package parking;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class VipParkingStrategyTest {

	@Test
    public void testPark_givenAVipCarAndAFullParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

	    /* Exercise 4, Write a test case on VipParkingStrategy.park()
	    * With using Mockito spy, verify and doReturn */

	    Car vincentCar = new Car("vincentCarA");
	    ParkingLot parkingLot = new ParkingLot("Parking Lot A",1);
	    parkingLot.getParkedCars().add(new Car("other car"));
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        doReturn(true).when(spyVipParkingStrategy).isAllowOverPark(vincentCar);

        Receipt receipt = spyVipParkingStrategy.park(parkingLots,vincentCar);

        assertEquals("vincentCarA",receipt.getCarName());
        assertEquals("Parking Lot A",receipt.getParkingLotName());
    }

    @Test
    public void testPark_givenCarIsNotVipAndAFullParkingLog_thenGiveNoSpaceReceipt() {

        /* Exercise 4, Write a test case on VipParkingStrategy.park()
         * With using Mockito spy, verify and doReturn */

        Car vincentCar = new Car("vincentCarA");
        ParkingLot parkingLot = new ParkingLot("Parking Lot A",1);
        parkingLot.getParkedCars().add(new Car("other car"));
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        doReturn(false).when(spyVipParkingStrategy).isAllowOverPark(vincentCar);

        Receipt receipt = spyVipParkingStrategy.park(parkingLots,vincentCar);

        verify(spyVipParkingStrategy,times(0)).createReceipt(parkingLot, vincentCar);

        assertEquals("vincentCarA",receipt.getCarName());
        assertEquals("No Parking Lot",receipt.getParkingLotName());
    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsVipCar_thenReturnTrue(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */

        Car vincentCar = new Car("vincentCarA");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao carDao = mock(CarDao.class);

        when(carDao.isVip("vincentCarA")).thenReturn(true);
        doReturn(carDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(vincentCar);

        assertTrue(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsVipCar_thenReturnFalse(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */

        Car vincentCar = new Car("vincentCar");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao carDao = mock(CarDao.class);

        when(carDao.isVip("vincentCar")).thenReturn(true);
        doReturn(carDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(vincentCar);

        assertFalse(allowOverPark);

    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsNotVipCar_thenReturnFalse(){
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        Car vincentCar = new Car("vincentCarA");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao carDao = mock(CarDao.class);

        when(carDao.isVip("vincentCarA")).thenReturn(false);
        doReturn(carDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(vincentCar);

        assertFalse(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsNotVipCar_thenReturnFalse() {
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */

        Car vincentCar = new Car("vincentCar");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao carDao = mock(CarDao.class);

        when(carDao.isVip("vincentCar")).thenReturn(false);
        doReturn(carDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(vincentCar);

        assertFalse(allowOverPark);
    }

    private Car createMockCar(String carName) {
        Car car = mock(Car.class);
        when(car.getName()).thenReturn(carName);
        return car;
    }
}
