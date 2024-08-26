package imageservice.dtos.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ImagesDto(CarHutCarInfoDto carHutCarInfoDto, List<MultipartFile> multipartFiles) {}
