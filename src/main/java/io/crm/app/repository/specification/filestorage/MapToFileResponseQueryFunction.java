package io.crm.app.repository.specification.filestorage;


import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.filestorage.FileStorageInfoEntity;
import io.crm.app.model.customer.CustomerResponse;
import io.crm.app.model.filestorage.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MapToFileResponseQueryFunction implements Function<FileStorageInfoEntity, FileUploadResponse> {

    @Override
    public FileUploadResponse apply(FileStorageInfoEntity fileStorageInfoEntity) {
        var response = entityToResponse(fileStorageInfoEntity);
        return response;
    }

    private FileUploadResponse entityToResponse(FileStorageInfoEntity fileStorageInfoEntity) {

        var builder =  FileUploadResponse.builder()
                .id(fileStorageInfoEntity.getId())
                .fileName(fileStorageInfoEntity.getFileName())
                .fileDescription(fileStorageInfoEntity.getFileDescription())
                .fileUrl(fileStorageInfoEntity.getFilePath());
        return builder.build();
    }
}
