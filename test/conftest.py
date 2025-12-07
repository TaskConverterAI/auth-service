import random
import string
import requests

import pytest

ENDPOINT_AUTH_REGISTER = '/auth/register'
ENDPOINT_AUTH_LOGIN = '/auth/login'
ENDPOINT_AUTH_REFRESH = '/auth/refresh'
ENDPOINT_AUTH_LOGOUT = '/auth/logout'
ENDPOINT_AUTH_DECODE = '/auth/decode'

# Groups endpoints
ENDPOINT_GROUPS = '/groups'
ENDPOINT_GROUP_BY_ID = '/groups/{groupId}'
ENDPOINT_GROUP_MEMBERS = '/groups/{groupId}/members'
ENDPOINT_GROUP_MEMBER_BY_ID = '/groups/{groupId}/members/{userId}'
ENDPOINT_GROUP_LEAVE = '/groups/{groupId}/leave'
ENDPOINT_MEMBER_INFO = '/groups/members/{memberId}'

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
    response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=unique_user_data)
    assert response.status_code == 201
    register_response = response.json()

    return unique_user_data | register_response

@pytest.fixture
def registered_unauthorized_user(base_url, registered_authorized_user):
    logout_user(base_url, registered_authorized_user["accessToken"])

    return {
        "username": registered_authorized_user["username"],
        "email": registered_authorized_user["email"],
        "password": registered_authorized_user["password"],
        "userId": registered_authorized_user["userId"]
    }

@pytest.fixture
def second_registered_user(base_url):
    """Creates a second unique registered user for multi-user tests"""
    username = "user_" + ''.join(random.choices(string.ascii_lowercase, k=6))
    user_data = {
        "username": username,
        "email": f"{username}@example.com",
        "password": "SecurePass123!",
    }
    response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=user_data)
    assert response.status_code == 201
    register_response = response.json()

    return user_data | register_response

@pytest.fixture
def third_registered_user(base_url):
    """Creates a third unique registered user for multi-user tests"""
    username = "user_" + ''.join(random.choices(string.ascii_lowercase, k=6))
    user_data = {
        "username": username,
        "email": f"{username}@example.com",
        "password": "SecurePass123!",
    }
    response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=user_data)
    assert response.status_code == 201
    register_response = response.json()

    return user_data | register_response

@pytest.fixture
def created_group(base_url, registered_authorized_user):
    """Creates a group owned by registered_authorized_user"""
    group_data = {
        "name": "Test Group " + ''.join(random.choices(string.ascii_lowercase, k=4)),
        "description": "Test group description",
        "userId": str(decode_token(base_url, registered_authorized_user["accessToken"])["userId"])
    }

    headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
    response = requests.post(base_url + ENDPOINT_GROUPS, json=group_data, headers=headers)
    assert response.status_code == 201

    group = response.json()
    group["owner"] = registered_authorized_user
    return group

@pytest.fixture
def group_with_members(base_url, created_group, second_registered_user, third_registered_user):
    """Creates a group with multiple members"""
    group_id = created_group["id"]
    owner_token = created_group["owner"]["accessToken"]
    headers = {"Authorization": f"Bearer {owner_token}"}

    # Add second user as admin
    add_member_data = {
        "usernameOrEmail": second_registered_user["username"],
        "role": "admin"
    }
    response = requests.post(
        base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=group_id),
        json=add_member_data,
        headers=headers
    )
    assert response.status_code == 201

    # Add third user as member
    add_member_data = {
        "usernameOrEmail": third_registered_user["email"],
        "role": "member"
    }
    response = requests.post(
        base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=group_id),
        json=add_member_data,
        headers=headers
    )
    assert response.status_code == 201

    created_group["members"] = {
        "admin": second_registered_user,
        "member": third_registered_user
    }
    return created_group

def decode_token(base_url, access_token):
    decode_data = {
        "accessToken": access_token
    }

    response = requests.post(base_url + ENDPOINT_AUTH_DECODE, json=decode_data)
    assert response.status_code == 200

    return response.json()

def logout_user(base_url, access_token):
    decode_response = decode_token(base_url, access_token)
    logout_data = {
        "userId": decode_response["userId"]
    }
    response = requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)
    assert response.status_code == 204