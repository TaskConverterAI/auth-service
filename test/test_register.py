from .conftest import *

class TestAuthRegister:
    def test_successful_registration(self, base_url, unique_user_data):
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=unique_user_data)

        assert response.status_code == 201
        data = response.json()
        assert "accessToken" in data
        assert "refreshToken" in data
        assert data["accessToken"] is not None
        assert data["refreshToken"] is not None

    def test_registration_duplicate_email(self, base_url, registered_authorized_user, unique_user_data):
        duplicate_data = unique_user_data
        duplicate_data["email"] = registered_authorized_user["email"]
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=duplicate_data)

        assert response.status_code == 400

    def test_registration_duplicate_username(self, base_url, registered_authorized_user, unique_user_data):
        duplicate_data = unique_user_data
        duplicate_data["username"] = registered_authorized_user["username"]
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=duplicate_data)

        assert response.status_code == 400

    @pytest.mark.parametrize("field,invalid_value", [
        ("username", ""),
        ("email", "test@"),
        ("email", "@test.com"),
        ("password", ""),
    ])
    def test_registration_invalid_input(self, base_url, unique_user_data, field, invalid_value):
        unique_user_data[field] = invalid_value
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=unique_user_data)

        assert response.status_code == 400

    def test_registration_empty_body(self, base_url):
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json={})

        assert response.status_code == 400

    @pytest.mark.parametrize("missing_field", [
        "username",
        "email",
        "password"
    ])
    def test_registration_missing_fields(self, base_url, unique_user_data, missing_field):
        unique_user_data.pop(missing_field)
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=missing_field)

        assert response.status_code == 400
