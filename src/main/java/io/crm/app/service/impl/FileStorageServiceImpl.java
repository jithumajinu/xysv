package io.crm.app.service.impl;

import com.google.common.collect.Maps;
import io.crm.app.core.model.ModelPage;
import io.crm.app.core.utils.PageUtil;
import io.crm.app.entity.filestorage.FileStorageInfoEntity;
import io.crm.app.model.filestorage.*;
import io.crm.app.repository.FileStorageRepository;
import io.crm.app.repository.specification.filestorage.MapToFileResponseQueryFunction;
import io.crm.app.service.FileStorageService;
import io.crm.app.utils.FileStorage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FileStorageServiceImpl implements FileStorageService {

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private FileStorageRepository fileStorageRepository;

    private static final Integer UNREGISTER_DISPLAY_ID = 0;

    private final MapToFileResponseQueryFunction mapToFileResponseQueryFunction;

    @Override
    public FileUploadResponse uploadFile(FileUploadRequest request) {
        System.out.println("--------file upload-- request-------:"+ request.toString());
        FileUploadResponse response=fileStorage.uploadFile("emailTemplates",request.getFile());
        if(response!=null)
        {
            response.setId(BigInteger.ZERO);
                    var fileStorageEntity = this.fileStorageRepository
                .saveAndFlush(FileStorageInfoEntity.builder()
                        .fileName(request.getFileName())
                        .fileDescription(request.getFileDescription())
                         .filePath(response.getFileUrl())
                        .build());
        var newFile = this.fileStorageRepository.findById(fileStorageEntity.getId()).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("file is not found. [displayId=%d]", fileStorageEntity.getId()),
                1));
            response.setId(newFile.getId());
            response.setFileName(newFile.getFileName());
            response.setFileUrl(newFile.getFilePath());
            response.setFileDescription(newFile.getFileDescription());
        }
        return response;
    }

    @Override
    public FileDownloadResponse downloadFile(Integer fileId) {
        FileDownloadResponse response=new FileDownloadResponse();
        var fileEntity = this.fileStorageRepository.findById(BigInteger.valueOf(fileId)).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("file is not found. [displayId=%d]", fileId),
                1));
        byte[] data = fileStorage.downloadFile(fileEntity.getFilePath());
        response.setFileData(data);
        response.setFileName(fileEntity.getFileName());
        return response;
    }

    @Override
    public String deleteFile(Integer fileId) {

        var fileEntity = this.fileStorageRepository.findById(BigInteger.valueOf(fileId)).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("file is not found. [displayId=%d]", fileId),
                1));
        this.fileStorageRepository.deleteByFileId(BigInteger.valueOf(fileId));
        String msg=" removed ...";
       // String msg=  fileStorage.deleteFile(fileEntity.getStoragefileName());

        return msg;
    }

    @Override
    public String getFileUrl(Integer fileId) {
        var fileEntity = this.fileStorageRepository.findById(BigInteger.valueOf(fileId)).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("file is not found. [displayId=%d]", fileId),
                1));
        return fileEntity.getFilePath();
    }

    @Override
    public FileUploadResponse updateFile(FileUploadChangeRequest request) {
            System.out.println("--------Update file- IMPL-------:" + request.toString());
            var fileEntity = this.findForUpdate(request.getId());
        FileUploadResponse response= new FileUploadResponse();

        if(request.isFileChangeStatus())
        {
            response=fileStorage.uploadFile("emailTemplates",request.getFile());
            fileEntity.setFileName(response.getFileName());
            fileEntity.setFilePath((response.getFileUrl()));
        }
        else{
            fileEntity.setFileName(request.getFileName());
        }
        response.setId(BigInteger.ZERO);

        fileEntity.setFileDescription(request.getFileDescription());

            var updatefileStorageEntity = this.fileStorageRepository
                    .saveAndFlush(fileEntity);
            response.setId(updatefileStorageEntity.getId());
            response.setFileName(updatefileStorageEntity.getFileName());
            response.setFileUrl(updatefileStorageEntity.getFilePath());
            response.setFileDescription(updatefileStorageEntity.getFileDescription());
        return response;
    }

    @Override
    public FileUploadResponse getFileById(Integer fileId) {
        var fileEntity = this.findForUpdate(fileId);
        return FileUploadResponse.builder()
                .id(fileEntity.getId())
                .fileName(fileEntity.getFileName())
                .fileDescription(fileEntity.getFileDescription())
                .fileUrl(fileEntity.getFilePath())
                 .build();
    }

    @Override
    public ModelPage<FileUploadResponse> findFile(FindFilePageRequest request) {
        var conditionBuilder = FilePageCondition.builder()
                .keyword(request.getKeyword())
                .sortItem(request.getSortItem());

        var pageable = PageUtil.toPageable(request.getPage(), request.getPagingSize().getCode(), Maps.newHashMap());

        var page = fileStorageRepository.findPageByCondition(conditionBuilder.build(), pageable);

        List<FileUploadResponse> fileList = new ArrayList<>();  // Lists.newArrayList();

        if (page.hasContent()) {
            fileList = page.getContent().stream()
                    .filter(Objects::nonNull)
                    .map(mapToFileResponseQueryFunction)  //  .map(dealingFunction)
                    .collect(Collectors.toList());
        }

        return ModelPage.<FileUploadResponse>builder()
                .content(fileList)  //  responseList
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public void deleteSelectedFile(DeleteSelectedFileRequest request) {
        if (CollectionUtils.isEmpty(request.getIds())) {
            return;
        }

        this.fileStorageRepository.deleteAllBySelectedId(request.getIds());
    }


    private FileStorageInfoEntity findForUpdate(Integer displayId) {
        if (Objects.isNull(displayId) || 0 == UNREGISTER_DISPLAY_ID.compareTo(displayId)) {
            throw new IllegalArgumentException(String.format(
                    "displayId is invalid. [displayId=%d]", displayId));
        }
        var optionalFileEntity = this.fileStorageRepository.findById(BigInteger.valueOf(displayId.intValue()));

        return optionalFileEntity.orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("File is not found. [displayId=%d]", displayId),
                1));
    }


}
