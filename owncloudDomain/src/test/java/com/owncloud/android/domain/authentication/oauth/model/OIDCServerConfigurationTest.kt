/**
 * ownCloud Android client application
 *
 * Copyright (C) 2026 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.domain.authentication.oauth.model

import com.owncloud.android.domain.authentication.oauth.model.OIDCServerConfiguration.Companion.TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_BASIC
import com.owncloud.android.domain.authentication.oauth.model.OIDCServerConfiguration.Companion.TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_POST
import com.owncloud.android.testutil.oauth.OC_OIDC_SERVER_CONFIGURATION
import org.junit.Assert.assertEquals
import org.junit.Test

class OIDCServerConfigurationTest {

    @Test
    fun `client registration auth method prefers basic when basic and post are supported`() {
        val configuration = buildConfiguration(
            tokenEndpointAuthMethodsSupported = listOf(
                TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_POST,
                TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_BASIC,
            )
        )

        assertEquals(TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_BASIC, configuration.getClientRegistrationTokenEndpointAuthMethod())
    }

    @Test
    fun `client registration auth method uses post when it is the only supported method`() {
        val configuration = buildConfiguration(
            tokenEndpointAuthMethodsSupported = listOf(TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_POST)
        )

        assertEquals(TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_POST, configuration.getClientRegistrationTokenEndpointAuthMethod())
    }

    @Test
    fun `client registration auth method falls back to basic when supported methods are null`() {
        val configuration = buildConfiguration(tokenEndpointAuthMethodsSupported = null)

        assertEquals(TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_BASIC, configuration.getClientRegistrationTokenEndpointAuthMethod())
    }

    @Test
    fun `client registration auth method falls back to basic when supported methods are empty`() {
        val configuration = buildConfiguration(tokenEndpointAuthMethodsSupported = emptyList())

        assertEquals(TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_BASIC, configuration.getClientRegistrationTokenEndpointAuthMethod())
    }

    @Test
    fun `client registration auth method falls back to basic when no supported method is advertised`() {
        val configuration = buildConfiguration(tokenEndpointAuthMethodsSupported = listOf("client_secret_jwt"))

        assertEquals(TOKEN_ENDPOINT_AUTH_METHOD_CLIENT_SECRET_BASIC, configuration.getClientRegistrationTokenEndpointAuthMethod())
    }

    private fun buildConfiguration(tokenEndpointAuthMethodsSupported: List<String>?): OIDCServerConfiguration =
        OC_OIDC_SERVER_CONFIGURATION.copy(
            tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported,
        )
}
