package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.customer.CustomerResponse;
import io.crm.app.model.customer.FindCustomerPageRequest;
import io.crm.app.model.filestorage.*;
import io.crm.app.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileStorageController extends AbstractCoreUtilController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileUploadResponse> uploadFile(@Validated @ModelAttribute FileUploadRequest request,
                                                      BindingResult bindingResult,
                                                      HttpServletResponse httpServletResponse) {
        // Process the uploaded file here
        var responseBuilder = ApiResponse.<FileUploadResponse>builder()
                .companyPublish(true);

        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {
            if (!request.getFile().isEmpty()) {
                FileUploadResponse response = fileStorageService.uploadFile(request);
                responseBuilder.data(response);
            }
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();

    }


    //    @PostMapping(value="/",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ApiResponse<FileUploadResponse> createFile(
//            @Validated @ModelAttribute FileUploadRequest request,
//            BindingResult bindingResult,
//            HttpServletResponse httpServletResponse) {
//
//        var responseBuilder = ApiResponse.<FileUploadResponse>builder()
//                .companyPublish(true);
//
//        if (bindingResult.hasErrors()) {
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            return responseBuilder
//                    .error(ApiResponse.ApiError.builder()
//                            .code(ApiErrorCode.INPUT_ERROR)
//                            .errors(formatInputErrors(bindingResult))
//                            .build())
//                    .build();
//        }
//
//        try {
//            System.out.println("--------fileUpload-- impl-------:" + request.toString());
//            FileUploadResponse response = fileStorageService.uploadFile(request);
//            responseBuilder.data(response);
//        } catch (IllegalArgumentException e) {
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            return responseBuilder.build();
//        }
//
//        return responseBuilder.build();
//    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(
            @PathVariable(value = "fileId", required = true) Integer fileId,
            HttpServletResponse httpServletResponse) {
        FileDownloadResponse response = fileStorageService.downloadFile(fileId);
        ByteArrayResource resource = new ByteArrayResource(response.getFileData());
        return ResponseEntity
                .ok()
                .contentLength(response.getFileData().length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + response.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/link/{fileId}")
    public ApiResponse<String> getFileLink(@PathVariable(value = "fileId", required = true) Integer fileId,
                                           HttpServletResponse httpServletResponse) {
        var responseBuilder = ApiResponse.<String>builder()
                .companyPublish(true);
        try {
            System.out.println("--------file link-- impl-------:" + fileId.toString());
            String urlPath = fileStorageService.getFileUrl(fileId);
            responseBuilder.data(urlPath);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }

    @PutMapping(value = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<FileUploadResponse> updateFile(
            @Validated @ModelAttribute FileUploadChangeRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<FileUploadResponse>builder()
                .companyPublish(true);

        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {
            System.out.println("--------fileUpload update-- impl-------:" + request.toString());
            FileUploadResponse response = fileStorageService.updateFile(request);
            responseBuilder.data(response);
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }


    @GetMapping({"/{fileId}"})
    public ApiResponse<FileUploadResponse> getFile(
            @PathVariable(value = "fileId", required = true) Integer fileId,
            HttpServletResponse httpServletResponse) {

        var responseBuilder = ApiResponse.<FileUploadResponse>builder().companyPublish(true);

        try {
            System.out.println("--------get file- CTRL-------:" + fileId.toString());
            FileUploadResponse fileUploadResponse = fileStorageService.getFileById(fileId);
            responseBuilder.data(fileUploadResponse);
        } catch (CrmException ex) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.FIND_FILE_ID_ERROR)
                            .build())
                    .build();
        } catch (IllegalArgumentException e) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }
        return responseBuilder.build();
    }


    @PostMapping({"delete-selected"})
    public ApiResponse<Void> deleteSelectedFiles(
            @Validated @RequestBody DeleteSelectedFileRequest request,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse) {


        var responseBuilder = ApiResponse.<Void>builder()
                .companyPublish(true);

        if (bindingResult.hasErrors()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder
                    .error(ApiResponse.ApiError.builder()
                            .code(ApiErrorCode.INPUT_ERROR)
                            .errors(formatInputErrors(bindingResult))
                            .build())
                    .build();
        }

        try {

            System.out.println("--------Delete File-------:" + request.toString());
            fileStorageService.deleteSelectedFile(request);
        } catch (IllegalArgumentException e) {
//            log.info(e.getMessage());
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return responseBuilder.build();
        }

        return responseBuilder.build();
    }

    @GetMapping({"", "/"})
    public ApiResponse<ModelPage<FileUploadResponse>> getFilePaged(
            FindFilePageRequest request,
            HttpServletResponse httpServletResponse) {

        System.out.println("FindFilePageRequest: keyword : " + request.getKeyword());
        System.out.println("FindFilePageRequest: page: " + request.getPage());
        System.out.println("FindFilePageRequest: pagingSize: " + request.getPagingSize());
        System.out.println("FindFilePageRequest: sortItem: " + request.getSortItem());
        var responseBuilder = ApiResponse.<ModelPage<FileUploadResponse>>builder()
                .companyPublish(true);
        try {
            var fileList = fileStorageService.findFile(request);
            System.out.println("Msg: beforBind-fileList" + fileList);
            responseBuilder.data(fileList);
        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return responseBuilder.error(ApiResponse.ApiError.builder()
                    .code(ApiErrorCode.FIND_FILE_RECIEVE_PAGE_ERROR)
                    .build()).build();
        }
        return responseBuilder.build();
    }


//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
//        return new ResponseEntity<>(fileStorageService.uploadFile(file), HttpStatus.OK);
//    }
//
//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
//        byte[] data = fileStorageService.downloadFile(fileName);
//        ByteArrayResource resource = new ByteArrayResource(data);
//        return ResponseEntity
//                .ok()
//                .contentLength(data.length)
//                .header("Content-type", "application/octet-stream")
//                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
//                .body(resource);
//    }
//
//    @DeleteMapping("/delete/{fileName}")
//    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
//        return new ResponseEntity<>(fileStorageService.deleteFile(fileName), HttpStatus.OK);
//    }
//    @GetMapping("/link/{fileName}")
//    public ResponseEntity<String> getFile(@PathVariable String fileName) {
//        return new ResponseEntity<>(fileStorageService.getFile(fileName), HttpStatus.OK);
//    }


//@PostMapping("/geturl")
//public ResponseEntity<String> generateUrl(@RequestParam String extension) {
//        return ResponseEntity.ok(fileService.generatePreSignedUrl(
//        UUID.randomUUID()+"."+extension, HttpMethod.PUT));
//        }
//
//@GetMapping("/getpdfurl")
//public ResponseEntity<String> getUrl(@RequestParam String filename) {
//        return ResponseEntity.ok(fileService.generatePreSignedUrl(
//        filename, HttpMethod.GET));
//        }

//@GetMapping("download/{filename}")
//public ResponseEntity<byte[]> download(@PathVariable("filename") String filename){
//        HttpHeaders headers=new HttpHeaders();
//        headers.add("Content-type", MediaType.ALL_VALUE);
//        headers.add("Content-Disposition", "attachment; filename="+filename);
//        byte[] bytes = s3Service.downloadFile(filename);
//        return  ResponseEntity.status(HTTP_OK).headers(headers).body(bytes);
//        }


}