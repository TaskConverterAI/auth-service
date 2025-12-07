import pytest
import requests
from conftest import (
    ENDPOINT_GROUP_MEMBERS,
    ENDPOINT_GROUP_MEMBER_BY_ID,
    ENDPOINT_MEMBER_INFO,
    decode_token
)


class TestGetMemberInfo:
    """Tests for GET /groups/members/{memberId}"""

    def test_get_member_info_success(self, base_url, group_with_members):
        """Test getting user information by member ID"""
        owner_token = group_with_members["owner"]["accessToken"]
        member_user_id = decode_token(base_url, group_with_members["members"]["member"]["accessToken"])["userId"]

        headers = {"Authorization": f"Bearer {owner_token}"}
        url = base_url + ENDPOINT_MEMBER_INFO.format(memberId=member_user_id)
        response = requests.get(url, headers=headers)

        assert response.status_code == 200
        data = response.json()
        assert "userId" in data
        assert "username" in data
        assert "email" in data

    def test_get_member_info_nonexistent(self, base_url, registered_authorized_user):
        """Test getting info for non-existent member"""
        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        url = base_url + ENDPOINT_MEMBER_INFO.format(memberId=999999)
        response = requests.get(url, headers=headers)

        assert response.status_code == 404


class TestAddMember:
    """Tests for POST /groups/{groupId}/members"""

    def test_add_member_by_username_as_owner(self, base_url, created_group, second_registered_user):
        """Test adding member by username as group owner"""
        headers = {"Authorization": f"Bearer {created_group['owner']['accessToken']}"}
        add_data = {
            "usernameOrEmail": second_registered_user["username"],
            "role": "member"
        }

        url = base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=created_group["id"])
        response = requests.post(url, json=add_data, headers=headers)

        assert response.status_code == 201
        data = response.json()
        assert "userId" in data
        assert data["username"] == second_registered_user["username"]
        assert data["role"] == "member"
        assert "joinedAt" in data

    def test_add_member_by_email_as_owner(self, base_url, created_group, second_registered_user):
        """Test adding member by email as group owner"""
        headers = {"Authorization": f"Bearer {created_group['owner']['accessToken']}"}
        add_data = {
            "usernameOrEmail": second_registered_user["email"],
            "role": "admin"
        }

        url = base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=created_group["id"])
        response = requests.post(url, json=add_data, headers=headers)

        assert response.status_code == 201
        data = response.json()
        assert data["email"] == second_registered_user["email"]
        assert data["role"] == "admin"

    def test_add_member_as_admin(self, base_url, group_with_members, third_registered_user):
        """Test that admin can add members"""
        # Create a new user to add
        import random
        import string
        username = "user_" + ''.join(random.choices(string.ascii_lowercase, k=6))
        new_user_data = {
            "username": username,
            "email": f"{username}@example.com",
            "password": "SecurePass123!",
        }
        response = requests.post(base_url + "/auth/register", json=new_user_data)
        assert response.status_code == 201

        admin_token = group_with_members["members"]["admin"]["accessToken"]
        headers = {"Authorization": f"Bearer {admin_token}"}
        add_data = {
            "usernameOrEmail": username,
            "role": "member"
        }

        url = base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=group_with_members["id"])
        response = requests.post(url, json=add_data, headers=headers)

        assert response.status_code == 201

    def test_add_member_already_in_group(self, base_url, group_with_members):
        """Test adding member who is already in the group"""
        headers = {"Authorization": f"Bearer {group_with_members['owner']['accessToken']}"}
        add_data = {
            "usernameOrEmail": group_with_members["members"]["admin"]["username"]
        }

        url = base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=group_with_members["id"])
        response = requests.post(url, json=add_data, headers=headers)

        assert response.status_code == 400

    def test_add_member_nonexistent_user(self, base_url, created_group):
        """Test adding non-existent user"""
        headers = {"Authorization": f"Bearer {created_group['owner']['accessToken']}"}
        add_data = {
            "usernameOrEmail": "nonexistent_user_12345"
        }

        url = base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=created_group["id"])
        response = requests.post(url, json=add_data, headers=headers)

        assert response.status_code == 404

    def test_add_member_nonexistent_group(self, base_url, registered_authorized_user, second_registered_user):
        """Test adding member to non-existent group"""
        headers = {"Authorization": f"Bearer {registered_authorized_user['accessToken']}"}
        add_data = {
            "usernameOrEmail": second_registered_user["username"]
        }

        url = base_url + ENDPOINT_GROUP_MEMBERS.format(groupId=999999)
        response = requests.post(url, json=add_data, headers=headers)

        assert response.status_code == 404

class TestRemoveMember:
    """Tests for DELETE /groups/{groupId}/members/{userId}"""

    def test_remove_member_as_owner(self, base_url, group_with_members):
        """Test removing member as group owner"""
        member_user_id = decode_token(base_url, group_with_members["members"]["member"]["accessToken"])["userId"]

        headers = {"Authorization": f"Bearer {group_with_members['owner']['accessToken']}"}
        url = base_url + ENDPOINT_GROUP_MEMBER_BY_ID.format(
            groupId=group_with_members["id"],
            userId=member_user_id
        )
        response = requests.delete(url, headers=headers)

        assert response.status_code == 204

    def test_remove_member_as_admin(self, base_url, group_with_members):
        """Test removing member as group admin"""
        member_user_id = decode_token(base_url, group_with_members["members"]["member"]["accessToken"])["userId"]

        headers = {"Authorization": f"Bearer {group_with_members['members']['admin']['accessToken']}"}
        url = base_url + ENDPOINT_GROUP_MEMBER_BY_ID.format(
            groupId=group_with_members["id"],
            userId=member_user_id
        )
        response = requests.delete(url, headers=headers)

        assert response.status_code == 204

    def test_remove_nonexistent_member(self, base_url, created_group):
        """Test removing member who is not in the group"""
        headers = {"Authorization": f"Bearer {created_group['owner']['accessToken']}"}
        url = base_url + ENDPOINT_GROUP_MEMBER_BY_ID.format(
            groupId=created_group["id"],
            userId=999999
        )
        response = requests.delete(url, headers=headers)

        assert response.status_code == 404
