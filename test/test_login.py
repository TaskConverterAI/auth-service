from .conftest import *

class TestAuthLogin:

    def test_successful_login_with_username(self, base_url, registered_authorized_user):
        login_data = {
            "usernameOrEmail": registered_authorized_user["username"],
            "password": registered_authorized_user["password"]
        }
        response = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=login_data)

        assert response.status_code == 200
        data = response.json()
        assert "accessToken" in data
        assert "refreshToken" in data

    def test_successful_login_with_email(self, base_url, registered_authorized_user):
        login_data = {
            "usernameOrEmail": registered_authorized_user["email"],
            "password": registered_authorized_user["password"]
        }
        response = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=login_data)

        assert response.status_code == 200
        data = response.json()
        assert "accessToken" in data
        assert "refreshToken" in data

    def test_login_wrong_password(self, base_url, registered_authorized_user):
        login_data = {
            "usernameOrEmail": registered_authorized_user["username"],
            "password": "WrongPassword123!"
        }
        response = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=login_data)

        assert response.status_code == 401

    def test_login_nonexistent_user(self, base_url):
        login_data = {
            "usernameOrEmail": "nonexistent_user_12345",
            "password": "SomePassword123!"
        }
        response = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=login_data)

        assert response.status_code == 401

    @pytest.mark.parametrize("invalid_data", [
        {"usernameOrEmail": "", "password": "Pass123!"},
        {"usernameOrEmail": "testuser", "password": ""},
        {"usernameOrEmail": "", "password": ""},
    ])
    def test_login_empty_fields(self, base_url, invalid_data):
        response = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=invalid_data)

        assert response.status_code == 400

    def test_login_empty_body(self, base_url):
        response = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json={})

        assert response.status_code == 400
