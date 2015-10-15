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

import org.apache.nifi.web.security.form.FormAuthenticationFilter;
import org.apache.nifi.web.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * NiFi Web Ui Security Config
 */
@Configuration
@EnableWebSecurity
public class NiFiWebUiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public NiFiWebUiSecurityConfiguration() {
        super(true); // disable defaults
    }

    private JwtService jwtService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .addFilterBefore(buildFormLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private FormAuthenticationFilter buildFormLoginFilter() throws Exception {
        final FormAuthenticationFilter loginFilter = new FormAuthenticationFilter("/token");
        loginFilter.setJwtService(jwtService);
        return loginFilter;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                    .withUser("gilman").password("password").roles("USER");
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

}
