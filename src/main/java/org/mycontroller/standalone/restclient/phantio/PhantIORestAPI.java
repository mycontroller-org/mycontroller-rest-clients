/*
 * Copyright 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.restclient.phantio;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PhantIORestAPI {

    @DELETE
    @Path("input/{publicKey}")
    Response clear(@PathParam("publicKey") String publicKey);

    @POST
    @Path("input/{publicKey}")
    Response post(@PathParam("publicKey") String publicKey, ObjectNode objectNode);

    @GET
    @Path("output/{publicKey}.json")
    Response get(
            @PathParam("publicKey") String publicKey,
            @QueryParam("timezone") String timezone,
            @QueryParam("limit") Long limit);

    @GET
    @Path("output/{publicKey}/stats.json")
    Response stats(@PathParam("publicKey") String publicKey);
}
