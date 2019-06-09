
package com.diageo.mras.webservices.smsaggregator;

/*
 * Copyright (c) 2005, Mobile 365, Inc.  4511 Singer Court, Suite 300,
 * Chantilly, VA, 20152.  703-961-8300.  http://www.mobile365.com.  All
 * rights reserved.
 * ---------------------------------------------------------------------------
 * This file contains unpublished proprietary source code of Mobile 365,
 * Inc.  The material in this file is the exclusive confidential property of
 * Mobile 365, Inc., is intended for internal use only within Mobile 365, and
 * is protected by copyright law.  The copyright notice found above does not
 * evidence any actual or intended publication of this source material.  Any
 * reproduction or distribution of the material in this file, either in whole
 * or in part, without the explicit prior written approval of Mobile 365, Inc.
 * is prohibited and will be prosecuted to the maximum extent possible under
 * the law.
 * ---------------------------------------------------------------------------
 *
 *   File Name: HttpMoServlet.java
 *
 *   Contents: Sample source file for receiving both MT notifications and MO
 *             messages.  The MT notifications are sent from the Mobile 365
 *             hub to the server via the HTTP GET method.  These messages are
 *             handled by overriding the doGet method of the HttpServlet class.
 *             The MO messages are sent via the HTTP POST method and are
 *             handled by overriding the doPost method of the HttpServlet class.
 *             In each case, a hard-coded positive response is sent back to the
 *             Mobile 365 hub.  This sample code does not perform any parsing
 *             or processing of the data contained in the request, nor any
 *             error handling.  This was tested with Apache Tomcat 4.1.31
 *             running on Linux.  For further descriptions of the MT
 *             notifications and MO message formats , see the Mobile 365 HTTP
 *             interface document.
 *
 *
 *             To compile: javac HttpMoServlet.java
 *                          This file requires the JSEE jar file for the servlet
 *                          class definitions
 *
 * ---------------------------------------------------------------------------
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpMoServlet extends HttpServlet {

    // MT notifications are sent via the GET method
    public void doGet(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        // get the relevant parameters from the URL
        /*String customerid = request.getHeader("customerid");
        String orderid = request.getHeader("orderid");
        String subject = request.getHeader("subject");

        // decode the status parameter
        String status = request.getHeader("status");
        URLDecoder decoder = new URLDecoder();
        status = decoder.decode(status, "UTF-8");
*//*
        // print out parameter values
        System.out.println("Received URL parameters:");
        System.out.println("CustomerI=" + customerid);
        System.out.println("OrderId=" + orderid);
        System.out.println("Subject=" + subject);
        System.out.println("Status=" + status);
*/
        // send positive ack
        response.setStatus(response.SC_OK);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("OK");
    }

    // MO messages are sent via the POST method
    public void doPost(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        // hardcoding the buffer to 2k...a real application should use
        // a more robust method for retrieving the data
        int c, avail=2048, offset=0;
        char cbuf[] = new char[avail];
        BufferedReader in = request.getReader();

        // read the body of the http message, using buffered input
        while ((c = in.read(cbuf, offset, avail)) != -1) {
            offset += c;
            avail -= c;
        }
        in.close();

        // MO messages are contained in the body, xml-encoded
        String body = new String(cbuf);
        URLDecoder decoder = new URLDecoder();
        body = decoder.decode(body, "UTF-8");

        // parse the xml here to obtain the parameters of interest...

        // display raw xml
        System.out.println("Received request:");
        System.out.println(body);

        // send positive ack
        response.setStatus(response.SC_OK);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("OK");
    }

}
