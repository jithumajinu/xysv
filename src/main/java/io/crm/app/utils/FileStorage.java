package io.crm.app.utils;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import io.crm.app.model.filestorage.AwsUploadResponse;
import io.crm.app.model.filestorage.FileUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileStorage {

    @Value("${upload.path}")
    private String uploadPath;

    public FileUploadResponse uploadFile(String dirPath,MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileUploadResponse response= new FileUploadResponse();
        if (file.isEmpty()) {
            return null;
        }
        try{
            File rootFolder = new File(uploadPath+File.separatorChar +dirPath);
            if (!rootFolder.exists()) {
               // rootFolder.mkdirs();

                Files.createDirectories(Paths.get(rootFolder.getAbsolutePath()));
            }
            String destPath=rootFolder.getAbsolutePath()+ File.separatorChar + fileName;

            System.out.println("test"+rootFolder.getAbsolutePath());

            //InputStream inputStream = file.getInputStream();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(destPath);
            FileOutputStream outputStream = new FileOutputStream(path.toString());
            outputStream.write(bytes);
            System.out.println("File uploaded successfully");
            response.setFileUrl(dirPath + File.separatorChar + fileName);
            response.setFileName(file.getOriginalFilename());
            return response;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage().toString());

        }
        return null;

    }

//    public FileUploadResponse uploadFile(String dirPath,MultipartFile file) {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        FileUploadResponse response= new FileUploadResponse();
//
//        if (file.isEmpty()) {
//            return null;
//        }
//
//        File rootFolder = new File(uploadPath+File.separator +dirPath);
//        if (!rootFolder.exists()) {
//            rootFolder.mkdirs();
//        }
//        String destPath=rootFolder.getAbsolutePath()+ File.separator + fileName;
//
//        System.out.println("test"+rootFolder.getAbsolutePath());
//
//        try  {
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(destPath);
//            Files.write(path, bytes);
//            response.setFileUrl(dirPath + File.separator + fileName);
//            response.setFileName(file.getOriginalFilename());
//           // InputStream inputStream = file.getInputStream();
//           // Files.copy(inputStream, rootFolder.getAbsolutePath().toString(), StandardCopyOption.REPLACE_EXISTING);
//        }
//        //try {
////            File destFile = new File(rootFolder , fileName);
////            file.transferTo(destFile);
//            //response.setFileName(failed);
//       //     response.setFileUrl(getFileUrl(fileName));
//       // }
//        catch(Exception ex){
//            System.out.println(ex.getMessage().toString());
//            return null;
//        }
//        return response;
//    }

    public byte[] downloadFile(String filePath) {
        try {
            Path path = Paths.get(uploadPath+File.separatorChar +filePath);
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return resource.getByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
