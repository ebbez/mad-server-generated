package nl.hanze.se4.automaat.service;

import java.util.Optional;
import nl.hanze.se4.automaat.domain.Car;
import nl.hanze.se4.automaat.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link nl.hanze.se4.automaat.domain.Car}.
 */
@Service
@Transactional
public class CarService {

    private static final Logger LOG = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Save a car.
     *
     * @param car the entity to save.
     * @return the persisted entity.
     */
    public Car save(Car car) {
        LOG.debug("Request to save Car : {}", car);
        return carRepository.save(car);
    }

    /**
     * Update a car.
     *
     * @param car the entity to save.
     * @return the persisted entity.
     */
    public Car update(Car car) {
        LOG.debug("Request to update Car : {}", car);
        return carRepository.save(car);
    }

    /**
     * Partially update a car.
     *
     * @param car the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Car> partialUpdate(Car car) {
        LOG.debug("Request to partially update Car : {}", car);

        return carRepository
            .findById(car.getId())
            .map(existingCar -> {
                if (car.getBrand() != null) {
                    existingCar.setBrand(car.getBrand());
                }
                if (car.getModel() != null) {
                    existingCar.setModel(car.getModel());
                }
                if (car.getPicture() != null) {
                    existingCar.setPicture(car.getPicture());
                }
                if (car.getPictureContentType() != null) {
                    existingCar.setPictureContentType(car.getPictureContentType());
                }
                if (car.getFuel() != null) {
                    existingCar.setFuel(car.getFuel());
                }
                if (car.getOptions() != null) {
                    existingCar.setOptions(car.getOptions());
                }
                if (car.getLicensePlate() != null) {
                    existingCar.setLicensePlate(car.getLicensePlate());
                }
                if (car.getEngineSize() != null) {
                    existingCar.setEngineSize(car.getEngineSize());
                }
                if (car.getModelYear() != null) {
                    existingCar.setModelYear(car.getModelYear());
                }
                if (car.getSince() != null) {
                    existingCar.setSince(car.getSince());
                }
                if (car.getPrice() != null) {
                    existingCar.setPrice(car.getPrice());
                }
                if (car.getNrOfSeats() != null) {
                    existingCar.setNrOfSeats(car.getNrOfSeats());
                }
                if (car.getBody() != null) {
                    existingCar.setBody(car.getBody());
                }
                if (car.getLongitude() != null) {
                    existingCar.setLongitude(car.getLongitude());
                }
                if (car.getLatitude() != null) {
                    existingCar.setLatitude(car.getLatitude());
                }

                return existingCar;
            })
            .map(carRepository::save);
    }

    /**
     * Get all the cars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Car> findAll(Pageable pageable) {
        LOG.debug("Request to get all Cars");
        return carRepository.findAll(pageable);
    }

    /**
     * Get one car by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Car> findOne(Long id) {
        LOG.debug("Request to get Car : {}", id);
        return carRepository.findById(id);
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }
}
