package org.apache.nifi.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 *
 */
@Configuration
@Import({ NiFiWebApiSecurityConfiguration.class})
@ImportResource( {"classpath:nifi-context.xml",
    "classpath:nifi-administration-context.xml",
    "classpath:nifi-cluster-manager-context.xml",
    "classpath:nifi-cluster-protocol-context.xml",
    "classpath:nifi-web-security-context.xml",
    "classpath:nifi-web-api-context.xml"} )
public class NiFiWebApiConfiguration {

    public NiFiWebApiConfiguration() {
        super();
    }
    
}
