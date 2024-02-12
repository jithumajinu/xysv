package io.crm.app.controller;

import io.crm.app.core.constant.ApiErrorCode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.customer.CreateCustomerRequest;
import io.crm.app.model.customer.CustomerGroupResponse;
import io.crm.app.model.filestorage.FileUploadRequest;
import io.crm.app.model.filestorage.FileUploadResponse;
import io.crm.app.model.group.*;
import io.crm.app.service.CustomerService;
import io.crm.app.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpHeaders;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController extends AbstractCoreUtilController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<FileUploadResponse> uploadFile(@Validated @ModelAttribute FileUploadRequest request,
                                                      BindingResult bindingResult,
                                                      HttpServletResponse httpServletResponse) {
        // Process the uploaded file here
        var responseBuilder = ApiResponse.<FileUploadResponse>builder()
                .companyPublish(true);



        return responseBuilder.build();

    }

    @PostMapping(value = "/customer-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadCustomer(@RequestParam("file") MultipartFile file) throws Exception {

        try {
            // Create a CSV reader from the input stream of the uploaded file
            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));

            // Read all rows from the CSV file
            List<String[]> rows = reader.readAll();

            // Process the rows as needed
            String[] headers = rows.get(0);
            rows = rows.subList(1, rows.size());
            for (String[] row : rows) {
                CreateCustomerRequest request = new CreateCustomerRequest();
                request.setFirstName(row[0]);
                request.setLastName(row[1]);
                request.setCompany(row[2]);
                request.setAddress(row[3]);
                request.setCity(row[4]);
                request.setState(row[5]);
                request.setCountry(row[6]);
                request.setPostalCode(row[7]);
                request.setPhoneCode(row[8]);
                request.setPhone(row[9]);
                request.setEmail(row[10]);
                request.setAssignedGroups(new ArrayList<>());
                customerService.createCustomer(request);
            }
            // Close the CSV reader
            reader.close();
        } catch (IOException | CsvException e) {
            // Handle exceptions (e.g., file not found, CSV parsing error)
            e.printStackTrace(); // Handle this appropriately in your application
        }
    }

    @GetMapping("/customer")
    public void exportCustomer(HttpServletResponse response) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());

        // set file name and content type
        String originalFileName = "Customer-List.csv";

        String filename = addTimestampToFileName(originalFileName, timestamp);

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        // create a csv writer with CSVWriterBuilder
        ICSVWriter csvWriter = new CSVWriterBuilder(response.getWriter())
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        String[] customHeaders = {
                "Id" ,
                "FirstName",
                "LastName" ,
                "Company" ,
                "Address" ,
                "City" ,
                "State" ,
                "Country" ,
                "PostalCode" ,
                "PhoneCode" ,
                "Phone" ,
                "Email"
        };
        csvWriter.writeNext(customHeaders);

        // write custom values using a loop
        List<CustomerGroupResponse> responseList = customerService.findAllCustomers();
        for (CustomerGroupResponse customer : responseList) {
            String[] customValues = {
                    customer.getId().toString(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getCompany(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getState(),
                    customer.getCountry(),
                    customer.getPostalCode(),
                    customer.getPhoneCode(),
                    customer.getPhone(),
                    customer.getEmail()
            };
            csvWriter.writeNext(customValues);
        }
        csvWriter.close();
    }

    @GetMapping("/group")
    public void exportGroup(HttpServletResponse response) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());

        // set file name and content type
        String originalFileName = "Group-List.csv";

        String filename = addTimestampToFileName(originalFileName, timestamp);

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        // create a csv writer with CSVWriterBuilder
        ICSVWriter csvWriter = new CSVWriterBuilder(response.getWriter())
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        String[] customHeaders = {
                "Id" ,
                "GroupName"
        };
        csvWriter.writeNext(customHeaders);

        // write custom values using a loop
        List<GroupResponse> groupList = groupService.findAllGroups();
        for (GroupResponse group : groupList) {
            String[] customValues = {
                    group.getId().toString(),
                    group.getGroupName()
            };
            csvWriter.writeNext(customValues);
        }
        csvWriter.close();
    }

    @GetMapping("/email")
    public void exportEmailReport(HttpServletResponse response) throws Exception {

        // set file name and content type

        String filename = "Group-List.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        // create a csv writer with CSVWriterBuilder
        ICSVWriter csvWriter = new CSVWriterBuilder(response.getWriter())
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        String[] customHeaders = {
                "Id" ,
                "GroupName"
        };
        csvWriter.writeNext(customHeaders);

        // write custom values using a loop
        List<GroupResponse> groupList = groupService.findAllGroups();
        for (GroupResponse group : groupList) {
            String[] customValues = {
                    group.getId().toString(),
                    group.getGroupName()
            };
            csvWriter.writeNext(customValues);
        }
        csvWriter.close();
    }

    @GetMapping("/sms")
    public void exportSmsReport(HttpServletResponse response) throws Exception {

        // set file name and content type
        String filename = "Group-List.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        // create a csv writer with CSVWriterBuilder
        ICSVWriter csvWriter = new CSVWriterBuilder(response.getWriter())
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        String[] customHeaders = {
                "Id" ,
                "GroupName"
        };
        csvWriter.writeNext(customHeaders);

        // write custom values using a loop
        List<GroupResponse> groupList = groupService.findAllGroups();
        for (GroupResponse group : groupList) {
            String[] customValues = {
                    group.getId().toString(),
                    group.getGroupName()
            };
            csvWriter.writeNext(customValues);
        }
        csvWriter.close();
    }

    private static String addTimestampToFileName(String fileName, String timestamp) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot != -1) {
            // If the file has an extension, insert the timestamp before the extension
            return fileName.substring(0, lastDot) + "_" + timestamp + fileName.substring(lastDot);
        } else {
            // If the file has no extension, simply append the timestamp
            return fileName + "_" + timestamp;
        }
    }

}
