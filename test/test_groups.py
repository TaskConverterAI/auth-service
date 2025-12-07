import pytest
import requests
from conftest import (
    ENDPOINT_GROUPS,
    ENDPOINT_GROUP_BY_ID,
    decode_token, logout_user
)

class TestCreateGroup:
    """Tests for POST /groups"""

    def test_create_group_success(self, base_url, registered_authorized_user):
        """Test successful group creation"""
        user_id = decode_token(base_url, registered_authorized_user["accessToken"])["userId"]
        group_data = {
            "name": "My Test Group",
            "description": "A test group description",
            "userId": str(user_id)
        }

        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        response = requests.post(base_url + ENDPOINT_GROUPS, json=group_data, headers=headers)

        assert response.status_code == 201
        data = response.json()
        assert "id" in data
        assert data["name"] == "My Test Group"
        assert data["description"] == "A test group description"
        assert data["ownerId"] == user_id
        assert "createdAt" in data
        assert data["memberCount"] == 1

    def test_create_group_without_description(self, base_url, registered_authorized_user):
        """Test creating group without optional description"""
        user_id = decode_token(base_url, registered_authorized_user["accessToken"])["userId"]
        group_data = {
            "name": "Minimal Group",
            "userId": str(user_id)
        }

        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        response = requests.post(base_url + ENDPOINT_GROUPS, json=group_data, headers=headers)

        assert response.status_code == 201
        data = response.json()
        assert data["name"] == "Minimal Group"

    def test_create_group_unauthenticated(self, base_url, registered_authorized_user):
        """Test creating group without authentication"""
        decoded_token = decode_token(base_url, registered_authorized_user["accessToken"])
        group_data = {
            "name": "Test Group",
            "userId": decoded_token["userId"]
        }

        logout_user(base_url, registered_authorized_user["accessToken"])

        response = requests.post(base_url + ENDPOINT_GROUPS, json=group_data)
        assert response.status_code == 401

    def test_create_group_invalid_data_missing_name(self, base_url, registered_authorized_user):
        """Test creating group with missing name"""
        user_id = decode_token(base_url, registered_authorized_user["accessToken"])["userId"]
        group_data = {
            "userId": str(user_id)
        }

        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        response = requests.post(base_url + ENDPOINT_GROUPS, json=group_data, headers=headers)

        assert response.status_code == 400

    def test_create_group_invalid_data_name_too_long(self, base_url, registered_authorized_user):
        """Test creating group with name exceeding max length"""
        user_id = decode_token(base_url, registered_authorized_user["accessToken"])["userId"]
        group_data = {
            "name": "A" * 101,  # Exceeds 100 character limit
            "userId": str(user_id)
        }

        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        response = requests.post(base_url + ENDPOINT_GROUPS, json=group_data, headers=headers)

        assert response.status_code == 400


class TestGetAllGroups:
    """Tests for GET /groups"""

    def test_get_all_groups_success(self, base_url, created_group):
        """Test getting all groups for authenticated user"""
        headers = {"Authorization": f"Bearer {created_group['owner']['accessToken']}"}
        response = requests.get(base_url + ENDPOINT_GROUPS, headers=headers)

        assert response.status_code == 200
        data = response.json()
        assert isinstance(data, list)
        assert len(data) >= 1

        # Verify the created group is in the list
        group_ids = [g["id"] for g in data]
        assert created_group["id"] in group_ids

        # Check GroupSummary structure
        for group in data:
            assert "id" in group
            assert "name" in group
            assert "memberCount" in group
            assert "createdAt" in group

    def test_get_all_groups_empty_list(self, base_url, registered_authorized_user):
        """Test getting groups when user has no groups"""
        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        response = requests.get(base_url + ENDPOINT_GROUPS, headers=headers)

        assert response.status_code == 200
        data = response.json()
        assert isinstance(data, list)

    def test_get_all_groups_unauthenticated(self, base_url):
        """Test getting groups without authentication"""
        response = requests.get(base_url + ENDPOINT_GROUPS)
        assert response.status_code == 401


class TestGetGroupDetails:
    """Tests for GET /groups/{groupId}"""

    def test_get_group_details_as_owner(self, base_url, created_group):
        """Test getting group details as the owner"""
        headers = {"Authorization": f"Bearer {created_group['owner']['accessToken']}"}
        url = base_url + ENDPOINT_GROUP_BY_ID.format(groupId=created_group["id"])
        response = requests.get(url, headers=headers)

        assert response.status_code == 200
        data = response.json()
        assert data["id"] == created_group["id"]
        assert data["name"] == created_group["name"]
        assert "ownerId" in data
        assert "members" in data
        assert isinstance(data["members"], list)

    def test_get_group_details_as_member(self, base_url, group_with_members):
        """Test getting group details as a member"""
        member_token = group_with_members["members"]["member"]["accessToken"]
        headers = {"Authorization": f"Bearer {member_token}"}
        url = base_url + ENDPOINT_GROUP_BY_ID.format(groupId=group_with_members["id"])
        response = requests.get(url, headers=headers)

        assert response.status_code == 200
        data = response.json()
        assert data["id"] == group_with_members["id"]

    def test_get_group_details_not_member(self, base_url, created_group, second_registered_user):
        """Test getting group details when not a member"""
        headers = {"Authorization": f"Bearer {second_registered_user['accessToken']}"}
        url = base_url + ENDPOINT_GROUP_BY_ID.format(groupId=created_group["id"])
        response = requests.get(url, headers=headers)

        assert response.status_code == 403

    def test_get_group_details_nonexistent(self, base_url, registered_authorized_user):
        """Test getting details for non-existent group"""
        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        url = base_url + ENDPOINT_GROUP_BY_ID.format(groupId=999999)
        response = requests.get(url, headers=headers)

        assert response.status_code == 404

    def test_get_group_details_unauthenticated(self, base_url, created_group):
        """Test getting group details without authentication"""
        url = base_url + ENDPOINT_GROUP_BY_ID.format(groupId=created_group["id"])
        response = requests.get(url, headers={})

        assert response.status_code == 401