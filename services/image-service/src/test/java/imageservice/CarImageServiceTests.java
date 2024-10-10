//package imageservice;
//
//import imageservice.dtos.request.CarHutCarInfoDto;
//import imageservice.dtos.request.ImagesDto;
//import imageservice.services.CarImageService;
//import imageservice.status.ImageServiceStatus;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.FileVisitResult;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.SimpleFileVisitor;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class CarImageServiceTests {
//
//    @Autowired
//    private CarImageService carImageService;
//
//    @AfterAll
//    public void cleanUp() {
//        try {
//            // Walk through the file tree in reverse order to delete children before parents
//            Files.walkFileTree(Path.of(System.getProperty("user.dir") + "/src/test/resources/test-images/test_car"), new SimpleFileVisitor<Path>() {
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    // Delete each file
//                    Files.delete(file);
//                    return FileVisitResult.CONTINUE;
//                }
//
//                @Override
//                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                    // Delete the directory after all children have been visited
//                    Files.delete(dir);
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//            System.out.println("Directory and all its contents have been successfully deleted.");
//        } catch (IOException e) {
//            System.err.println("Error deleting directory: " + e.getMessage());
//        }
//
//        carImageService.deleteAll();
//    }
//
//    @Test
//    @Order(1)
//    public void addImagesToDatabase_listOfImagesIsEmpty() throws Exception {
//        carImageService.addImagesToDatabase(new ImagesDto(new CarHutCarInfoDto(null, null),
//                new ArrayList<>()));
//    }
//
//    @Test
//    @Order(2)
//    public void addImagesToDatabase_carIsNull() throws Exception {
//        carImageService.addImagesToDatabase(new ImagesDto(null, new ArrayList<>()));
//    }
//
//    @Test
//    @Order(3)
//    public void addImagesToDatabase_imageListContainsData() throws Exception {
//        Thread.sleep(1000);
//
//        List<MultipartFile> images = new ArrayList<>();
//        MockMultipartFile image1 = new MockMultipartFile("image1", "image1.png", "image/jpg", "Image 1 content".getBytes());
//        MockMultipartFile image2 = new MockMultipartFile("image2", "image2.png", "image/jpeg", "Image 2 content".getBytes());
//        MockMultipartFile image3 = new MockMultipartFile("image3", "image3.png", "image/png", "Image 3 content".getBytes());
//
//        images.add(image1);
//        images.add(image2);
//        images.add(image3);
//
//        ImageServiceStatusInterface status = carImageService.addImagesToDatabase(new ImagesDto(new CarHutCarInfoDto("test_car", "user0"), images));
//        Assertions.assertEquals(ImageServiceStatus.SUCCESS, status);
//
//        Thread.sleep(1000);
//    }
//
//    @Test
//    @Order(4)
//    public void getImagesWithCarId_carIdIsNull() throws IOException {
//        List<byte[]> images = carImageService.getImagesWithCarId(null);
//        Assertions.assertNull(images);
//    }
//
//    @Test
//    @Order(5)
//    public void getImagesWithCarId_carIdIsNotNull() throws Exception {
//        List<byte[]> images = carImageService.getImagesWithCarId("test_car");
//        Assertions.assertNotNull(images);
//        Assertions.assertEquals(3, images.size());
//    }
//
//}
