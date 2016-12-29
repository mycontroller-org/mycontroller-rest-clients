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
package org.mycontroller.restclient.philips.hue.jaxrs.handlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.mycontroller.restclient.philips.hue.model.State;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LightsHandler {

    @GET
    @Path("/{authorizedUser}/lights")
    Response getAll(@PathParam("authorizedUser") String authorizedUser);

    @GET
    @Path("/{authorizedUser}/lights/new")
    Response getNew(@PathParam("authorizedUser") String authorizedUser);

    @POST
    @Path("/{authorizedUser}/lights")
    Response searchNew(@PathParam("authorizedUser") String authorizedUser);

    @GET
    @Path("/{authorizedUser}/lights/{id}")
    Response get(@PathParam("authorizedUser") String authorizedUser, @PathParam("id") String id);

    @PUT
    @Path("/{authorizedUser}/lights/{id}")
    Response updateName(@PathParam("authorizedUser") String authorizedUser, @PathParam("id") String id, String name);

    @PUT
    @Path("/{authorizedUser}/lights/{id}/state")
    Response updateState(@PathParam("authorizedUser") String authorizedUser, @PathParam("id") String id, State state);

    @DELETE
    @Path("/{authorizedUser}/lights/{id}")
    Response delete(@PathParam("authorizedUser") String authorizedUser, @PathParam("id") String id);

}