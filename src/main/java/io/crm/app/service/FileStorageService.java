package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.filestorage.*;


public interface FileStorageService {
    public FileUploadResponse uploadFile(FileUploadRequest file);
    public FileDownloadResponse downloadFile(Integer fileId);
    public String deleteFile(Integer fileId);
    public String getFileUrl(Integer fileId);

    public FileUploadResponse updateFile(FileUploadChangeRequest file);

    public FileUploadResponse getFileById(Integer fileId) throws CrmException;

    ModelPage<FileUploadResponse> findFile(FindFilePageRequest request);

    void deleteSelectedFile(DeleteSelectedFileRequest request);

}
