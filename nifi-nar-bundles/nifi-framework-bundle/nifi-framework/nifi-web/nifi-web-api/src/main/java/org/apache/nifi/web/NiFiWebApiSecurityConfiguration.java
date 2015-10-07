/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.web;

import org.apache.nifi.admin.service.UserService;
import org.apache.nifi.util.NiFiProperties;
import org.apache.nifi.web.security.NiFiAuthenticationProvider;
import org.apache.nifi.web.security.anonymous.NiFiAnonymousUserFilter;
import org.apache.nifi.web.security.NiFiAuthenticationEntryPoint;
import org.apache.nifi.web.security.node.NodeAuthorizedUserFilter;
import org.apache.nifi.web.security.x509.SubjectDnX509PrincipalExtractor;
import org.apache.nifi.web.security.x509.X509AuthenticationFilter;
import org.apache.nifi.web.security.x509.X509AuthenticationProvider;
import org.apache.nifi.web.security.x509.X509CertificateExtractor;
import org.apache.nifi.web.security.x509.ocsp.OcspCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

/**
 * NiFi Web Api Spring security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class NiFiWebApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private NiFiProperties properties;
    private UserService userService;
    private AuthenticationUserDetailsService userDetailsService;

    public NiFiWebApiSecurityConfiguration() {
        super(true); // disable defaults
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .rememberMe().disable()
                .exceptionHandling()
                    .authenticationEntryPoint(new NiFiAuthenticationEntryPoint())
                    .and()
                .authorizeRequests()
                    .anyRequest().fullyAuthenticated()
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // cluster - authorized user
        final NodeAuthorizedUserFilter authorizedUserFilter = new NodeAuthorizedUserFilter(properties);
        http.addFilterBefore(authorizedUserFilter, AnonymousAuthenticationFilter.class);

        // x509
        http.addFilterBefore(buildX509Filter(), AnonymousAuthenticationFilter.class);

        // anonymous
        final NiFiAnonymousUserFilter anonymousFilter = new NiFiAnonymousUserFilter();
        anonymousFilter.setProperties(properties);
        anonymousFilter.setUserService(userService);
        http.anonymous().authenticationFilter(anonymousFilter);
    }

    @Bean 
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // override xxxBean method so the authentication manager is available in app context (necessary for the method level security)
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // x509
        final AuthenticationProvider x509AuthenticationProvider = new NiFiAuthenticationProvider(new X509AuthenticationProvider(), userDetailsService);
        
        auth
                .authenticationProvider(x509AuthenticationProvider);
    }
    
    private X509AuthenticationFilter buildX509Filter() throws Exception {
        final X509AuthenticationFilter x509Filter = new X509AuthenticationFilter();
        x509Filter.setPrincipalExtractor(new SubjectDnX509PrincipalExtractor());
        x509Filter.setCertificateExtractor(new X509CertificateExtractor());
        x509Filter.setCertificateValidator(new OcspCertificateValidator(properties));
        x509Filter.setAuthenticationManager(authenticationManager());
        return x509Filter;
    }
    
    @Autowired
    public void setUserDetailsService(AuthenticationUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    public void setProperties(NiFiProperties properties) {
        this.properties = properties;
    }

}
