package imageservice.status;

public enum ImageServiceError implements ImageServiceResultInterface {
    ERROR_OBJECT_IS_NULL,
    IMAGE_LIST_IS_EMPTY,
    ERROR_CREATING_DIRECTORY,
    ERROR_UPLOADING_IMAGE_TO_DATABASE, ERROR_UPLOADING_IMAGE_TO_FILE_SYSTEM
}
