import random
import string
import requests

import pytest

ENDPOINT_AUTH_REGISTER = '/auth/register'
ENDPOINT_AUTH_LOGIN = '/auth/login'
ENDPOINT_AUTH_REFRESH = '/auth/refresh'
ENDPOINT_AUTH_LOGOUT = '/auth/logout'
ENDPOINT_AUTH_DECODE = '/auth/decode'

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
def registered_authorized_user(base_url, unique_user_data):
    """Registers new user, returns it with tokens and decoded token data"""
    response= requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=unique_user_data)
    assert response.status_code == 201
    register_response= response.json()

    return unique_user_data | register_response

@pytest.fixture
def registered_unauthorized_user(base_url, registered_authorized_user):
    logout_user(base_url, registered_authorized_user["accessToken"])

    return {
        "username": registered_authorized_user["username"],
        "email": registered_authorized_user["email"],
        "password": registered_authorized_user["password"]
    }

def decode_token(base_url, access_token):
    decode_data = {
        "accessToken": access_token
    }

    response= requests.post(base_url + ENDPOINT_AUTH_DECODE, json=decode_data)
    assert response.status_code == 200

    return response.json()

def logout_user(base_url, access_token):
    decode_response = decode_token(base_url, access_token)
    logout_data = {
        "userId": decode_response["userId"]
    }
    response= requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)
    assert response.status_code == 204