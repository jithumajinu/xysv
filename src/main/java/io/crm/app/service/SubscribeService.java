package io.crm.app.service;

import io.crm.app.core.model.ModelPage;
import io.crm.app.exception.CrmException;
import io.crm.app.model.customer.*;

import java.util.List;

public interface SubscribeService {
    String unsubscribe(String hashCode);
}
