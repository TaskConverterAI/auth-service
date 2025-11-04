from .conftest import *

class TestAuthIntegration:
    def test_full_authentication_flow(self, base_url, unique_user_data):
        """full cycle test: register -> decode -> logout -> login -> refresh -> decode -> logout"""
        # 1. Register
        register_response = requests.post(base_url + ENDPOINT_AUTH_REGISTER, json=unique_user_data)
        assert register_response.status_code == 201
        tokens = register_response.json()

        # 2. Decode
        decode_data = {"accessToken": tokens["accessToken"]}
        response= requests.post(base_url + ENDPOINT_AUTH_DECODE, json=decode_data)
        decode_response= response.json()
        assert response.status_code == 200

        # 2. Logout
        logout_data = {"userId": decode_response["userId"]}
        logout_response = requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)
        assert logout_response.status_code == 204

        # 2. Login
        login_data = {
            "usernameOrEmail": unique_user_data["username"],
            "password": unique_user_data["password"]
        }
        login_response = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=login_data)
        assert login_response.status_code == 200
        new_tokens = login_response.json()

        # 3. Refresh
        refresh_data = {"refreshToken": new_tokens["refreshToken"]}
        refresh_response = requests.post(base_url + ENDPOINT_AUTH_REFRESH, json=refresh_data)
        assert refresh_response.status_code == 200
        refreshed_token = refresh_response.json()

        # 4. Decode
        decode_data = {"accessToken": refreshed_token["accessToken"]}
        response= requests.post(base_url + ENDPOINT_AUTH_DECODE, json=decode_data)
        decode_response= response.json()
        assert response.status_code == 200

        # 5. Logout
        logout_data = {"userId": decode_response["userId"]}
        logout_response = requests.post(base_url + ENDPOINT_AUTH_LOGOUT, json=logout_data)
        assert logout_response.status_code == 204

    def test_multiple_logins_same_user(self, base_url, registered_unauthorized_user):
        login_data = {
            "usernameOrEmail": registered_unauthorized_user["username"],
            "password": registered_unauthorized_user["password"]
        }

        response1 = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=login_data)
        assert response1.status_code == 200

        response2 = requests.post(base_url + ENDPOINT_AUTH_LOGIN, json=login_data)
        assert response2.status_code == 200
