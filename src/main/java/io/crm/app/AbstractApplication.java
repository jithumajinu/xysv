package io.crm.app;

import java.text.NumberFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class AbstractApplication implements WebMvcConfigurer {

    @Autowired
    private Validator validator;

    @Override
    public Validator getValidator() {
        return validator;
    }


    protected static void systemSettingLog() {
        Runtime runtime = Runtime.getRuntime();

        final NumberFormat format = NumberFormat.getInstance();

        final long maxMemory = runtime.maxMemory();
        final long allocatedMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        final long mb = 1024 * 1024L;
        final String mega = " MB";
        System.out.println("========================== Memory Info ==========================");
        System.out.println("Free memory: " + format.format(freeMemory / mb) + mega);
        System.out.println("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
        System.out.println("Max memory: " + format.format(maxMemory / mb) + mega);
        System.out.println("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
        System.out.println("=================================================================\n");
    }
}
