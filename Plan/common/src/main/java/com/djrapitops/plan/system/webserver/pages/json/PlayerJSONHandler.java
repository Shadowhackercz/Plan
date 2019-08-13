/*
 *  This file is part of Player Analytics (Plan).
 *
 *  Plan is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License v3 as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Plan. If not, see <https://www.gnu.org/licenses/>.
 */
package com.djrapitops.plan.system.webserver.pages.json;

import com.djrapitops.plan.api.exceptions.WebUserAuthException;
import com.djrapitops.plan.api.exceptions.connection.WebException;
import com.djrapitops.plan.data.WebUser;
import com.djrapitops.plan.system.Identifiers;
import com.djrapitops.plan.system.json.PlayerJSONParser;
import com.djrapitops.plan.system.webserver.Request;
import com.djrapitops.plan.system.webserver.RequestTarget;
import com.djrapitops.plan.system.webserver.auth.Authentication;
import com.djrapitops.plan.system.webserver.pages.PageHandler;
import com.djrapitops.plan.system.webserver.response.Response;
import com.djrapitops.plan.system.webserver.response.data.JSONResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class PlayerJSONHandler implements PageHandler {

    private final Identifiers identifiers;
    private final PlayerJSONParser jsonParser;

    @Inject
    public PlayerJSONHandler(Identifiers identifiers, PlayerJSONParser jsonParser) {
        this.identifiers = identifiers;
        this.jsonParser = jsonParser;
    }

    @Override
    public Response getResponse(Request request, RequestTarget target) throws WebException {
        UUID playerUUID = identifiers.getPlayerUUID(target); // Can throw BadRequestException
        return new JSONResponse(jsonParser.createJSONAsMap(playerUUID));
    }

    @Override
    public boolean isAuthorized(Authentication auth, RequestTarget target) throws WebUserAuthException {
        WebUser webUser = auth.getWebUser();
        return webUser.getPermLevel() <= 1 || webUser.getName().equalsIgnoreCase(target.get(target.size() - 1));

    }
}