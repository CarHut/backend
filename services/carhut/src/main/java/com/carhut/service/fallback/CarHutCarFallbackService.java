package com.carhut.service.fallback;

import com.carhut.commons.model.CarHutCar;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.http.CarHutApiSRCaller;
import com.carhut.http.UserServiceCaller;
import com.carhut.request.RemoveCarRequestModel;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CarHutCarFallbackService {

    private final CarHutCar car;
    private final List<MultipartFile> images;
    private final String bearerToken;
    private final CarHutApiSRCaller carHutApiSRCaller = new CarHutApiSRCaller();
    private final UserServiceCaller userServiceCaller = new UserServiceCaller();

    public CarHutCarFallbackService(CarHutCar car, List<MultipartFile> images, String bearerToken) {
        this.car = car;
        this.images = images;
        this.bearerToken = bearerToken;
    }

    public CompletableFuture<ServiceStatusEntity> invokeFallback(FallbackStage fallbackStage)
            throws URISyntaxException {
        CompletableFuture<ServiceStatusEntity> cf = new CompletableFuture<>();
        if (fallbackStage == null) {
            cf.complete(ServiceStatusEntity.ERROR);
            return cf;
        }

        if (fallbackStage == FallbackStage.STAGE_1) {
            cf.complete(invokeStage1Fallback());
            return cf;
        }

        if (fallbackStage == FallbackStage.STAGE_2) {
            CompletableFuture<ServiceStatusEntity> stage2Status = invokeStage2Fallback();

            stage2Status.whenComplete((res, ex) -> {
               if (ex != null || !res.equals(ServiceStatusEntity.SUCCESS)) {
                   cf.complete(ServiceStatusEntity.ERROR);
               }
            });
        }

        if (fallbackStage == FallbackStage.STAGE_3) {
            CompletableFuture<ServiceStatusEntity> stage3Status = invokeStage3Fallback();

            stage3Status.whenComplete((res, ex) -> {
                if (ex != null || !res.equals(ServiceStatusEntity.SUCCESS)) {
                    cf.complete(ServiceStatusEntity.ERROR);
                }
            });
        }

        return cf;
    }

    /**
     * Currently returns success due to that stage not having previous stages that should be undone.
     * @return status of stage 1 fallback execution
     */
    private ServiceStatusEntity invokeStage1Fallback() {
        return ServiceStatusEntity.SUCCESS;
    }

    /**
     * Failed to update sellers offer count, removes saved Car on stage 1.
     * @return status of stage 2 fallback execution
     */
    private CompletableFuture<ServiceStatusEntity> invokeStage2Fallback() throws URISyntaxException {
        CompletableFuture<ServiceStatusEntity> status = new CompletableFuture<>();
        CompletableFuture<HttpResponse<String>> serviceStatus =
                carHutApiSRCaller.removeOffer(new RemoveCarRequestModel(car.getId(), car.getSellerId()));

        serviceStatus.whenComplete((res, ex) -> {
           if (ex != null) {
               status.complete(ServiceStatusEntity.ERROR);
           } else if (res.statusCode() < 200 || res.statusCode() > 299) {
               status.complete(ServiceStatusEntity.ERROR);
           } else {
               status.complete(ServiceStatusEntity.SUCCESS);
           }
        });

        return status;
    }

    /**
     * Failed to save images, removes saved Car on stage 1 and decrements users offer count.
     * @return status of stage 3 fallback execution
     */
    private CompletableFuture<ServiceStatusEntity> invokeStage3Fallback() throws URISyntaxException {
        CompletableFuture<ServiceStatusEntity> status = new CompletableFuture<>();
        CompletableFuture<HttpResponse<String>> removeCarStatus =
                carHutApiSRCaller.removeOffer(new RemoveCarRequestModel(car.getId(), car.getSellerId()));

        removeCarStatus.whenComplete((res, ex) -> {
            if (ex != null) {
                status.complete(ServiceStatusEntity.ERROR);
            } else if (res.statusCode() < 200 || res.statusCode() > 299) {
                status.complete(ServiceStatusEntity.ERROR);
            }
        });

        CompletableFuture<HttpResponse<String>> decrementOfferCountStatus =
                removeCarStatus.thenCompose(resp -> {
                    try {
                        return userServiceCaller
                                .decrementOfferCount(car.getSellerId(), bearerToken);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });

        decrementOfferCountStatus.whenComplete((res, ex) -> {
            if (ex != null) {
                status.complete(ServiceStatusEntity.ERROR);
            } else if (res.statusCode() < 200 || res.statusCode() > 299) {
                status.complete(ServiceStatusEntity.ERROR);
            } else {
                status.complete(ServiceStatusEntity.SUCCESS);
            }
        });

        return status;
    }
}
