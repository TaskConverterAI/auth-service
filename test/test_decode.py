from .conftest import *


class TestAuthDecode:
    def test_successful_token_decode(self, base_url, registered_authorized_user):
        decode_data = {
            "accessToken": registered_authorized_user["accessToken"]
        }
        response = requests.post(base_url + ENDPOINT_AUTH_DECODE, json=decode_data)

        assert response.status_code == 200
        data = response.json()
        assert "userId" in data
        assert "role" in data or "roles" in data

    def test_decode_invalid_token(self, base_url, registered_authorized_user):
        logout_user(base_url, registered_authorized_user["accessToken"])

        decode_data = {
            "accessToken": registered_authorized_user["accessToken"]
        }
        response = requests.post(base_url + ENDPOINT_AUTH_DECODE, json=decode_data)

        assert response.status_code == 401

    def test_decode_empty_token(self, base_url):
        decode_data = {
            "accessToken": ""
        }
        response = requests.post(base_url + ENDPOINT_AUTH_DECODE, json=decode_data)

        assert response.status_code == 400

    def test_decode_empty_body(self, base_url):
        response = requests.post(base_url + ENDPOINT_AUTH_DECODE, json={})

        assert response.status_code == 400
