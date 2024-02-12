package io.crm.app.service.impl;

import io.crm.app.repository.CustomerRepository;

import io.crm.app.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.crm.app.utils.EncryptionAES;


@Service
@RequiredArgsConstructor
@Transactional
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public String unsubscribe(String request) {
        try {
            String encryptedId = EncryptionAES.decryptId(request);
            return encryptedId;
        } catch (Exception e) {
            e.printStackTrace();  // Handle exceptions appropriately
            return "Error";
        }
    }

}
