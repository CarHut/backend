package com.carhut;

import com.carhut.database.repository.BrandRepository;
import com.carhut.database.repository.ModelRepository;
import com.carhut.datatransfer.AutobazarEUCarRepository;
import com.carhut.datatransfer.ExcelDataTransfer;
import com.carhut.services.CarHutAPIService;
import com.carhut.services.DataTransferService;
import com.carhut.temputils.repo.TempCarRepository;
import com.carhut.util.loggers.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class CarHutApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarHutApplication.class, args);
	}

	@Bean
	public DataTransferService dataTransferService(AutobazarEUCarRepository repository, TempCarRepository tempCarRepository) {
		return new DataTransferService(repository, tempCarRepository);
	}

	@Bean
	public ExcelDataTransfer excelDataTransfer(DataTransferService dataTransferService) {
		return new ExcelDataTransfer(dataTransferService);
	}

	@Bean
	public CarHutAPIService carHutAPIService(AutobazarEUCarRepository repository, BrandRepository brandRepository, ModelRepository modelRepository, TempCarRepository tempCarRepository) {
		return new CarHutAPIService(repository, brandRepository, modelRepository, tempCarRepository);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Logger logger() {
		return new Logger();
	}

}
