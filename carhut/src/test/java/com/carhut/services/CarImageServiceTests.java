package com.carhut.services;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.requests.PrincipalRequest;
import com.carhut.security.models.AuthenticationPrincipal;
import com.carhut.services.carhutapi.CarHutAPIService;
import com.carhut.services.carhutapi.CarImageService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarImageServiceTests {

    @Autowired
    private CarImageService carImageService;
    @Autowired
    private CarHutAPIService carHutAPIService;
    @Value("${images.path}")
    private String baseImagesPath;

    private static CarHutCar car;

    @BeforeEach
    public void setup() {
        car = new CarHutCar(
                "user0", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
                "Test address",
                1,
                1,
                "Test header",
                "Test price",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, "color0", "color0", null, false, false,
                null, null, null, null, null
        );

        carHutAPIService.save(car);
    }

    @AfterEach
    public void tearDown() throws IOException, InterruptedException {
        car.setSellerId("user0");
        FileUtils.cleanDirectory(new File(baseImagesPath));
        carImageService.deleteAll();
        carHutAPIService.delete(car);
    }

    @Test
    public void addImagesToDatabase_listOfImagesIsEmpty() throws Exception {
        carImageService.addImagesToDatabase(new PrincipalRequest<>(), new ArrayList<>());
    }

    @Test
    public void addImagesToDatabase_carIsNull() throws Exception {
        carImageService.addImagesToDatabase(null, new ArrayList<>());
    }

    @Test
    public void addImagesToDatabase_imageListContainsData() throws Exception {
        Thread.sleep(1000);

        car.setSellerId("admin");

        List<MultipartFile> images = new ArrayList<>();
        MockMultipartFile image1 = new MockMultipartFile("image1", "image1.png", "image/jpg", "Image 1 content".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image2", "image2.png", "image/jpeg", "Image 2 content".getBytes());
        MockMultipartFile image3 = new MockMultipartFile("image3", "image3.png", "image/png", "Image 3 content".getBytes());

        images.add(image1);
        images.add(image2);
        images.add(image3);

        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");

        PrincipalRequest<CarHutCar> principalRequest = new PrincipalRequest<>();
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        principalRequest.setDto(car);

        carImageService.addImagesToDatabase(principalRequest, images);

        Thread.sleep(1000);
    }

    @Test
    public void getImagesWithCarId_carIdIsNull() throws IOException {
        List<byte[]> images = carImageService.getImagesWithCarId(null);
        Assertions.assertNull(images);
    }

    @Test
    public void getImagesWithCarId_carIdIsNotNull() throws Exception {
        addImagesToDatabase_imageListContainsData();

        List<byte[]> images = carImageService.getImagesWithCarId(car.getId());
        Assertions.assertNotNull(images);
        Assertions.assertEquals(3, images.size());
    }

}
