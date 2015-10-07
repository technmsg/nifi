<%--
 Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <title>NiFi Login</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="shortcut icon" href="images/nifi16.ico"/>
        <link rel="stylesheet" href="css/reset.css" type="text/css" />
        <script type="text/javascript" src="js/jquery/jquery-2.1.1.min.js"></script>
        <script type="text/javascript" src="js/jquery/jquery.form.min.js"></script>
        <script type="text/javascript" src="js/nf/nf-namespace.js?${project.version}"></script>
        <script type="text/javascript">
            /* global nf */
            
            $(document).ready(function() {
                nf.LogIn.init();
            });
            
            nf.LogIn = (function () {
                var initializePage = function () {
                    return $.Deferred(function(deferred) {
                        
                    });
                };
                
                return {
                    /**
                     * Initializes the login page.
                     */
                    init: function () {
                        initializePage().done(function () {
                        });
                    }
                };
            }());
        </script>
    </head>
    <body>
        <form name="loginForm" action="token" method="post">
            <legend>Please Login</legend>
            <label for="username">Username</label>
            <input type="text" id="username" name="username" value="${username}"/>
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/>
            <div class="form-actions">
                <button type="submit" class="btn">Log in</button>
            </div>
        </form>
    </body>
</html>
