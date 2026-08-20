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

enum class OAuthClientAuthenticationMethod(
    val value: String,
    val useAuthorizationHeader: Boolean,
) {
    CLIENT_SECRET_BASIC("client_secret_basic", true),
    CLIENT_SECRET_POST("client_secret_post", false),
    ;

    companion object {
        @JvmStatic
        fun fromValueOrDefault(value: String?): OAuthClientAuthenticationMethod =
            if (value == CLIENT_SECRET_POST.value) CLIENT_SECRET_POST else CLIENT_SECRET_BASIC
    }
}
