from .conftest import *


class TestAuthLogout:
    def test_successful_logout(self, base_url, registered_authorized_user):
        decode_response = decode_token(base_url, registered_authorized_user["accessToken"])
        logout_data = {
            "userId": decode_response["userId"]
        }
        response = requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)

        assert response.status_code == 204

    def test_logout_invalidates_refresh_token(self, base_url, registered_authorized_user):
        decode_response = decode_token(base_url, registered_authorized_user["accessToken"])
        logout_data = {
            "userId": decode_response["userId"]
        }
        logout_response = requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)
        assert logout_response.status_code == 204

        # try to use same refresh token
        refresh_data = {
            "refreshToken": registered_authorized_user["refreshToken"]
        }
        refresh_response = requests.post(base_url + ENDPOINT_AUTH_REFRESH, json=refresh_data)
        assert refresh_response.status_code == 401

    def test_logout_with_empty_userid(self, base_url):
        logout_data = {
            "userId": ""
        }
        response= requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)

        assert response.status_code == 400

    def test_double_logout(self, base_url, registered_authorized_user):
        decode_response = decode_token(base_url, registered_authorized_user["accessToken"])

        logout_data = {
            "userId": decode_response["userId"]
        }
        response= requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)
        assert response.status_code == 204

        response= requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)
        assert response.status_code == 401

    def test_logout_empty_body(self, base_url):
        response = requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json={})

        assert response.status_code == 400
