package uk.gov.hmcts.reform.role_assignment.performance.scenarios.utils;

import java.util.Properties;
import com.typesafe.config.ConfigFactory;

public class Env {
    private static String file = "CV-CMC-GOR-ENG-0004-UI-Test.docx";
    static Properties defaults = new Properties();
    static {
        //defaults.setProperty("IDAM_API_BASE_URI", "https://idam-api.aat.platform.hmcts.net");
        defaults.setProperty("IDAM_API_BASE_URI", "https://idam-api.perftest.platform.hmcts.net");
        //defaults.setProperty("OAUTH_CLIENT", "am_role_assignment");
        defaults.setProperty("OAUTH_CLIENT", "paybubble");
        //defaults.setProperty("IDAM_OAUTH_SECRET", ConfigFactory.load().getString("IDAM_SECRET_AAT"));
        defaults.setProperty("IDAM_OAUTH_SECRET", ConfigFactory.load().getString("IDAM_SECRET_PERFTEST"));
        defaults.setProperty("IDAM_SCOPE", "openid%20profile%20roles%20authorities");
        //defaults.setProperty("IDAM_USERNAME", "befta.caseworker.2.solicitor.2@gmail.com");
        defaults.setProperty("IDAM_USERNAME", "ccdloadtest1@gmail.com");
        //defaults.setProperty("IDAM_PASSWORD", "PesZvqrb78");
        defaults.setProperty("IDAM_PASSWORD", "Password12");
        //defaults.setProperty("S2S_BASE_URI", "http://rpe-service-auth-provider-aat.service.core-compute-aat.internal/testing-support");
        defaults.setProperty("S2S_BASE_URI", "http://rpe-service-auth-provider-perftest.service.core-compute-perftest.internal/testing-support");
        defaults.setProperty("FUNCTIONAL_TEST_CLIENT_S2S_TOKEN", ConfigFactory.load().getString("S2S_SECRET"));
        defaults.setProperty("S2S_SERVICE_NAME", "am_role_assignment_service");
    }

    public static String require(String name) {
        return System.getenv(name) == null ? defaults.getProperty(name) : System.getenv(name);
    }


    public static String getIdamUrl() {
        return require("IDAM_API_BASE_URI");
    }

    public static String getOAuthClient() {
        return require("OAUTH_CLIENT");
    }

    public static String getOAuthSecret() {
        return require("IDAM_OAUTH_SECRET");
    }

    public static String getScope() { return require("IDAM_SCOPE");}

    public static String getUsername() { return require("IDAM_USERNAME");}

    public static String getPassword() { return require("IDAM_PASSWORD");}


    public static String getS2sUrl() {
        return require("S2S_BASE_URI");
    }

    public static String getS2sSecret() {
        return require("FUNCTIONAL_TEST_CLIENT_S2S_TOKEN");
    }

    public static String getS2sMicroservice() {
        System.out.println("getS2sMicroservice");
        return require("S2S_SERVICE_NAME");
    }

}
