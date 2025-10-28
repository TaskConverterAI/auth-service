import pytest
import requests
import random
import string

ENDPOINT_AUTH_REGISTER = '/auth/register'

@pytest.fixture
def unique_user_data():
    """Generates unique user data for each test"""
    username = "user_" + ''.join(random.choices(string.ascii_lowercase, k=6))
    return {
        "username": username,
        "email": f"{username}@example.com",
        "password": "SecurePass123!",
    }

@pytest.fixture
def registered_user(base_url, unique_user_data):
    """Registers new user and returns it with tokens"""
    response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=unique_user_data)
    assert response.status_code == 200
    data = response.json()

    return {
        "userData": unique_user_data,
        "accessToken": data.get("accessToken"),
        "refreshToken": data.get("refreshToken")
    }

class TestAuthRegister:
    def test_successful_registration(self, base_url, unique_user_data):
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=unique_user_data)

        assert response.status_code == 200
        data = response.json()
        assert "accessToken" in data
        assert "refreshToken" in data
        assert data["accessToken"] is not None
        assert data["refreshToken"] is not None
    def test_registration_duplicate_email(self, base_url, registered_user, unique_user_data):
        duplicate_data = unique_user_data
        duplicate_data["email"] = registered_user["userData"]["email"]
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=duplicate_data)

        assert response.status_code == 409

    def test_registration_duplicate_username(self, base_url, registered_user, unique_user_data):
        duplicate_data = unique_user_data
        duplicate_data["username"] = registered_user["userData"]["username"]
        response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=duplicate_data)

        assert response.status_code == 409

    @pytest.mark.parametrize("field,invalid_value", [
        ("username", ""),
        ("email", "test@"),
        ("email", "@test.com"),
        ("email", "test@test"),
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
