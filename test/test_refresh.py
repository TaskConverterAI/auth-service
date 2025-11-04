from .conftest import *

class TestAuthRefresh:
    def test_successful_token_refresh(self, base_url, registered_authorized_user):
        refresh_data = {
            "refreshToken": registered_authorized_user["refreshToken"]
        }
        response = requests.post(base_url + ENDPOINT_AUTH_REFRESH, json=refresh_data)

        assert response.status_code == 200
        data = response.json()
        assert "accessToken" in data
        assert data["accessToken"] is not None

    def test_refresh_with_invalid_token(self, base_url):
        refresh_data = {
            "refreshToken": "invalid.token.here"
        }
        response = requests.post(base_url + ENDPOINT_AUTH_REFRESH, json=refresh_data)

        assert response.status_code == 401

    def test_refresh_with_empty_token(self, base_url):
        refresh_data = {
            "refreshToken": ""
        }
        response = requests.post(base_url + ENDPOINT_AUTH_REFRESH, json=refresh_data)

        assert response.status_code == 400

    def test_refresh_empty_body(self, base_url):
        response = requests.post(base_url + ENDPOINT_AUTH_REFRESH, json={})

        assert response.status_code == 400
