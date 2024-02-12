package io.crm.app.controller;
import io.crm.app.core.controller.AbstractCoreUtilController;
import io.crm.app.core.model.ModelPage;
import io.crm.app.model.ApiResponse;
import io.crm.app.model.customer.*;
import io.crm.app.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController extends AbstractCoreUtilController {


    @Autowired
    private SubscribeService subscribeService;

    /**
     * GET request to unsubscribe
     */
    @GetMapping({"", "/"})
    public ResponseEntity<String> unsubscribe(@RequestParam String hash) {

        System.out.println("FindCustomerPageRequest: keyword : " + hash);
        var responseBuilder = ApiResponse.<ModelPage<CustomerResponse>>builder()
                .companyPublish(true);
        try {
            var result = subscribeService.unsubscribe(hash);

            System.out.println("result iD: " + result);

        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
        }
        return ResponseEntity.ok("Unsubscribed successfully");
    }

    /**
     * GET request to unsubscribe
     */
    @GetMapping({"/{hashCode}"})
    public ResponseEntity<String> unsubscribeHas(@PathVariable(value = "hashCode", required = true) String hashCode) {
        System.out.println("FindCustomerPageRequest: keyword : " + hashCode);
        var responseBuilder = ApiResponse.<ModelPage<CustomerResponse>>builder()
                .companyPublish(true);
        try {
            var result = subscribeService.unsubscribe(hashCode);
            System.out.println("result: " + result);

        } catch (Throwable ex) {
            System.out.println("Msg: all-rsp" + ex);
        }
        return ResponseEntity.ok("Unsubscribed successfully");
    }

}
