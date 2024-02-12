//package io.crm.app.utils;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.*;
//import com.amazonaws.util.IOUtils;
//import io.crm.app.model.filestorage.AwsUploadResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import com.amazonaws.HttpMethod;
//
//import java.io.File;
//import java.math.BigInteger;
//import java.util.List;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//public class AwsFileStorage {
//    @Value("${application.bucket.name}")
//    private String bucketName;
//
//    @Autowired
//    private AmazonS3 s3Client;
//
//
//    public AwsUploadResponse uploadFile(BigInteger fileId, MultipartFile file) {
//        AwsUploadResponse response= new AwsUploadResponse();
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName = fileId.toString()+"_"+System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
//        fileObj.delete();
//        response.setFileName(fileName);
//        response.setFileUrl(getFileUrl(fileName));
//        return response;
//    }
//
//
//    public byte[] downloadFile(String fileName) {
//        S3Object s3Object = s3Client.getObject(bucketName, fileName);
//        S3ObjectInputStream inputStream = s3Object.getObjectContent();
//        try {
//            byte[] content = IOUtils.toByteArray(inputStream);
//            return content;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    public String deleteFile(String fileName) {
//        s3Client.deleteObject(bucketName, fileName);
//        return fileName + " removed ...";
//    }
//
//    //get an object
//    public S3Object getObject(String bucketName, String objectKey) {
//        return s3Client.getObject(bucketName, objectKey);
//    }
//
//    public String getFileUrl(String filename){
//        //S3Object s3object = getObject(bucketName, filename);
//        //S3ObjectInputStream inputStream = s3object.getObjectContent();
//        //ileUtils.copyInputStreamToFile(inputStream, new File("/Users/user/Desktop/hello.txt"));
//        return s3Client.getUrl(bucketName, filename).toString();
//    }
//
//    private File convertMultiPartFileToFile(MultipartFile file) {
//        File convertedFile = new File(file.getOriginalFilename());
//        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
//            fos.write(file.getBytes());
//        } catch (IOException e) {
//            log.error("Error converting multipartFile to file", e);
//        }
//        return convertedFile;
//    }
//
////    //copying an object
////    public CopyObjectResult copyObject(
////            String sourceBucketName,
////            String sourceKey,
////            String destinationBucketName,
////            String destinationKey
////    ) {
////        return s3Client.copyObject(
////                sourceBucketName,
////                sourceKey,
////                destinationBucketName,
////                destinationKey
////        );
////    }
//////
////    //deleting an object
////    public void deleteObject(String bucketName, String objectKey) {
////        s3Client.deleteObject(bucketName, objectKey);
////    }
////
////    //deleting multiple Objects
////    public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
////        return s3Client.deleteObjects(delObjReq);
////    }
////
////
////    public String generatePreSignedUrl(String filePath, HttpMethod http) {
////        // setting expiry for the url
////        Calendar cal = Calendar.getInstance();
////        cal.setTime(new Date());
////        cal.add(Calendar.MINUTE,2);
////        return s3Client.generatePresignedUrl(bucketName,filePath,cal.getTime(),http).toString();
////    }
////
////    public List<String> listAllFiles() {
////
////        ListObjectsV2Result listObjectsV2Result = s3Client.listObjectsV2(bucketName);
////        return  listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
////
////    }
////
////    //is bucket exist?
////    public boolean doesBucketExist(String bucketName) {
////        return s3Client.doesBucketExist(bucketName);
////    }
////
////    //create a bucket
////    public Bucket createBucket(String bucketName) {
////        return s3Client.createBucket(bucketName);
////    }
////
////    //list all buckets
////    public List<Bucket> listBuckets() {
////        return s3Client.listBuckets();
////    }
////
////    //delete a bucket
////    public void deleteBucket(String bucketName) {
////        s3Client.deleteBucket(bucketName);
////    }
//
//
////    public String saveFile(MultipartFile file) {
////        String originalFilename = file.getOriginalFilename();
////        int count = 0;
////        int maxTries = 3;
////        while(true) {
////            try {
////                File file1 = convertMultiPartToFile(file);
////                PutObjectResult putObjectResult = s3.putObject(bucketName, originalFilename, file1);
////                return putObjectResult.getContentMd5();
////            } catch (IOException e) {
////                if (++count == maxTries) throw new RuntimeException(e);
////            }
////        }
////
////    }
////
////    @Override
////    public byte[] downloadFile(String filename) {
////        S3Object object = s3.getObject(bucketName, filename);
////        S3ObjectInputStream objectContent = object.getObjectContent();
////        try {
////            return IOUtils.toByteArray(objectContent);
////        } catch (IOException e) {
////            throw  new RuntimeException(e);
////        }
////
////
////    }
//
//}
