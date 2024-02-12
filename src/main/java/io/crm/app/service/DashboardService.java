package io.crm.app.service;

import io.crm.app.model.dashboard.DashboardCountRequest;
import io.crm.app.model.dashboard.DashboardResponse;

import java.util.List;

public interface DashboardService {

    DashboardResponse getCount(DashboardCountRequest request);
//    DashboardResponse getCustomerCount(DashboardCountRequest request);
//
//    DashboardResponse getGroupCount(DashboardCountRequest request);
//
//    DashboardResponse getEmailTemplateCount(DashboardCountRequest request);
//
//    DashboardResponse getSmsTemplateCount(DashboardCountRequest request);
//
//    DashboardResponse getEmailSentCount(DashboardCountRequest request);
//
//    DashboardResponse getSmsSentCount(DashboardCountRequest request);

}
