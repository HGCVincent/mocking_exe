package parking;

import mockit.Mock;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InOrderParkingStrategyTest {

	@Test
    public void testCreateReceipt_givenACarAndAParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {
	    Car LeoCar = mock(Car.class);
	    when(LeoCar.getName()).thenReturn("LeoCar");

	    ParkingLot parkingLot = mock(ParkingLot.class);
	    when(parkingLot.getName()).thenReturn("Parking Lot A");


	    InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();

	    Receipt receipt = inOrderParkingStrategy.createReceipt(parkingLot,LeoCar);

	    verify(LeoCar,times(1)).getName();
        verify(parkingLot,times(1)).getName();

        assertEquals("LeoCar",receipt.getCarName());
        assertEquals("Parking Lot A", receipt.getParkingLotName());

    }

    @Test
    public void testCreateNoSpaceReceipt_givenACar_thenGiveANoSpaceReceipt() {

        /* Exercise 1, Write a test case on InOrderParkingStrategy.createNoSpaceReceipt()
         * With using Mockito to mock the input parameter */

        Car LeoCar = mock(Car.class);
        when(LeoCar.getName()).thenReturn("LeoCar2");


        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();

        Receipt receipt = inOrderParkingStrategy.createNoSpaceReceipt(LeoCar);

        verify(LeoCar,times(1)).getName();

        assertEquals("LeoCar2",receipt.getCarName());
        assertEquals(ParkingStrategy.NO_PARKING_LOT, receipt.getParkingLotName());


    }

    @Test
    public void testPark_givenNoAvailableParkingLot_thenCreateNoSpaceReceipt(){

	    /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for no available parking lot */

        Car vincentCar = new Car("vincentCar");
        spy(new InOrderParkingStrategy());
        InOrderParkingStrategy spyInOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Receipt receipt = spyInOrderParkingStrategy.park(null,vincentCar);
        verify(spyInOrderParkingStrategy).createNoSpaceReceipt(vincentCar);


    }

    @Test
    public void testPark_givenThereIsOneParkingLotWithSpace_thenCreateReceipt(){

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot */
        Car vincentCar = new Car("vincentCar");
        ParkingLot parkingLot = new ParkingLot("Parking Lot A", 1);
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
        InOrderParkingStrategy spyInOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Receipt receipt = spyInOrderParkingStrategy.park(parkingLots, vincentCar);
        verify(spyInOrderParkingStrategy).createReceipt(parkingLot,vincentCar);
        assertEquals("vincentCar",receipt.getCarName());
        assertEquals("Parking Lot A",receipt.getParkingLotName());
    }

    @Test
    public void testPark_givenThereIsOneFullParkingLot_thenCreateReceipt(){
        Car vincentCar = new Car("vincentCar");
        ParkingLot parkingLot = new ParkingLot("Parking Lot A", 1);
        parkingLot.getParkedCars().add(new Car("otherCar"));
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
        InOrderParkingStrategy spyInOrderParkingStrategy = spy(new InOrderParkingStrategy());
        spyInOrderParkingStrategy.park(parkingLots,vincentCar);
        verify(spyInOrderParkingStrategy, times(0)).createReceipt(parkingLot,vincentCar);
        verify(spyInOrderParkingStrategy, times(1)).createNoSpaceReceipt(vincentCar);
        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot but it is full */

    }

    @Test
    public void testPark_givenThereIsMultipleParkingLotAndFirstOneIsFull_thenCreateReceiptWithUnfullParkingLot(){
        /* Exercise 3: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for multiple parking lot situation */
        Car vincentCar = new Car("vincentCar");
        ParkingLot parkingLot1 = new ParkingLot("Parking Lot A", 1);
        parkingLot1.getParkedCars().add(new Car("otherCar"));
        ParkingLot parkingLot2 = new ParkingLot("Parking Lot B", 1);
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot1,parkingLot2);
        InOrderParkingStrategy spyInOrderParkingStrategy = spy(new InOrderParkingStrategy());
        Receipt receipt = spyInOrderParkingStrategy.park(parkingLots,vincentCar);

        assertEquals("vincentCar",receipt.getCarName());
        assertEquals("Parking Lot B",receipt.getParkingLotName());

    }


}
