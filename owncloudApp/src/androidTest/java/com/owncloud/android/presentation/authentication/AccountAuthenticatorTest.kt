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

package com.owncloud.android.presentation.authentication

import com.owncloud.android.domain.authentication.oauth.model.OAuthClientAuthenticationMethod
import com.owncloud.android.testutil.oauth.OC_OIDC_SERVER_CONFIGURATION
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountAuthenticatorTest {

    @Test
    fun dynamicClientReusesStoredPostMethod() {
        val discoveryWithBasicOnly = OC_OIDC_SERVER_CONFIGURATION.copy(
            tokenEndpointAuthMethodsSupported = listOf(
                OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC.value
            )
        )

        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_POST,
            AccountAuthenticator.getClientAuthenticationMethod(
                true,
                OAuthClientAuthenticationMethod.CLIENT_SECRET_POST.value,
                discoveryWithBasicOnly
            )
        )
    }

    @Test
    fun preFixDynamicClientDefaultsToBasic() {
        val discoveryWithPostOnly = OC_OIDC_SERVER_CONFIGURATION.copy(
            tokenEndpointAuthMethodsSupported = listOf(
                OAuthClientAuthenticationMethod.CLIENT_SECRET_POST.value
            )
        )

        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC,
            AccountAuthenticator.getClientAuthenticationMethod(true, null, discoveryWithPostOnly)
        )
    }

    @Test
    fun hardcodedClientUsesPostWhenAdvertised() {
        val discoveryWithPost = OC_OIDC_SERVER_CONFIGURATION.copy(
            tokenEndpointAuthMethodsSupported = listOf(
                OAuthClientAuthenticationMethod.CLIENT_SECRET_POST.value
            )
        )

        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_POST,
            AccountAuthenticator.getClientAuthenticationMethod(false, null, discoveryWithPost)
        )
    }

    @Test
    fun hardcodedClientDefaultsToBasicWhenDiscoveryFails() {
        assertEquals(
            OAuthClientAuthenticationMethod.CLIENT_SECRET_BASIC,
            AccountAuthenticator.getClientAuthenticationMethod(false, null, null)
        )
    }
}
