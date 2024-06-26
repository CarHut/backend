package com.carhut;

import com.carhut.database.repository.BrandRepository;
import com.carhut.database.repository.ModelRepository;
import com.carhut.datatransfer.AutobazarEUCarRepository;
import com.carhut.datatransfer.ExcelDataTransfer;
import com.carhut.services.CarHutAPIService;
import com.carhut.services.DataTransferService;
import com.carhut.temputils.repo.TempCarRepository;
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

	@Deprecated
	@Bean
	public DataTransferService dataTransferService(AutobazarEUCarRepository repository, TempCarRepository tempCarRepository) {
		return new DataTransferService(repository, tempCarRepository);
	}

	@Deprecated
	@Bean
	public ExcelDataTransfer excelDataTransfer(DataTransferService dataTransferService) {
		return new ExcelDataTransfer(dataTransferService);
	}

	@Bean
	public CarHutAPIService carHutAPIService(BrandRepository brandRepository, ModelRepository modelRepository) {
		return new CarHutAPIService(brandRepository, modelRepository);
	}

}
