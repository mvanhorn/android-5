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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OAuthClientAuthenticationMethodTest {

    @Test
    fun `basic is preferred when discovery advertises both supported methods`() {
        val configuration = createConfiguration(
            listOf(
                OAuthClientAuthenticationMethod.CLIENT_SECRET_POST.value,
                OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC.value,
            )
        )

        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC,
            configuration.getClientAuthenticationMethodForRegistration()
        )
    }

    @Test
    fun `post is selected when it is the only supported method`() {
        val configuration = createConfiguration(
            listOf(OAuthClientAuthenticationMethod.CLIENT_SECRET_POST.value)
        )

        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_POST,
            configuration.getClientAuthenticationMethodForRegistration()
        )
    }

    @Test
    fun `basic is selected when methods are missing empty or unsupported`() {
        listOf(null, emptyList(), listOf("private_key_jwt")).forEach { supportedMethods ->
            assertEquals(
                OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                createConfiguration(supportedMethods).getClientAuthenticationMethodForRegistration()
            )
        }
    }

    @Test
    fun `stored values map to request placement with basic as compatibility default`() {
        assertTrue(OAuthClientAuthenticationMethod.fromValueOrDefault("client_secret_basic").useAuthorizationHeader)
        assertFalse(OAuthClientAuthenticationMethod.fromValueOrDefault("client_secret_post").useAuthorizationHeader)
        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC,
            OAuthClientAuthenticationMethod.fromValueOrDefault(null)
        )
        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC,
            OAuthClientAuthenticationMethod.fromValueOrDefault("unknown")
        )
    }

    private fun createConfiguration(
        tokenEndpointAuthMethodsSupported: List<String>?
    ): OIDCServerConfiguration =
        OIDCServerConfiguration(
            authorizationEndpoint = "https://server.test/authorize",
            checkSessionIframe = null,
            endSessionEndpoint = null,
            issuer = "https://server.test",
            registrationEndpoint = "https://server.test/register",
            responseTypesSupported = null,
            scopesSupported = null,
            tokenEndpoint = "https://server.test/token",
            tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported,
            userInfoEndpoint = null,
        )
}
