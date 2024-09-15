package com.carhut.proxy.processors;

import com.carhut.proxy.models.dtos.SerializedResponseDto;
import com.carhut.proxy.threading.dispatchers.DispatcherManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RequestProcessor {

    public CompletableFuture<SerializedResponseDto> processRequestAsync(
            HttpServletRequest request, String payload, String bearerToken, String contentType
    ) {
        return DispatcherManager.dispatchAndStartWorker();
    }
}
