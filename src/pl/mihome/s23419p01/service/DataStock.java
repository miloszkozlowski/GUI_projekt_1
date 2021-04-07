package pl.mihome.s23419p01.service;

import pl.mihome.s23419p01.model.BookEntry;
import pl.mihome.s23419p01.model.CarService;
import pl.mihome.s23419p01.model.CustomerProperty;
import pl.mihome.s23419p01.model.ServiceHistoryEntry;
import pl.mihome.s23419p01.model.person.CarServiceOwner;
import pl.mihome.s23419p01.model.person.Person;
import pl.mihome.s23419p01.model.rent.*;
import pl.mihome.s23419p01.model.vehicle.CityCar;
import pl.mihome.s23419p01.model.vehicle.OffRoadCar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStock {

    private static DataStock INSTANCE;

    private final Map<CarService, CarServiceOwner> carServicesWithOwners = new ConcurrentHashMap<>();
    private final Set<BookEntry> paymentsBook = Collections.synchronizedSet(new HashSet<>());
    private final Set<ServiceHistoryEntry> servicesHistory = new HashSet<>();
    private final List<Person> people = new ArrayList<>();
    private Person currentUser;
    private LocalDate currentDate;

    private DataStock() {
        loadData();
        if(currentDate==null) {
            currentDate = LocalDate.now();
        }
    }

    public static DataStock getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DataStock();
        }
        return INSTANCE;
    }

    public void loadData() {}

    public void saveData() {}

    public Map<CarService, CarServiceOwner> getCarServicesWithOwners() {
        return carServicesWithOwners;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void nextDay() {
        currentDate = currentDate.plusDays(1);
    }

    public String getCurrentDateString() {
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("eeee, d LLLL y");
        return currentDate.format(dtf);
    }

    public void addCarService(CarService carService, CarServiceOwner owner) {
        carServicesWithOwners.put(carService, owner);
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public void addPaymentEntry(BookEntry bookEntry) {
        paymentsBook.add(bookEntry);
    }

    public void addServiceHistoryEntry(ServiceHistoryEntry entry) {
        servicesHistory.add(entry);
    }

    public Set<BookEntry> getPaymentsBook() {
        return paymentsBook;
    }

    public Set<ServiceHistoryEntry> getServicesHistory() {
        return servicesHistory;
    }

    public void addPerson(Person person) {
        people.add(person);
        people.sort(Comparator.comparing(Person::getLastName));
    }

    public void init() {
        CarServiceOwner owner = new CarServiceOwner("John", "Kowalsky", "670910221122", "ul. Januszowa 3, 99-876 Januszowo");
        addPerson(owner);
        CarService carService = new CarService("Auto-rakieta", BigDecimal.valueOf(0.1), BigDecimal.valueOf(1000), BigDecimal.valueOf(5));
        carServicesWithOwners.put(carService, owner);
        Random random = new Random();
        for(int i = 0; i<10; i++) {
            BigDecimal size = BigDecimal.valueOf(random.nextInt(20)).add(BigDecimal.valueOf(35));
            ConsumerWarehouse cw = new ConsumerWarehouse(size, size.multiply(BigDecimal.valueOf(1.47)));
            carService.addWarehouse(cw);
        }
        for(int i = 0; i<5; i++) {
            ParkingSpace ps = new ParkingSpace(BigDecimal.valueOf(16), BigDecimal.valueOf(70));
            carService.addWarehouse(ps);
        }
        for(int i = 0; i<10; i++) {
            new CarServiceSpot(BigDecimal.valueOf(450), carService);
        }
        for(int i = 0; i<20; i++) {
            new IndependentCarServiceSpot(BigDecimal.valueOf(150), carService);
        }
        Person person1 = new Person("Dobromir", "Wesołek", "85052933443", "ul. Indycza 8, Kutno");
        Person person2 = new Person("Sławomir Aleksander", "Danczak", "87043077886", "ul. Kurczakowa 8, Warszawa");
        Person person3 = new Person("Jarosław", "Kurczakinski", "90012933443", "ul. Wołowa 11, Sierpc");
        Person person4 = new Person("Będzimir", "Wołek","75092933443", "ul. Cielęca 18, Jarocin");
        Person person5 = new Person("Sędzimir", "Cielak", "12121933443", "ul. Wieprza 228, Samo Pole");
        Person person6 = new Person("Wszędobył", "Wieprzowski", "01230933443", "ul. Wietnamska 1, Zgierz");
        addPerson(person1);
        addPerson(person2);
        addPerson(person3);
        addPerson(person4);
        addPerson(person5);
        addPerson(person6);
        CityCar cityCar = new CityCar("Fiat Punto", BigDecimal.valueOf(15), false, 0);
        person6.addVehicle(cityCar);
        CityCar cityCar2 = new CityCar("BMW M5", BigDecimal.valueOf(21), true, 3);
        person3.addVehicle(cityCar2);
        OffRoadCar offRoadCar = new OffRoadCar("Toyota Landcruiser", BigDecimal.valueOf(27), true, 8);
        person3.addVehicle(offRoadCar);
        carService.serviceCar(cityCar, true);
        carService.serviceCar(cityCar2, false);
        try {
            ConsumerWarehouse rentableArea = carService.getAvailableConsumerWarehouse();
            rentableArea.rent(person2, LocalDate.now(), LocalDate.now().plusDays(3));
            rentableArea.insertItem(new CustomerProperty(BigDecimal.ONE, "Old Iron"));
            rentableArea.insertItem(new CustomerProperty(BigDecimal.valueOf(2.3), "Desk"));
            rentableArea.insertItem(new CustomerProperty(BigDecimal.valueOf(0.5), "Chair"));
        }
        catch (IllegalStateException | IllegalAccessException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            carService.getAvailableConsumerWarehouse().rent(person4, LocalDate.now(), LocalDate.now().plusDays(7));
        }
        catch (IllegalStateException | IllegalAccessException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            ParkingSpace ps = carService.getAvailableParkingSpace().rent(person6, LocalDate.now(), LocalDate.now().plusDays(10));
            ps.moveTheCarIn(offRoadCar);
        }
        catch (IllegalStateException | IllegalAccessException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void setCurrentUser(Person currentUser) {
        this.currentUser = currentUser;
    }
}
